(ns problem1
  (:require [clojure.spec.alpha :as s])
  (:use [invoice-spec]))

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn get-iva
  [item]
  (->> (:taxable/taxes item)
       (filter #(= (:tax/category %) :iva))
       (map :tax/rate))
  )

(defn get-ret_fuente
  [item]
  (->> (:retentionable/retentions item)
       (filter #(= (:retention/category %) :ret_fuente))
       (map :retention/rate))
  )

(defn iva-xor-ret-fuente
  [inv]
  (->> (:invoice/items inv)
       (filter )
       )
  )