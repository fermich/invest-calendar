(ns pl.fermich.invest-data.quotes
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.csv :as csv]))

(def conf (props/read-properties "resources/service.properties"))

(defn- if-not-404 [response]
  (let [status (:status response)]
    (if-not (= (:status response) 404)
      (:body response)
      nil)))

(defn- fetch-raw-quotes-by-date [url]
  (let [options {
                 :headers {
                           :accept "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                           :accept-encoding "gzip, deflate"
                           :user-agent (:user-agent conf)
                           }
                 :throw-exceptions false
                 :follow-redirects false
                 }
        ]
    (some->> (client/get url options)
             (if-not-404)
             (csv/parse-data)
             )
    ))

(defn- read-quotes-from-file [file]
  (some->> (slurp file)
           (csv/parse-data)
           ))

(defn load-quotes-from-file [data-cnf file]
  (let [format (:format data-cnf)
        table (:table data-cnf)]
    (some->> (read-quotes-from-file file)
             (csv/mark-columns format)
             (db/insert-rows table))
    ))

(defn load-daily-quotes [data-cnf date]
  (let [url (str (:stock-url conf) date ".prn")
        format (:format data-cnf)
        table (:table data-cnf)]
    (some->> (fetch-raw-quotes-by-date url)
             (csv/mark-columns format)
             (db/insert-rows table))
    ))
