(ns pl.fermich.invest-calendar.full
  (:require [pl.fermich.invest-calendar.parser :as p]))

(defn -main [& args]
  (p/fetch-events-starting-from 2010 01 01))
