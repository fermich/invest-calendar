(ns pl.fermich.invest-data.options
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.quotes :as q]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(def data-conf {:format [:option :dtyymmdd :open :high :low :close :vol :volatility]
                :table (:db-option-table conf)})

(defn- load-quotes-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(q/load-xls-quotes-for-date data-conf %))
           ))

(defn current-month-diff []
  (vec (load-quotes-starting-from 2017 05 26)))


(defn load-from-dir [dir]
  ;(some->> (file-seq (clojure.java.io/file dir))
  ;         (filter #(.isFile %))
  ;         (map #(q/load-quotes-from-csv-file data-conf (.getPath %)))
  ;         (vec)
  ;         )
  )
