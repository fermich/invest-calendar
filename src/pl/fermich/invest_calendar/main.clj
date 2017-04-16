(ns pl.fermich.invest-calendar.main
  (:require [pl.fermich.invest-calendar.events :as e]
            [pl.fermich.invest-calendar.quotes :as q]))

(defn -main [& args]
  (case args
    ["events" "all"] (e/fetch-all-events)
    ["events" "diff"] (e/fetch-diff-events)
    ["quotes" "all"] (q/fetch-all-quotes)
    ["quotes" "diff"] (q/fetch-diff-quotes)
    ))
