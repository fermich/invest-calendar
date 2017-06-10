(ns pl.fermich.invest-data.time
  (:require [clj-time.periodic :as p]
            [clj-time.core :as tc]
            [clj-time.format :as tf]))

(defn dates-sequence [from to format]
  (let [formatter (tf/formatter format)
        dates (p/periodic-seq from to (tc/hours 24))]
    (map #(tf/unparse formatter %) dates)))

(defn calculate-dates [y m d]
  (dates-sequence (tc/date-time y m d) (tc/now) "yyyy-MM-dd"))

(defn calculate-plain-dates [y m d]
  (dates-sequence (tc/date-time y m d) (tc/now) "yyyyMMdd"))

(defn split-date [date]
  (let [yy (subs date 0 4)
        mm (subs date 4 6)
        dd (subs date 6 8)
        date-splitted [yy mm dd]]
    (map #(Integer/parseInt %) date-splitted)))

(defn first-day-of-the-month [date]
  (let [splitted (split-date date)]
    (concat (take 2 splitted) [01])))
