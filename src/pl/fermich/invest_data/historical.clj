(ns pl.fermich.invest-data.historical
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [clojure.java.jdbc :as j]
            [pl.fermich.invest-data.time :as t]
            [pl.fermich.invest-data.db :as db]
            ))

(defn- encode [str]
  (java.net.URLEncoder/encode str "UTF-8"))


(def conf (props/read-properties "resources/service.properties"))

(defn- get-history-data-by-date [request-body]
  (let [options {
                 :body request-body
                 :multi-param-style :array
                 :headers {
                           :accept "application/json, text/javascript, */*; q=0.01"
                           :accept-encoding "gzip, deflate, br"
                           :content-type "application/x-www-form-urlencoded; charset=UTF-8"
                           :x-requested-with "XMLHttpRequest"
                           :user-agent (:user-agent conf)
                           }}]
    (client/post (:historical-url conf) options)))

(defn- request-body [params]
  (let [pkeys (keys params)
        pvals (map encode (vals params))
        enc-params (zipmap pkeys pvals)]
    (format (:historical-request-body conf) (:curr enc-params) (:sml enc-params) (:header enc-params) (:start enc-params) (:end enc-params))
    ))

(defn- parse-html [body]
  (html/select (html/html-snippet body) [[:tr ]]))

(defn- cell-content [cell]
  (first (cell :content)))

(defn- drop-whitespaces [s]
  (let [trimmed (some-> s (str/replace #"\u00A0" " ") (str/trim))]
    (if (str/blank? trimmed) nil trimmed)))

(defn- parse-table-row [sml tr]
  (let [tr-content (tr :content)
        cells (filter map? tr-content)
        cell {
              :name sml
              :timestamp (some-> cells (nth 0) (:attrs) (:data-real-value) (drop-whitespaces))
              :price (some-> cells (nth 1) (:attrs) (:data-real-value) (drop-whitespaces))
              :open (some-> cells (nth 2) (:attrs) (:data-real-value) (drop-whitespaces))
              :high (some-> cells (nth 3) (:attrs) (:data-real-value) (drop-whitespaces))
              :low (some-> cells (nth 4) (:attrs) (:data-real-value) (drop-whitespaces))
              :vol (some-> cells (nth 5) (:attrs) (:data-real-value) (drop-whitespaces))
              :chg (some-> cells (nth 6) (cell-content) (drop-whitespaces))
              }
        ]
    cell
    ))

(defn fetch-historical-data [start end sml-params]
  (let [params (assoc sml-params :start (t/to-american-date start) :end (t/to-american-date end))
        req (request-body params)
        resp (get-history-data-by-date req)
        body (resp :body)]
    (some->> (parse-html body)
             (rest)
             (drop-last)
             (map #(parse-table-row (:name params) %))
             )))


(defn fetch-commodity-data [sml start end]
  (some->> (db/select-commodity-by-sml sml)
           (fetch-historical-data start end)
           (db/insert-rows (:commodities-table conf))
           ))

(defn fetch-all-commodities-data [start end]
  (let [smls (db/all-commodities-sml)
        data (map #(fetch-commodity-data (:sml %) start end) smls)]
    (prn data)
    ))


(defn fetch-index-data [name start end]
  (some->> (db/select-index-by-name name)
           (fetch-historical-data start end)
           (db/insert-rows (:indices-table conf))
           ))

(defn fetch-all-indices-data [start end]
  (let [smls (db/all-indices-sml)
        data (map #(fetch-index-data (:sml %) start end) smls)]
    (prn data)
    ))
