(defproject invest-data-loader "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [cheshire "5.7.0"]
                 [clj-http "2.3.0"]
                 [clj-webdriver "0.6.0"]
                 [propertea "1.2.3"]
                 [clj-time "0.13.0"]
                 [enlive "1.1.6"]
                 [mysql/mysql-connector-java "8.0.22"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.craigandera/dynne "0.4.1"]
                 [dk.ative/docjure "1.11.0"]
                 [org.clojure/data.zip "0.1.3"]
                 ]
  :resource-paths ["resources"]
  :main pl.fermich.invest-data.main
  )
