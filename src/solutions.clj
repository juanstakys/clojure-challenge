(ns solutions
  (:require [clojure.spec.alpha :as s]
            [tupelo.core :as t])
  (:use [invoice-spec]
        [clojure.set :only rename-keys]
        [clojure.test :only deftest is])
)

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

;; SOLUTION TO PROBLEM 1

(defn iva-xor-ret-fuente
  [inv]
  (->> (:invoice/items inv)
       (filter #(xor (= 19 (get-iva %)) (= 1 (get-ret_fuente %))))
       )
  )

;; SOLUTION TO PROBLEM 2
;; Apparent bug
;; while other defined specs work, with (s/get-spec ::invoice) we get nil, while, for example, (s/get-spec :invoice-item/price) returns an object

;; (println (s/valid? ::invoice invoice))

;; (s/valid? ::invoice invoice) => Execution error at solutions/eval2042 (form-init8543560321450255420.clj:1).
;; Unable to resolve spec: :solutions/invoice

;; USING (s/valid? :invoice-spec/invoice invoice) makes it work!

(def invoice-to-validate (t/json->edn (slurp "invoice.json")))

(defn json-invoice-to-map
  [input-file]
  (let [invoice (:invoice (t/json->edn (slurp input-file)))]
    invoice ;; to-solve
    )
  )

;; SOLUTION TO PROBLEM 3
