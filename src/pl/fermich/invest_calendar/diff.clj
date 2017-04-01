(ns pl.fermich.invest-calendar.diff
  (:require [clojure.string :as str]
            [pl.fermich.invest-calendar.db :as db]
            [pl.fermich.invest-calendar.parser :as p]))

(defn -main [& args]
  (let [last-event-date (db/select-last-date)
        date-splitted (str/split last-event-date #"-")
        [y m d] (map #(Integer/parseInt %) date-splitted)]
    (println "Last event: " last-event-date)
    (db/delete-events-by-date last-event-date)
    (p/fetch-events-starting-from y m d)))
