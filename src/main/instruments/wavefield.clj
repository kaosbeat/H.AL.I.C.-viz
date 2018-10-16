(ns main.instruments.wavefields
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [x]
  "main draw for this visual instrument"
  (q/box x)
  )

(defn wavefield [posy width heigth]
  (let [ w (/ width 12)
         h (/ heigth 20)]
                                        ;l;  (q/stroke-weight 10) ;
       (q/begin-shape)
       (dotimes [n 8]
         (case n
           0 (q/vertex 0 0)
           1 (q/vertex (* w 2) (* h (q/random 10 20)))
           2 (q/vertex (* w 4) (* h (q/random 0 10)))
           3 (q/vertex (+ (-  (q/random w) (/ w) 2) (* w 5)) (* h (q/random 0 5)))
           4 (q/vertex (+ (-  (q/random w) (/ w) 2) (* w 6)) (* h (q/random 0 5)))
           5 (q/vertex (* w 7) (* h (q/random 0 10)))
           6 (q/vertex (* w 9) (* h (q/random 10 20)))
           7 (q/vertex (* w 11) 0)
           )
;         @transfield
         )
       (q/end-shape)
       )
  )



(def viz (atom []))
(def vizcount (atom 0))




(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          w  100
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (q/stroke-weight 2)
        (draw w h hfr vfr )))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
     (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false }))
  )



(defn update []
                                        ; for some reason not all pills are deleted

  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (get (get @viz n) :sticky))
      (if (false? (= 0 (get (get @viz n) :ttl)))
                                        ;decrease TTL in pill if ttl > 0
        (do
          (swap! viz update-in [n :ttl] dec)
                                        ;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
          (swap! viz update-in [n :y] (fn [y] (- y 10)))
          )
                                        ;else mark pill for deletion
        (swap! vizcount conj n)
                                        ;(reset! @pills [0 9 0])
        ))
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )

(defn removeall []

  )

(add 500 200 -500 120)
(update)
