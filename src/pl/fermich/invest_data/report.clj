(ns pl.fermich.invest-data.report
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [clojure.string :as str]
            [dynne.sampled-sound :as snd]
            ))

(def conf (props/read-properties "resources/service.properties"))

(defn- fetch-report-by-date [date sym]
  (let [options {
                 :body (str (:report-data conf) "&searchText=" sym "&date=" date)
                 :headers {
                           :accept "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"
                           :accept-encoding "gzip, deflate"
                           :content-type "application/x-www-form-urlencoded"
                           :user-agent (:user-agent conf)
                           :x-requested-with "XMLHttpRequest"
                           }
                 :follow-redirects false
                 }
        ]
    (Thread/sleep 5000)
    (some->> (client/post (:report-url conf) options)
             (:body)
             (str/trim-newline)
             )))

(defn- beep []
   (-> conf :report-file snd/read-sound snd/play))

(defn search-report-by-date-sym [date sym]
  (while
    (some->> (fetch-report-by-date date sym)
             (str/blank?))
    (println "not found"))
  (beep)
  )
