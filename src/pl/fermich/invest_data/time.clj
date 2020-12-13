(ns pl.fermich.invest-data.time
  (:require [clj-time.periodic :as p]
            [clj-time.core :as tc]
            [clj-time.format :as tf]))

(defn dates-sequence [from to interval format]
  (let [formatter (tf/formatter format)
        dates (p/periodic-seq from to interval)]
    (map #(tf/unparse formatter %) dates)))


(defn days-sequence [from to format]
  (dates-sequence from to (tc/hours 24) format))

(defn calculate-days [y m d]
  (days-sequence (tc/date-time y m d) (tc/now) "yyyy-MM-dd"))

(defn calculate-plain-days [y m d]
  (days-sequence (tc/date-time y m d) (tc/now) "yyyyMMdd"))


(defn months-sequence [from to format]
  (dates-sequence from to (tc/months 1) format))

(defn calculate-months [y m d]
  (months-sequence (tc/date-time y m d) (tc/now) "yyyy-MM-dd"))

(defn current-formatted-date []
  (let [formatter (tf/formatter "yyyy-MM-dd")]
    (tf/unparse formatter (tc/now))
    ))

(defn split-date [date]
  (let [yy (subs date 0 4)
        mm (subs date 4 6)
        dd (subs date 6 8)
        date-splitted [yy mm dd]]
    (map #(Integer/parseInt %) date-splitted)))

(defn to-american-date [date]
  (let [plain-date (clojure.string/replace date "-" "")
        formatter (tf/formatter "MM/dd/yyyy")
        [yy mm dd] (split-date plain-date)
        dt (tc/date-time yy mm dd)]
    (tf/unparse formatter dt)
    ))

(defn first-day-of-the-month [date]
  (let [plain-date (clojure.string/replace date "-" "")
        formatter (tf/formatter "yyyy-MM-dd")
        [yy mm dd] (split-date plain-date)
        first (tc/date-time yy mm 01)]
    (tf/unparse formatter first)
    ))

(defn seconds-from-now [ss]
  (-> ss tc/seconds tc/from-now))
