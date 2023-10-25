(ns solution2
  (:require [clojure.spec.alpha :as s])
  (:use [invoice-spec]))

;; SOLUTION TO PROBLEM 2

(def invoice-to-validate (slurp "invoice.json"))

;; Helper functions and bindings

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

(defn- replace-json-keys
  [json rules]
  (reduce-kv (fn [result k v]
               (clojure.string/replace result (k rules) (str k)))
             json
             rules
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

(defn map-item-taxes
  [item]
  (assoc item :invoice-item/taxes (into [] (map #(adequate-tax-values % tax-rules) (:invoice-item/taxes item))))
  ) ; returns the full item with the taxes formatted

(def invoice-conversion-rules {
                               :invoice/issue-date parse-date
                               :invoice/items #(into [] (map map-item-taxes %)) ; we need to map every tax of every item
                               })

;; Main functions

(defn convert-invoice-json-to-map
  [json rules]
  (->> (replace-json-keys json rules)
       (clojure.edn/read-string)
       (:invoice)
       )
  )

(defn convert-values
  [invoice rules]
  (reduce-kv (fn [result k rule]
               (update result k rule))
             invoice
             rules)
  )
;; Result
(def invoice (-> (convert-invoice-json-to-map invoice-to-validate keynames-map)
                 (convert-values invoice-conversion-rules)))

(println (s/valid? :invoice-spec/invoice invoice)) ; prints true
