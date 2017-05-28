(ns pl.fermich.invest-calendar.main
  (:require [pl.fermich.invest-calendar.events :as e]
            [pl.fermich.invest-calendar.fx :as f]
            [pl.fermich.invest-calendar.option :as o]
            ))

(defn -main [& args]
  (case (take 2 args)
    ["events" "all"] (e/fetch-all-events)
    ["events" "diff"] (e/fetch-diff-events)
    ["fx" "all"] (f/fetch-all-quotes)
    ["fx" "diff"] (f/fetch-diff-quotes)
    ["option" "last-month-diff"] (o/last-month-diff)
    ["option" "load-from-dir"] (o/load-from-dir (nth args 2))
    ))
