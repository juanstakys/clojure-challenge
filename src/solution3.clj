(ns solution3
  (:require [invoice-item]))
(use 'clojure.test)

;; SOLUTION TO PROBLEM 3

(deftest discounts
         (is (= 4250.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                             :invoice-item/quantity 1.0
                                             :invoice-item/discount-rate 50
                                             })))
         (is (= 850.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                               :invoice-item/quantity 1.0
                                               :invoice-item/discount-rate 90
                                               })))
         (is (= 8500.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                              :invoice-item/quantity 1.0
                                              })))
         (is (= 7650.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                              :invoice-item/quantity 1.0
                                              :invoice-item/discount-rate 10
                                              })))
         )

(run-tests 'solution3)