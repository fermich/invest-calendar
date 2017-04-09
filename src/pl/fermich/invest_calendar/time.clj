(ns pl.fermich.invest-calendar.time
  (:require [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

(defn calculate-dates [y m d]
  (let [format (tf/formatter "yyyy-MM-dd")
        dates (p/periodic-seq (t/date-time y m d) (t/now) (t/hours 24))]
    (map #(tf/unparse format %) dates)))
