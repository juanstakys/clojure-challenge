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
(def tax-rules {
                :tax/category #(keyword (clojure.string/lower-case %))
                :tax/rate double
                })
(defn adequate-tax-values
  [tax rules]
  (reduce-kv (fn [result k rule]
               (update result k rule)
            )
          tax
          rules)
  )

(defn parse-date
  "Parses a date (string) in the specified format to inst"
  ([date] (parse-date date "dd/MM/yyyy")) ; default format for invoices
  ([date fmt]
  (.parse
    (java.text.SimpleDateFormat. fmt)
    date))
  )

(defn json-invoice-to-map
  [json]
  ()
  )
