(ns pl.fermich.invest-calendar.parser
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]
            [propertea.core :as props]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [clojure.java.jdbc :as j]
            [pl.fermich.invest-calendar.db :as db]
            [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

(def conf (props/read-properties "resources/service.properties"))

(defn- get-calendar-by-date [day]
  (let [options {
                 :body (str "dateFrom=" day "&dateTo=" day "&" (:service-params conf))
                 :multi-param-style :array
                 :headers {
                           :accept "application/json, text/javascript, */*; q=0.01"
                           :accept-encoding "gzip, deflate, br"
                           :content-type "application/x-www-form-urlencoded; charset=UTF-8"
                           :x-requested-with "XMLHttpRequest"
                           :user-agent (:user-agent conf)
                           :referer (:referer conf)
                           }}]
    (client/post (:url conf) options)))

(defn- parse-html [body]
  (html/select (html/html-snippet body) [[:tr (html/attr? :event_attr_id)]]))

(defn- cell-content [cell]
  (first (cell :content)))

(defn- drop-whitespaces [s]
  (let [trimmed (some-> s (str/replace #"\u00A0" " ") (str/trim))]
    (if (str/blank? trimmed) nil trimmed)))

(defn- parse-table-row [tr]
  (let [tr-content (tr :content)
        cells (filter map? tr-content)]
    {
     :id (some-> tr (:attrs) (:event_attr_id) (drop-whitespaces))
     :timestamp (some-> tr (:attrs) (:event_timestamp) (drop-whitespaces))
     :country (some-> cells (nth 1) (cell-content) (:attrs) (:title) (drop-whitespaces))
     :currency (some-> cells (nth 1) (:content) (last) (drop-whitespaces))
     :sentiment (some-> cells (nth 2) (:attrs) (:title) (drop-whitespaces))
     :event (some-> cells (nth 3) (cell-content) (drop-whitespaces))
     :feature (some-> cells (nth 3) (:content) (second) (:attrs) (:title) (drop-whitespaces))
     :actual (some-> cells (nth 4) (cell-content) (drop-whitespaces))
     :forecast (some-> cells (nth 5) (cell-content) (drop-whitespaces))
     :previous (some-> cells (nth 6) (cell-content) (drop-whitespaces))
     :revision (some-> cells (nth 7) (cell-content) (:attrs) (:title) (drop-whitespaces))
     }))

(defn fetch-events [date]
  (let [resp (get-calendar-by-date date)
        body (cheshire/parse-string (resp :body))
        events (body (:events-field-name conf))]
    (some->> (parse-html events)
             (map parse-table-row)
             (db/insert-events)
             (cheshire/generate-string)
             (println)
             (Thread/sleep 1000))))

(defn- calculate-dates [y m d]
  (let [format (tf/formatter "yyyy-MM-dd")
        dates (p/periodic-seq (t/date-time y m d) (t/now) (t/hours 24))]
    (map #(tf/unparse format %) dates)))

(defn fetch-events-starting-from [y m d]
  (some->> (calculate-dates y m d)
           (map #(fetch-events %))
           (vec)))
