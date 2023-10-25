(ns solution2
  (:use [invoice-spec]))

;; SOLUTION TO PROBLEM 2

(def invoice-to-validate (slurp "invoice.json"))

(def keynames-map {:invoice/customer #"\"customer\":",
                   :customer/email #"\"email\":",
                   :invoice-item/price #"\"price\":",
                   :customer/name #"\"company_name\":",
                   :tax/category #"\"tax_category\":",
                   :invoice-item/quantity #"\"quantity\":",
                   :invoice/items #"\"items\":",
                   :invoice-item/sku #"\"sku\":",
                   :tax/rate #"\"tax_rate\":",
                   :invoice/issue-date #"\"issue_date\":",
                   :invoice-item/taxes #"\"taxes\":"})

(defn replace-json-keys
  [json rules]
  (reduce-kv (fn [result k v]
               (clojure.string/replace result (k rules) (str k)))
             json
             rules
             )
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
            (assoc result (k) (keyword (clojure.string/lower-case v))))
          {}
          tax)
  )
(defn json-invoice-to-map
  [json]
  ()
  )
