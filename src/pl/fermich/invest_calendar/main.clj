(ns pl.fermich.invest-calendar.main
  (:require [pl.fermich.invest-calendar.events :as e]
            [pl.fermich.invest-calendar.fx :as f]))

(defn -main [& args]
  (case args
    ["events" "all"] (e/fetch-all-events)
    ["events" "diff"] (e/fetch-diff-events)
    ["fx" "all"] (f/fetch-all-quotes)
    ["fx" "diff"] (f/fetch-diff-quotes)
    ))
