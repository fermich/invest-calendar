(defproject invest-calendar "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.7.0"]
                 [clj-http "2.3.0"]
                 [clj-webdriver "0.6.0"]
                 [propertea "1.2.3"]
                 [clj-time "0.13.0"]
                 [enlive "1.1.6"]
                 [org.mariadb.jdbc/mariadb-java-client "1.5.9"]
                 [org.clojure/java.jdbc "0.6.1"]]
  :resource-paths ["resources"]

  :profiles {:full {:main pl.fermich.invest-calendar.full}
             :diff {:main pl.fermich.invest-calendar.diff}}
  )
