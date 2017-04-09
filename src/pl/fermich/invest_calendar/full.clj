(ns pl.fermich.invest-calendar.full
  (:require [pl.fermich.invest-calendar.events :as e]))

(defn -main [& args]
  (e/fetch-events-starting-from 2010 01 01))
