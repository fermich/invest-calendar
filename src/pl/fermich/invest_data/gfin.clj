(ns pl.fermich.invest-data.gfin
  (:require [clj-http.client :as client]
            [propertea.core :as props]
            [cheshire.core :as cheshire]
            [clojure.string :as string]
            [pl.fermich.invest-data.format :as f]))

(def conf (props/read-properties "resources/service.properties"))
(def header {33 "Id"
             0 "Ticker1"
             1 "Company name"
             3 "Ticker2"
             2 "Exchange"
             37 "Currency"
             4 "Price"
             30 "Change"
             34 "ChRGB"
             31 "Chg %"
             32 "Price history"
             10 "Earnings per share"
             11 "P/E ratio"
             12 "Price-to-book ratio"
             13 "Price-to-sales ratio"
             5 "Mkt Cap"
             35 "Enterprise value"
             14 "Dividend"
             15 "Dividend yield"
             16 "Current ratio"
             26 "Lt debt to assets"
             27 "Total debt to assets"
             28 "Lt debt to equity"
             29 "Total debt to equity"
             7 "Return on avg assets"
             8 "Return on avg equity"
             17 "Return on investment"
             18 "Beta"
             22 "Net profit margin"
             20 "Gross margin"
             6 "EBITD margin"
             21 "Operating margin"
             9 "Employees"
             24 "Revenue"
             25 "Net income"
             36 "EBITDA"
             })

(def request-params {
                     :headers {
                               :user-agent (:user-agent conf)
                               :content-type "text/plain; charset=utf-8"
                               :accept "*/*"
                               :accept-encoding "gzip, deflate, br"
                               :accept-language "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4,cs;q=0.2"
                               }
                     :throw-exceptions true
                     :follow-redirects false
                     })

(defn- call-gfin [url]
  (some-> (client/get url request-params)
          (:body)
          (cheshire/parse-string true)
          ))

(defn- get-companies [exchange]
  (let [url (-> (:gfin-exch-url conf) (format exchange))]
    (some->> (call-gfin url)
             (:searchresults)
             )))

(defn list-companies [exchange]
  (some->> (get-companies exchange)
           (map #(select-keys % [:title :ticker :id]))
           (prn)
           ))

(defn list-company-ids [exchange]
  (some->> (get-companies exchange)
           (map #(get % :id))
           ))

(defn company-data [company-ids]
  (let [csc (string/join "," company-ids)
        url (-> (:gfin-data-url conf) (format csc))
        data (call-gfin url)
        cols (-> data :company :related :cols)
        titles (map #(get header %) cols)
        rows (-> data :company :related :rows)
        ]
    (some->> (map #(:values %) rows)
             (f/mark-columns cols)
             (cheshire/generate-string)
             )
    ))
