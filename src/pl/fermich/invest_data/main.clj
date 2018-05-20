(ns pl.fermich.invest-data.main
  (:require [pl.fermich.invest-data.events :as e]
            [pl.fermich.invest-data.fx :as f]
            [pl.fermich.invest-data.options :as o]
            [pl.fermich.invest-data.stock :as s]
            [pl.fermich.invest-data.rates :as r]
            [pl.fermich.invest-data.gfin :as g]
            [pl.fermich.invest-data.historical :as h]
            ))

(defn -main [& args]
  (case (take 2 args)
    ["events" "all"] (e/load-all-events)
    ["events" "diff"] (e/load-diff-events)

    ["fx" "all"] (f/load-all-quotes)
    ["fx" "diff"] (f/load-diff-quotes)

    ["commodities" "one"] (h/fetch-commodity-data "300004" "2010-04-01" "2018-04-08")
    ["commodities" "all"] (h/fetch-all-commodities-data "2010-04-01" "2018-04-08")

    ["indices" "one"] (h/fetch-index-data "WIG20" "2010-01-01" "2018-05-18")
    ["indices" "all"] (h/fetch-all-indices-data "2010-04-01" "2018-04-08")

    ["stock" "current-month-diff"] (s/current-month-diff)
    ["stock" "load-from-dir"] (s/load-from-dir (nth args 2))

    ["options" "diff"] (o/load-diff-quotes)
    ["options" "all"] (o/load-all-quotes)

    ["rates" "diff"] (r/load-diff-rates)
    ["rates" "all"] (r/load-all-rates)

    ["gfin" "companies"] (g/list-companies "WSE")
    ["gfin" "data"] (-> (g/list-company-ids "WSE") (g/company-data))
    ))
