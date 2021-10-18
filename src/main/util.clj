(ns main.util
)

(defn drop-nth [n coll]
  ;;keep indexed changes coll from [] to () so added (vec)
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )


(defn update-each
  "updates each keyword in keys {:a 1 :c 3}  on assoc struct map {:a 0 :b 1 :c 2}"
  [map keys]
  (swap! map (fn [old new] (merge old new)) keys))
