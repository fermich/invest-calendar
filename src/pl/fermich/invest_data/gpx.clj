(ns pl.fermich.invest-data.gpx
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as dzip]
            [clojure.java.io :as io]
            [pl.fermich.invest-data.time :as t]))

(defn zip-walk [f z sec]
  (if (zip/end? z)
    (zip/root z)
    (recur f (zip/next (f z (+ 10 sec))) (+ 10 sec))))

(defn update-gpx [in-file out-file]
  (let [zipper (-> in-file io/file xml/parse zip/xml-zip)
        trkseg (dzip/xml1-> zipper :gpx :trk :trkseg)
        updated (zip-walk (fn [loc sec]
                            (if (= :trkpt (-> loc zip/node :tag))
                              (zip/append-child loc {:tag :time :content [(-> sec t/seconds-from-now str)]})
                              loc))
                          trkseg
                          10)]
    (->> updated xml/emit with-out-str (spit out-file))
    ))
