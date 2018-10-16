(ns main.util
)

(defn drop-nth [n coll]
  ;;keep indexed changes coll from [] to () so added (vec)
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )


