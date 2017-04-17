(ns pl.fermich.invest-calendar.time-test
  (:require [clj-time.core :as ct]
            [pl.fermich.invest-calendar.time :as t])
  (:use clojure.test))

(deftest should-return-dates-sequence
  (testing "should return dates day by day"
    (let [from (ct/date-time 2010 01 01)
          to (ct/date-time 2010 01 03)
          ds (t/dates-sequence from to)]
      (is (= ["2010-01-01" "2010-01-02"] ds))
      )))

;(run-all-tests)
