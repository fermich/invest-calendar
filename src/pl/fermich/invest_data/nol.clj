(ns pl.fermich.invest-data.nol
  (:require [propertea.core :as props]
            [pl.fermich.invest-data.format :as f]
            [pl.fermich.invest-data.db :as db]
            [clojure.string :as s]
            [cheshire.core :as json])
  (:import (java.sql Timestamp)))

(def conf (props/read-properties "resources/service.properties"))

(defn- flat-instrmt [line inc]
  (->> [ (new Timestamp (:timestamp line)) (map inc [:typ :px :sz :tov :dt :tm]) (-> inc (:instrmts) (first) (map [:sym :id :src]))]
       (flatten)
       ))

(defn- flat-inc [market-data]
  (let [ parsed (json/parse-string market-data true) ]
    (->> parsed (:message) (:incs) (flatten)
         (map #(flat-instrmt parsed %1))
         (f/mark-columns [:timestamp :typ :px :sz :tov :dt :tm :sym :id :src])
         )))

(defn batch-load-from-file [file]
  (->> file (slurp)
       (s/split-lines)
       (map #(flat-inc %1))
       (flatten)
       (db/insert-rows (:db-nol-table conf))
       ))

(defn stream-load-from-file [file]
  (-> (with-open [rdr (clojure.java.io/reader file)]
    (doseq [line (line-seq rdr)]
      (flat-inc line)
      ))))
