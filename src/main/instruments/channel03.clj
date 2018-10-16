(ns main.instruments.channel03
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )
(def viz (atom []))
(def vizcount (atom 0))


(defn draw [freq channel]
  "main draw for this visual instrument"

  ;(q/random-seed (get channel :beatnumber))
;  (q/fill (q/random 255) (q/random 155) (q/random 255))
;;  (q/box 10)


;  (println  (* 10000 (get channel :peak)))
                                        ; (dotimes [e (* 1000 (get channel :peak))])
  (q/fill 255 255 0)
  ;; (q/with-translation [500 0 0]
  ;;   (q/with-rotation [ (q/random 100) 0 0 1]
  ;;     (q/rect 0 0 230 20 )
  ;;     ))

  (q/with-translation [(rand-int 0) (rand-int 0) (rand-int 0)])

  (q/with-translation [0 -400 20]
    (dotimes [x 10]
      (dotimes [y 10]
        (let [v  (* (* 10 freq) x)
              w  (* 10 y)]
          (q/stroke-weight (rand-int 20))
          (q/line v w -10 v w 100))

        )))


)

(defn draw [freq channel]

  (q/with-translation [100 400 100]
    (dotimes [z 10]
      (let [amp (rand-int 100)
            phase 100
            n 100
            zz (* z -100)]

        (dotimes [n 10]

          (q/line (* phase n) 0 zz (+ (* phase n) (/ phase 2)) amp zz)
          (q/line (+ (* phase n) (/ phase 2)) amp zz (* phase (+ n 1)) 0 zz))))))






(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          w  100
          freq (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (q/stroke-weight 2)
        (draw freq channel)))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false }))
  )



(defn updatech3 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])

  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do

        (swap! viz update-in [n :ttl] dec)
     ; (swap! viz update-in [n :z] (fn [x] (rand-int 60)))
        ;(swap! viz update-in [n :y] (fn [y] (* 1 (random-int 1000 ) )))

        )
      ;else mark pill for deletion
      ;
;      (if (false? (get (get @viz n):sticky ))(swap! vizcount conj n))


                                        ;(reset! @pills [0 9 0])
      )

    (dotimes [n (count @vizcount)]
                                        ;    (println " really dropping stuff")
      (reset! viz  (drop-nth (nth @vizcount n) @viz))))
  ;(println @pills)
  )


;(update)
