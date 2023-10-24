(ns solutions
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [tupelo.core :as t])
  (:use [invoice-spec]
        [clojure.set :only [rename-keys]]
        )
)

;; SOLUTION TO PROBLEM 1

(def invoice (clojure.edn/read-string (slurp "invoice.edn")))

(defn get-iva
  [item]
  (->> (:taxable/taxes item)
       (filter #(= (:tax/category %) :iva))
       (map :tax/rate)
       (first))
  )

(defn get-ret_fuente
  [item]
  (->> (:retentionable/retentions item)
       (filter #(= (:retention/category %) :ret_fuente))
       (map :retention/rate)
       (first))
  )

(defn xor
  [condition1 condition2]
  (or
    (and condition1 (not condition2))
    (and condition2 (not condition1))
    ))

(defn iva-xor-ret-fuente
  [inv]
  (->> (:invoice/items inv)
       (filter #(xor (= 19 (get-iva %)) (= 1 (get-ret_fuente %))))
       )
  )

;; SOLUTION TO PROBLEM 2

(def invoice-to-validate (:invoice (t/json->edn (slurp "invoice.json"))))

(def keynames-map {
                   :tax_category :category
                   :tax_rate :rate
                   })
(defn appropiate-keywords
  [map-input rules]
  (reduce-kv (fn [result k v]
               (assoc result (k rules) v))
             {} map-input
             )
  )

(defn qualify-keywords
  [map-input namesp]
  (reduce-kv (fn [result k v]
               (assoc result (keyword (name namesp) (name k)) v))
             {} map-input
             )
  )


(defn adequate-tax-format
  [tax]
  (reduce (fn [result  [k v]]
            (assoc result (k) (keyword (string/lower-case v))))
          {}
          tax)
  )
(defn json-invoice-to-map
  [input-file]
  (let [invoice (:invoice (t/json->edn (slurp input-file)))]
    invoice ;; to-solve
    )
  )

;; SOLUTION TO PROBLEM 3