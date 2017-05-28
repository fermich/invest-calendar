(ns pl.fermich.invest-calendar.quotes
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-calendar.csv :as csv]
            [pl.fermich.invest-calendar.db :as db]
            [pl.fermich.invest-calendar.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(defn- fetch-raw-quotes-by-date [day pair]
  (let [options {
                 :body (str "backUrl=%24backUrl&data=" day "&nazwa=" pair "&format=mst&time_period=mins&send=on")
                 :headers {
                           :accept "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                           :accept-encoding "gzip, deflate"
                           :content-type "application/x-www-form-urlencoded"
                           :referer (:fx-url conf)
                           :user-agent (:user-agent conf)
                           }
                 :follow-redirects false
                 }
        ]
    (Thread/sleep 1000)
    (some->> (client/post (:fx-url conf) options)
             (:headers)
             (:location)
             (client/get)
             (:body)
             (csv/parse-data)
             )))

(defn- fetch-quotes-by-date [day pair]
  (some->> (fetch-raw-quotes-by-date day pair)
           (csv/mark-columns [:ticker :per :dtyymmdd :dthhmmss :open :high :low :close :vol])
           (db/insert-rows (:db-quotes-table conf))
           ))

(defn- fetch-quotes-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(fetch-quotes-by-date % "USDJPY"))))

(defn fetch-all-quotes []
  (vec (fetch-quotes-starting-from 2010 01 01)))

(defn fetch-diff-quotes []
  (let [last-date (db/select-last-quotes-date)
        yy (subs last-date 0 4)
        mm (subs last-date 4 6)
        dd (subs last-date 6 8)
        date-splitted [yy mm dd]
        [y m d] (map #(Integer/parseInt %) date-splitted)]
    (println "Last quotes: " last-date)
    (db/delete-quotes-by-date last-date)
    (vec (fetch-quotes-starting-from y m d))))
