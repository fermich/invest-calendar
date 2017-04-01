(ns pl.fermich.invest-calendar.db
  (:require [clojure.java.jdbc :as j]
            [propertea.core :as props]
            [clojure.string :as str]))

(def conf (props/read-properties "resources/service.properties"))

(def db-conf
  {:subprotocol "mysql"
   :subname (:db-subname conf)
   :user (:db-user conf)
   :password (:db-password conf)})

(defn insert-events [events]
  (j/insert-multi! db-conf (:db-table conf) events)
  events)

(defn select-last-date []
  (let [timestamp (j/query db-conf ["SELECT timestamp FROM events ORDER BY timestamp DESC limit 1"])
        [day hour] (some-> timestamp (first) (:timestamp) (str/split #" "))]
    day))

(defn delete-events-by-date [date]
  (j/query db-conf [(str "DELETE FROM events WHERE timestamp LIKE '" date "%'")]))
