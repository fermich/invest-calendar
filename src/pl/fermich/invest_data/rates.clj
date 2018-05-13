(ns pl.fermich.invest-data.rates
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [net.cgrand.enlive-html :as html]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.time :as t]
            ))

(def conf (props/read-properties "resources/service.properties"))

(defn- fetch-rates-by-date [symbol od do]
  (let [options {
                 :body (str "symbol=" symbol "&od=" od "&do=" do "&period=0&format=xls" )
                 :headers {
                           :accept "application/json, text/javascript, */*; q=0.01"
                           :accept-encoding "gzip, deflate, br"
                           :content-type "application/x-www-form-urlencoded; charset=UTF-8"
                           :user-agent (:user-agent conf)
                           }
                 :follow-redirects true
                 }]
    (client/post (:rate-url conf) options)))

(defn- parse-html [body]
  (html/select (html/html-snippet body) [[:tr]]))

(defn- cell-content [cell]
  (some-> (cell :content)
           (first)
           (clojure.string/replace "," ".")))

(defn- parse-table-row [symbol tr]
  (let [tr-content (tr :content)]
    {
     :symbol symbol
     :dtyymmdd (some-> tr-content (nth 1) (cell-content))
     :rate (some-> tr-content (nth 2) (cell-content))
     :chg (some-> tr-content (nth 3) (cell-content))
     }))

(defn- load-rate [symbol od do]
  (some->> (fetch-rates-by-date symbol od do)
           (:body)
           (parse-html)
           (drop 2)
           (drop-last)
           (map #(parse-table-row symbol %))
           (db/insert-rows "rates")
           ))

(defn- load-rates [od do]
  (let [sym ["WIBOR6M" "WIBOR3M" "WIBOR1M"]]
    (vec (map #(load-rate % od do) sym))
    ))

(defn load-all-rates [& args]
  (let [from (t/calculate-months 2010 01 02)
        shifted (rest from)
        to (map #(t/first-day-of-the-month %) shifted)
        from-to (map vector from to)]
    (vec (map #(load-rates (first %) (second %)) from-to))
    ))

(defn load-diff-rates [& args]
  (let [last-load (db/select-last-rates-date)
        to (t/current-formatted-date)]
    (println "Rates last load:" last-load)
    (db/delete-rates-by-date last-load)
    (vec (load-rates last-load to))
    ))
