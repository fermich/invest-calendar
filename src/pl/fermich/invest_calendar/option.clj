(ns pl.fermich.invest-calendar.option
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-calendar.csv :as csv]
            [pl.fermich.invest-calendar.db :as db]
            [pl.fermich.invest-calendar.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(defn- if-not-404 [response]
  (let [status (:status response)]
    (if-not (= (:status response) 404)
      (:body response)
      nil)))

(defn- fetch-raw-quotes-by-date [day]
  (let [url (str (:option-url conf) day ".prn")
        options {
                 :headers {
                           :accept "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                           :accept-encoding "gzip, deflate"
                           :user-agent (:user-agent conf)
                           }
                 :throw-exceptions false
                 :follow-redirects false
                 }
        ]
    (Thread/sleep 1000)
    (some->> (client/get url options)
             (if-not-404)
             (csv/parse-data)
             )
    ))

(defn- fetch-quotes-by-date [day]
  (some->> (fetch-raw-quotes-by-date day)
           (csv/mark-columns [:option :dtyymmdd :open :high :low :close :vol :oi])
           (db/insert-rows (:db-option-table conf))
           ))

(defn- load-quotes-from-file [file]
  (some->> (slurp file)
           (csv/parse-data)
           (csv/mark-columns [:option :dtyymmdd :open :high :low :close :vol :oi])
           (db/insert-rows (:db-option-table conf))
           ))

(defn- fetch-quotes-starting-from [y m d]
  (some->> (t/calculate-plain-dates y m d)
           (map #(fetch-quotes-by-date %))))

(defn last-month-diff []
  (vec (fetch-quotes-starting-from 2017 05 01)))


(defn load-from-dir [dir]
  (some->> (file-seq (clojure.java.io/file dir))
           (filter #(.isFile %))
           (map #(load-quotes-from-file (.getPath %)))
           (vec)
           )
  )
