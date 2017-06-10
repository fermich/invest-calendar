(ns pl.fermich.invest-data.stock
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.quotes :as q]
            [pl.fermich.invest-data.time :as t]
            [pl.fermich.invest-data.db :as db]
            ))

(def conf (props/read-properties "resources/service.properties"))

(def data-conf {:format [:ticker :dtyymmdd :open :high :low :close :vol]
                :table (:db-stock-table conf)})

(defn- load-quotes-starting-from [y m d]
  (some->> (t/calculate-plain-dates y m d)
           (map #(q/load-csv-quotes-for-date data-conf %))))

(defn current-month-diff []
  (let [last-load (db/select-last-stock-quotes-date)
        [yy mm dd] (t/split-date last-load)]
    (println "Last stock load:" last-load)
    (db/delete-stock-quotes-by-date last-load)
    (vec (load-quotes-starting-from  yy mm dd))
    ))

(defn load-from-dir [dir]
  (some->> (file-seq (clojure.java.io/file dir))
           (filter #(.isFile %))
           (map #(q/load-quotes-from-csv-file data-conf (.getPath %)))
           (vec)
           ))
