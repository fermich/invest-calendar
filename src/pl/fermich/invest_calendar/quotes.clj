(ns pl.fermich.invest-calendar.quotes
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [clojure.data.csv :as csv]
            [pl.fermich.invest-calendar.time :as t]))

(def conf (props/read-properties "resources/service.properties"))

(defn- get-quotes-by-date [day pair]
  (let [options {
                 :body (str "backUrl=%24backUrl&data=" day "&nazwa=" pair "&format=mst&time_period=mins&send=on")
                 :headers {
                           :accept "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                           :accept-encoding "gzip, deflate"
                           :content-type "application/x-www-form-urlencoded"
                           :referer (:fx-url conf)
                           :user-agent (:user-agent conf)
                           }
                 :follow-redirects false
                 }
        ]
    (Thread/sleep 1000)
    (some-> (client/post (:fx-url conf) options)
            (:headers)
            (:location)
            (client/get)
            (:body)
            (csv/read-csv))))

(defn fetch-quotes-starting-from [y m d]
  (some->> (t/calculate-dates y m d)
           (map #(get-quotes-by-date % "USDJPY"))))


(defn -main [& args]
  (let [out (fetch-quotes-starting-from 2017 04 01)]
    (prn out)))
