(ns pl.fermich.invest-data.format
  (:require [clojure.data.csv :as csv]
            [dk.ative.docjure.spreadsheet :as x]))

(defn parse-csv-data [content]
  (csv/read-csv content))

(defn parse-xls-data [format content]
  (with-open [stream (clojure.java.io/input-stream content)]
    (some->> (x/load-workbook-from-stream stream)
             (x/select-sheet "Worksheet")
             (x/select-columns format)
             ))
  )

(defn mark-columns [labels, rows]
  (some->> rows
           (map #(interleave labels %))
           (map #(apply array-map %))))
