(ns solution2
  (:require [clojure.spec.alpha :as s])
  (:use [invoice-spec]))

;; SOLUTION TO PROBLEM 2

;; Helper functions and bindings

(def keywords-map {:invoice #"\"invoice\":"
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
                   }) ; Associates the desired keyword with the corresponding regex pattern in json (rules for formatting the keys)

(defn- replace-json-keys
  [json rules]
  (reduce-kv (fn [result k v]
               (clojure.string/replace result (k rules) (str k))) ; Replaces every instance regex pattern of rules with the stringified keyword in the JSON
             json
             rules
             )
  )

(def tax-rules {
                :tax/category #(keyword (clojure.string/lower-case %))
                :tax/rate double
                }) ; functions for formatting the values of taxes according to the key

(defn- adequate-tax-values
  [tax rules]
  (reduce-kv (fn [result k rule]
               (update result k rule) ; Apply the corresponding function to the value in the specified key according to the specified rules map
            )
          tax
          rules)
  )

(defn- parse-date
  ([date] (parse-date date "dd/MM/yyyy")) ; default format for invoices
  ([date fmt]
  (.parse
    (java.text.SimpleDateFormat. fmt) ; Parses a date (string) in the specified format to inst
    date))
  )

(defn- map-item-taxes
  [item]
  (assoc item :invoice-item/taxes (into [] (map #(adequate-tax-values % tax-rules) (:invoice-item/taxes item))))
  ) ; returns the full item with the taxes formatted

(def invoice-conversion-rules {
                               :invoice/issue-date parse-date
                               :invoice/items #(into [] (map map-item-taxes %)) ; we need to map every tax of every item
                               })

;; Main functions

(defn convert-invoice-json-to-map
  "Takes a JSON string and a rules map that relates edn keywords with JSON keys regex. Then converts the JSON according to those rules and takes the map of the key :invoice"
  [json rules]
  (->> (replace-json-keys json rules)
       (clojure.edn/read-string)
       (:invoice)
       )
  )

(defn convert-values
  "Converts the values of an invoice map according to the functions for every key specified in rules"
  [invoice rules]
  (reduce-kv (fn [result k rule]
               (update result k rule))
             invoice
             rules)
  )

; Conversion function
(defn json-file-to-valid-invoice [filename]
  (-> (slurp filename)
      (convert-invoice-json-to-map keywords-map)
      (convert-values invoice-conversion-rules)
      ))

;; Result
(def invoice (json-file-to-valid-invoice "invoice.json"))

(println (s/valid? :invoice-spec/invoice invoice)) ; prints true
