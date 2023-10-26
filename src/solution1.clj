(ns solution1)

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

(clojure.pprint/pprint (iva-xor-ret-fuente invoice))
