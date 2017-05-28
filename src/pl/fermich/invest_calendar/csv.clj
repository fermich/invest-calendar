(ns pl.fermich.invest-calendar.csv
  (:require [clojure.data.csv :as csv]))

(defn parse-data [content]
  (csv/read-csv content))

(defn mark-columns [labels, rows]
  (some->> rows
           (map #(interleave labels %))
           (map #(apply array-map %))))
