(ns pl.fermich.invest-calendar.time
  (:require [clj-time.periodic :as p]
            [clj-time.core :as t]
            [clj-time.format :as tf]))

(defn dates-sequence [from to format]
  (let [format (tf/formatter format)
        dates (p/periodic-seq from to (t/hours 24))]
    (map #(tf/unparse format %) dates)))

(defn calculate-dates [y m d]
  (dates-sequence (t/date-time y m d) (t/now) "yyyy-MM-dd"))

(defn calculate-plain-dates [y m d]
  (dates-sequence (t/date-time y m d) (t/now) "yyyyMMdd"))

(defn split-date [date]
  (let [yy (subs date 0 4)
        mm (subs date 4 6)
        dd (subs date 6 8)
        date-splitted [yy mm dd]]
    (map #(Integer/parseInt %) date-splitted)))
