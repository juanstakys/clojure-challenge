(ns solution3
  (:require [invoice-item])
  )

;; SOLUTION TO PROBLEM 3

(println (invoice-item/subtotal {:invoice-item/price 8500.0
                                 :invoice-item/quantity 1.0
                                 :invoice-item/discount-rate 50
                                 :invoice-item/sku "SUPER-X"
                                 :invoice-item/taxes[{:tax/category :iva
                                                      :tax/rate 19.0},
                                                     {:tax/category :iva
                                                      :tax/rate 20.0},
                                                     {:tax/category :iva
                                                      :tax/rate 21.0}
                                                     ]
                                 }))