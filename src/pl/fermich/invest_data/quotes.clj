(ns pl.fermich.invest-data.quotes
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [pl.fermich.invest-data.db :as db]
            [pl.fermich.invest-data.format :as f]))

(def conf (props/read-properties "resources/service.properties"))

(def request-params {
   :headers {
             :user-agent (:user-agent conf)
             :content-type "text/plain; charset=utf-8"
             :accept "*/*"
             :accept-encoding "gzip, deflate, br"
             :accept-language "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4,cs;q=0.2"
             }
   :throw-exceptions false
   :follow-redirects false
  })

(defn- if-data-available [response]
  (let [status (:status response)]
    (if-not (= (:status response) 404)
      (:body response)
      nil)))

(defn- fetch-raw-quotes-by-date [url]
  (client/get url request-params))

(defn- fetch-binary-quotes-by-date [url]
  (client/get url (conj request-params {:as :byte-array}) )
  )


(defn- read-quotes-from-file [file]
  (some->> (slurp file)
           (f/parse-csv-data)
           ))

(defn load-quotes-from-csv-file [data-cnf file]
  (let [format (:format data-cnf)
        table (:table data-cnf)]
    (some->> (read-quotes-from-file file)
             (f/mark-columns format)
             (db/insert-rows table))
    ))

(defn load-csv-quotes-for-date [data-cnf date]
  (let [url (str (:stock-url conf) date ".prn")
        format (:format data-cnf)
        table (:table data-cnf)]
    (some->> (fetch-raw-quotes-by-date url)
             (if-data-available)
             (f/parse-csv-data)
             (f/mark-columns format)
             (db/insert-rows table))
    ))

(defn load-xls-quotes-for-date [data-cnf date]
  (let [url (str (:option-url conf) "date=" date)
        format (:format data-cnf)
        table (:table data-cnf)]
    (some->> (fetch-binary-quotes-by-date url)
             (if-data-available)
             (f/parse-xls-data format)
             (drop 1)
             (db/insert-rows table)
             )))
