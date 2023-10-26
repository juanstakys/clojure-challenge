(ns solution3
  (:require [invoice-item]))
(use 'clojure.test)

;; SOLUTION TO PROBLEM 3

;; 'expected' and 'actual' will be swapped in the output for the sake of readability. Otherwise, the expected result should be defined after the item map (not as readable)
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
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 0.0
                                         :invoice-item/quantity 999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.9
                                         })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 0.0
                                     :invoice-item/quantity 1.0E200
                                     })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999.9
                                     :invoice-item/quantity 0.0
                                     })))
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 1E308
                                     :invoice-item/quantity 0.0
                                     }))) ; Returns 0.0
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 1E309
                                     :invoice-item/quantity 0.0
                                     }))) ; Returns NaN (maybe we should define maximum values?)
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
  (is (= 100.0 (invoice-item/subtotal {:invoice-item/price 100.0
                                         :invoice-item/quantity 1.0
                                         :invoice-item/discount-rate 1E-308 ;; practically 0.0
                                         })))
  (is (= 100.0 (invoice-item/subtotal {:invoice-item/price 100.0
                                       :invoice-item/quantity 1.0
                                       :invoice-item/discount-rate 1E-9999 ;; practically 0.0
                                       })))
  (is (= 100.0 (invoice-item/subtotal {:invoice-item/price -100.0
                                         :invoice-item/quantity 1.0
                                         :invoice-item/discount-rate 200
                                         })))
  (is (= 90.0 (invoice-item/subtotal {:invoice-item/price 100.0
                                       :invoice-item/quantity 1.0
                                       :invoice-item/discount-rate 10.0 ;; double type discount rate
                                       })))
  (is (= 90.0 (invoice-item/subtotal {:invoice-item/price 100.0
                                      :invoice-item/quantity 1.0
                                      :invoice-item/discount-rate 10.0000000000001 ;; needs rounding
                                      })))
  (is (= 80.0 (invoice-item/subtotal {:invoice-item/price 100.0
                                      :invoice-item/quantity 1.0
                                      :invoice-item/discount-rate 20.0000000000000000000000000000000000000001 ;; rounds properly
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
  (is (= 0.0 (invoice-item/subtotal {:invoice-item/price 0
                                     :invoice-item/quantity 0
                                     :invoice-item/discount-rate 0
                                     })))
  )

(deftest unexpected-inputs
  (is (thrown? Exception (invoice-item/subtotal {})))
  (is (thrown? Exception (invoice-item/subtotal {:price "text"
                                                 :quantity 1
                                                 :discount-rate 15})))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price 8500.0
                                                 :invoice-item/quantity "1.0"
                                                 :invoice-item/discount-rate 50
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price 8500.0
                                                 :invoice-item/quantity 1.0
                                                 :invoice-item/discount-rate "50"
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price "100.0"
                                                 :invoice-item/quantity 1
                                                 :invoice-item/discount-rate 15
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price "text"
                                                 :invoice-item/quantity 1
                                                 :invoice-item/discount-rate 15
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price 1.0
                                                 :invoice-item/quantity 1.0
                                                 :invoice-item/discount-rate true
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price 1.0
                                                 :invoice-item/quantity true
                                                 :invoice-item/discount-rate 1.0
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price true
                                                 :invoice-item/quantity 1.0
                                                 :invoice-item/discount-rate 1.0
                                                 })))
  (is (thrown? Exception (invoice-item/subtotal {:invoice-item/price true
                                                 :invoice-item/quantity true
                                                 :invoice-item/discount-rate true
                                                 })))
  )

(run-tests 'solution3)