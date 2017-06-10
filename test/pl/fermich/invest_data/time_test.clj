(ns pl.fermich.invest-data.time-test
  (:require [clj-time.core :as ct]
            [pl.fermich.invest-data.time :as t])
  (:use clojure.test))

(deftest should-return-dates-sequence
  (testing "should return dates day by day"
    (let [from (ct/date-time 2010 01 01)
          to (ct/date-time 2010 01 03)
          ds (t/dates-sequence from to "yyyy-MM-dd")]
      (is (= ["2010-01-01" "2010-01-02"] ds))
      )))

(deftest should-return-first-day-of-month
  (testing "should return first day of the month"
    (let [date "20151215"
          first (t/first-day-of-the-month date)]
      (is (= [2015 12 01] first))
      )))

;(run-all-tests)
