(ns pl.fermich.invest-data.events
  (:require [clj-http.client :as client]
            [cheshire.core :as cheshire]
            [propertea.core :as props]
            [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [clojure.java.jdbc :as j]
            [pl.fermich.invest-data.time :as t]
            [pl.fermich.invest-data.db :as db]))

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

(defn- fetch-events [date]
  (let [resp (get-calendar-by-date date)
        body (cheshire/parse-string (resp :body))
        events (body (:events-field-name conf))]
    (some->> (parse-html events)
             (map parse-table-row)
             )))

(defn- load-events-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(fetch-events %))
           (map #(db/insert-rows (:db-table conf) %))
           (cheshire/generate-string)
           (println)
           (Thread/sleep 1000)
           (vec)))

(defn load-all-events [& args]
  (load-events-starting-from 2010 01 01))

(defn load-diff-events [& args]
  (let [last-event-date (db/select-last-event-date)
        date-splitted (str/split last-event-date #"-")
        [y m d] (map #(Integer/parseInt %) date-splitted)]
    (println "Last event: " last-event-date)
    (db/delete-events-by-date last-event-date)
    (load-events-starting-from y m d)))
