(ns solution3
  (:require [invoice-item]))
(use 'clojure.test)

;; SOLUTION TO PROBLEM 3

(deftest quantity-1
         (is (= 4250.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                             :invoice-item/quantity 1.0
                                             :invoice-item/discount-rate 50
                                             })))
         (is (= 850.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                               :invoice-item/quantity 1.0
                                               :invoice-item/discount-rate 90
                                               }))) ;; Needs rounding
         (is (= 8500.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                              :invoice-item/quantity 1.0
                                              })))
         (is (= 7650.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                              :invoice-item/quantity 1.0
                                              :invoice-item/discount-rate 10
                                              })))
         )

(deftest quantities-no-discount
  (is (= 85000.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                        :invoice-item/quantity 10.0
                                        })))
  (is (= 17000.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                         :invoice-item/quantity 2.0
                                         })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                         :invoice-item/quantity 0.0
                                         })))
  (is (= 850.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                     :invoice-item/quantity 0.1
                                     })))
  (is (= 85.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                       :invoice-item/quantity 0.01
                                       })))
  (is (= 8.5 (invoice-item/subtotal {:invoice-item/price 8500.0
                                      :invoice-item/quantity 0.001
                                      })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                     :invoice-item/quantity 0.00000000000000000000001
                                     }))) ;; Needs rounding
  )

(deftest unexpected-discount-rates
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                     :invoice-item/quantity 0.1
                                     :invoice-item/discount-rate 100
                                     })))
  (is (= -8500.0 (invoice-item/subtotal {:invoice-item/price 8500.0
                                     :invoice-item/quantity 1.0
                                     :invoice-item/discount-rate 200
                                     })))
  )

(deftest unexpected-prices
  (is (= 8.5e+35 (invoice-item/subtotal {:invoice-item/price 999999999999999999999999999999999999.0
                                     :invoice-item/quantity 1
                                     :invoice-item/discount-rate 15
                                     })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 0.0
                                     :invoice-item/quantity 1
                                     })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 0.0
                                     :invoice-item/quantity 1
                                     :invoice-item/discount-rate 15
                                     })))
  (is (= 85.0 (invoice-item/subtotal {:invoice-item/price "100.0"
                                     :invoice-item/quantity 1
                                     :invoice-item/discount-rate 15
                                     }))) ;; Error, maybe function needs parsing / conforming specs ?
  )


(run-tests 'solution3)