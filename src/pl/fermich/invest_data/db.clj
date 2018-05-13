(ns pl.fermich.invest-data.db
  (:require [clojure.java.jdbc :as j]
            [propertea.core :as props]
            [clojure.string :as str]))

(def conf (props/read-properties "resources/service.properties"))

(def db-conf
  {:subprotocol "mysql"
   :subname (:db-subname conf)
   :user (:db-user conf)
   :password (:db-password conf)})

(defn insert-rows [table rows]
  (j/insert-multi! db-conf table rows)
  rows)

(defn select-commodity-by-sml [sml]
  (some->> (j/query db-conf [(str "SELECT curr, sml, header FROM commodity_codes where sml=" sml)])
           (first)
           ))

(defn all-commodities-sml []
  (some->> (j/query db-conf ["SELECT distinct sml FROM commodity_codes"])
           ))

(defn select-last-event-date []
  (let [timestamp (j/query db-conf ["SELECT timestamp FROM events ORDER BY timestamp DESC limit 1"])
        [day hour] (some-> timestamp (first) (:timestamp) (str/split #" "))]
    day))

(defn- select-last-quotes-date [db-name]
  (some->> (j/query db-conf [ (str "SELECT dtyymmdd FROM " db-name " ORDER BY dtyymmdd DESC limit 1")])
           (first)
           (:dtyymmdd)))

(defn select-last-fx-quotes-date []
  (select-last-quotes-date "fx_quotes"))

(defn select-last-option-quotes-date []
  (select-last-quotes-date "option_quotes"))

(defn select-last-stock-quotes-date []
  (select-last-quotes-date "stock_quotes"))

(defn select-last-rates-date []
  (select-last-quotes-date "rates"))


(defn delete-events-by-date [date]
  (j/query db-conf [(str "DELETE FROM events WHERE timestamp LIKE '" date "%'")]))

(defn- delete-quotes-by-date [table date]
  (j/query db-conf [(str "DELETE FROM " table " WHERE dtyymmdd = '" date "'")]))

(defn delete-fx-quotes-by-date [date]
  (delete-quotes-by-date "fx_quotes" date))

(defn delete-option-quotes-by-date [date]
  (delete-quotes-by-date "option_quotes" date))

(defn delete-stock-quotes-by-date [date]
  (delete-quotes-by-date "stock_quotes" date))

(defn delete-rates-by-date [date]
  (delete-quotes-by-date "rates" date))
