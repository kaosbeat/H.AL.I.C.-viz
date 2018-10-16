(ns main.core
  (:require [main.kaos ]))




(defn drop-nth [n coll]
  ;;keep indexed changes coll from [] to () so added (vec)
  (vec (keep-indexed #(if (not= %1 n) %2) coll))

  ;  (if (= n 0)
  ;  (vec (drop 1 coll))
  ;(if (= n (count coll))
  ;  (pop coll)
  ;  (vec (concat (subvec coll 0 n) (subvec coll (+ 1 n) (count coll))))))
  )

;(def c1 (q/color 255 255 0))
;(def c2 (q/color 255 0 0))

(def street ( atom []))
(defn addstreet [x y z ttl size serialnumber]
  (if (= 0  (count @street))
    (reset! street [])
    )
  (swap! street conj {:x x :y y :z z :ttl ttl :size size :sn serialnumber   })

  )


(def streetcount ( atom []))
(defn updatestreet [state]
  (reset! streetcount [])
  (dotimes [n (- (count @street) 0) ]
    (if (false? (< 200 (get (nth @street n)  :y)) )
      (do
;        (println (get (nth @street n) :size))
        ;(swap! street update-in [n :y]  (fn [x]  (- (get (@street n) :y ) (get (@street n) :size ))))
        (swap! street update-in [n :y]  dec))

      (swap! streetcount conj n)
      )
    )
  (dotimes [n (count @street)]
    (reset! street (drop-nth (nth @streetcount n) @street))
    )
  )


(defn renderstreets [state]
  (dotimes [n (count @street)]
    (if (= (mod2) 0)
      (if (= (get (nth @street n) :sn) 0)
        (q/fill (q/color 255 0 0))
        (q/fill (q/color 255 255 0)))
      (if (= (get (nth @street n) :sn) 0)
        (q/fill (q/color 255 255 0))
        (q/fill (q/color 255 0 0)))
      )
    (q/rect 0  (* n (get (nth @street n) :y)) 1000 (get (nth @street n) :size))
    )

  )
(defn draw [state]
  (q/background 25)
  ;
  (if (> 20 (count @street))
    (addstreet (get @roadpan :pan) 1200 0 3 200 (binary01)))

  ;(updatestreet state)

  (q/fill 234 5 25)
  (q/with-translation [500 1000 0]

    (q/with-rotation [-1.5 1 0 0]
      (q/rect 0 0 2300 230)
      (renderstreets state))
    )

  (q/camera )
                                        ;  (q/perspective 0 ( / width height) 10 10 )
  (q/perspective)
  (.sendScreen @server)



                                        ;(println " test")
  )
