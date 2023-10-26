(ns invoice-item)

(defn- discount-factor [{:invoice-item/keys [discount-rate]
                         :or                {discount-rate 0}}]
  (- 1 (/ discount-rate 100.0)))

(defn subtotal
  [{:invoice-item/keys [quantity price discount-rate]
    :as                item
    :or                {discount-rate 0}}]
  (* price quantity (discount-factor item)))

