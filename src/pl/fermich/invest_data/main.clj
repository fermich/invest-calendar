(ns pl.fermich.invest-data.main
  (:require [pl.fermich.invest-data.events :as e]
            [pl.fermich.invest-data.fx :as f]
            [pl.fermich.invest-data.option :as o]
            ))

(defn -main [& args]
  (case (take 2 args)
    ["events" "all"] (e/load-all-events)
    ["events" "diff"] (e/load-diff-events)
    ["fx" "all"] (f/load-all-quotes)
    ["fx" "diff"] (f/load-diff-quotes)
    ["option" "last-month-diff"] (o/last-month-diff)
    ["option" "load-from-dir"] (o/load-from-dir (nth args 2))
    ))
