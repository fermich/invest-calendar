(ns pl.fermich.invest-calendar.time
  (:require [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

(defn dates-sequence [from to]
  (let [format (tf/formatter "yyyy-MM-dd")
        dates (p/periodic-seq from to (t/hours 24))]
    (map #(tf/unparse format %) dates)))

(defn calculate-dates [y m d]
  (dates-sequence (t/date-time y m d) (t/now)))
