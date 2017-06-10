(ns pl.fermich.invest-data.format
  (:require [clojure.data.csv :as csv]))

(defn parse-csv-data [content]
  (csv/read-csv content))

(defn mark-columns [labels, rows]
  (some->> rows
           (map #(interleave labels %))
           (map #(apply array-map %))))
