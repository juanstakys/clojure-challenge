(ns solution2
  (:use [invoice-spec]))

;; SOLUTION TO PROBLEM 2

(def invoice-to-validate (slurp "invoice.json"))

(def keynames-map {:invoice/customer #"\scustomer:",
                   :customer/email #"\semail:",
                   :invoice-item/price #"\sprice:",
                   :customer/name #"\scompany_name:",
                   :tax/category #"\stax_category:",
                   :invoice-item/quantity #"\squantity:",
                   :invoice/items #"\sitems:",
                   :invoice-item/sku #"\ssku:",
                   :tax/rate #"\stax_rate:",
                   :invoice/issue-date #"\sissue_date:",
                   :invoice-item/taxes #"\staxes:"})

(defn replace-json-keys
  [json rules]
  (string/replace json )
  )

(defn appropriate-keywords
  [map-input rules]
  (reduce-kv (fn [result k v]
               (assoc result (k rules) v))
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
  [json]
  ()
  )
