(ns pl.fermich.invest-data.options
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.quotes :as q]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(def data-conf {:format {:A :dtyymmdd, :B :ticker, :E :open, :F :high, :G :low, :H :close, :I :volatility, :J :volume}
                :table (:db-option-table conf)})

(defn- load-quotes-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(q/load-xls-quotes-for-date data-conf %))
           ))

(defn load-diff-quotes []
  (let [last-load (db/select-last-option-quotes-date)
        simple-date (clojure.string/replace last-load #"-" "")
        [yy mm dd] (t/split-date simple-date)]
    (println "Options last load:" last-load)
    (db/delete-option-quotes-by-date last-load)
    (vec (load-quotes-starting-from  yy mm dd))
    ))

(defn load-all-quotes []
  (vec (load-quotes-starting-from 2010 1 1)))
