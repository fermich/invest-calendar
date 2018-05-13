(ns pl.fermich.invest-data.time-test
  (:require [clj-time.core :as ct]
            [pl.fermich.invest-data.time :as t])
  (:use clojure.test))

(deftest should-return-days-sequence
  (testing "should return dates day by day"
    (let [from (ct/date-time 2010 01 01)
          to (ct/date-time 2010 01 03)
          ds (t/days-sequence from to "yyyy-MM-dd")]
      (is (= ["2010-01-01" "2010-01-02"] ds))
      )))

(deftest should-return-months-sequence
  (testing "should return dates month by month"
    (let [from (ct/date-time 2010 01 01)
          to (ct/date-time 2010 03 01)
          ds (t/months-sequence from to "yyyy-MM-dd")]
      (is (= ["2010-01-01" "2010-02-01"] ds))
      )))

(deftest should-return-first-day-of-month
  (testing "should return first day of the month"
    (let [date "2015-12-15"
          first (t/first-day-of-the-month date)]
      (is (= "2015-12-01" first))
      )))

(deftest should-return-american-date
  (testing "should format to american date"
    (let [date "2015-12-15"
          american (t/to-american-date date)]
      (is (= "12/15/2015" american))
      )))

;(run-all-tests)
