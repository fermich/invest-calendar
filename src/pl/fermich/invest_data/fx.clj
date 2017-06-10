(ns pl.fermich.invest-data.fx
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.format :as f]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.time :as t]))

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
             (f/parse-csv-data)
             )))

(defn- load-quotes-by-date [day pair]
  (some->> (fetch-raw-quotes-by-date day pair)
           (f/mark-columns [:ticker :per :dtyymmdd :dthhmmss :open :high :low :close :vol])
           (db/insert-rows (:db-fx-table conf))
           ))

(defn- load-quotes-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(load-quotes-by-date % "USDJPY"))))

(defn load-all-quotes []
  (vec (load-quotes-starting-from 2010 01 01)))

(defn load-diff-quotes []
  (let [last-date (db/select-last-fx-quotes-date)
        [y m d] (t/split-date last-date)]
    (println "Last quotes: " last-date)
    (db/delete-fx-quotes-by-date last-date)
    (vec (load-quotes-starting-from y m d))))
