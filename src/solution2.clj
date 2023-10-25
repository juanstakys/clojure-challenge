(ns solution2
  (:use [invoice-spec]))

;; SOLUTION TO PROBLEM 2

(def invoice-to-validate (slurp "invoice.json"))

(def keynames-map {:invoice #"\"invoice\":"
                   :invoice/issue-date #"\"issue_date\":",
                   :invoice/order-reference #"\"order_reference\":"
                   :invoice/payment-date #"\"payment_date\":"
                   :invoice/payment-means #"\"payment_means\":"
                   :invoice/payment-means-type #"\"payment_means_type\":"
                   :invoice/number #"\"number\":"
                   :invoice/customer #"\"customer\":"

                   :customer/name #"\"company_name\":"
                   :customer/email #"\"email\":"

                   :invoice-item/price #"\"price\":"
                   :invoice-item/quantity #"\"quantity\":"
                   :invoice/items #"\"items\":"
                   :invoice-item/sku #"\"sku\":"
                   :invoice-item/taxes #"\"taxes\":"
                   :invoice/retentions #"\"retentions\":"

                   :tax/rate #"\"tax_rate\":"
                   :tax/category #"\"tax_category\":"
                   })

(defn replace-json-keys
  [json rules]
  (reduce-kv (fn [result k v]
               (clojure.string/replace result (k rules) (str k)))
             json
             rules
             )
  )

(defn convert-invoice-json-to-map
  [json rules]
  (->> (replace-json-keys json rules)
       (clojure.edn/read-string)
       (:invoice)
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
