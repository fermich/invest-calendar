(ns pl.fermich.invest-data.main
  (:require [pl.fermich.invest-data.events :as e]
            [pl.fermich.invest-data.fx :as f]
            [pl.fermich.invest-data.options :as o]
            [pl.fermich.invest-data.stock :as s]
            ))

(defn -main [& args]
  (case (take 2 args)
    ["events" "all"] (e/load-all-events)
    ["events" "diff"] (e/load-diff-events)

    ["fx" "all"] (f/load-all-quotes)
    ["fx" "diff"] (f/load-diff-quotes)

    ["stock" "current-month-diff"] (s/current-month-diff)
    ["stock" "load-from-dir"] (s/load-from-dir (nth args 2))

    ["options" "diff"] (o/load-diff-quotes)
    ["options" "all"] (o/load-all-quotes)
    ))
