(ns pl.fermich.invest-calendar.option
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-calendar.csv :as csv]
            [pl.fermich.invest-calendar.quotes :as q]
            [pl.fermich.invest-calendar.db :as db]
            [pl.fermich.invest-calendar.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(def data-conf {:format [:option :dtyymmdd :open :high :low :close :vol :oi]
                :table (:db-option-table conf)})

(defn- fetch-quotes-starting-from [y m d]
  (some->> (t/calculate-plain-dates y m d)
           (map #(q/fetch-daily-quotes data-conf %))
           ))

(defn last-month-diff []
  (vec (fetch-quotes-starting-from 2017 05 01)))


(defn load-from-dir [dir]
  (some->> (file-seq (clojure.java.io/file dir))
           (filter #(.isFile %))
           (map #(q/load-quotes-from-file data-conf (.getPath %)))
           (vec)
           ))
