(ns pl.fermich.invest-calendar.quotes-test
  (:require [pl.fermich.invest-calendar.quotes :as q])
  (:use clojure.test))

(deftest should-mark-columns
  (testing "should mark each column"
    (let [rows [["USDJPY" "0" "20100103" "230000" 92.9700 92.9700 92.9500 92.9500 0]
                ["USDJPY" "0" "20100103" "230100" 92.9600 92.9700 92.9500 92.9700 0]]
          marked (q/mark-columns rows)]
      (are [x y] (= x y)
                 (first marked) {:ticker "USDJPY", :per "0", :dtyymmdd "20100103", :dthhmmss "230000",
                                 :open 92.97, :high 92.97, :low 92.95, :close 92.95, :vol 0}
                 (second marked) {:ticker "USDJPY", :per "0", :dtyymmdd "20100103", :dthhmmss "230100",
                                  :open 92.96, :high 92.97, :low 92.95, :close 92.97, :vol 0})
      )))

;(run-all-tests)
