(ns main.instruments.channel06
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [x channel]
  "main draw for this visual instrument"


                                        ; (dotimes [n (mod (get channel :beatnumber ) 8 )])

  ;;(dotimes [n 10])
    ;; (q/with-rotation [ (* n 10) 0 1 0])
    ;(q/with-translation [(* (get channel :freq) n) 100 0])
    ;;(q/fill ( * 5500 (get channel :peak)) 152  450 )


  (q/fill 25 (rand-int 230) 234 25 )
  (dotimes [n 40]
    (q/with-translation [ (rand-int 1900) (rand-int 1400) (* n 3)]
      (q/ellipse 0 0  200 200)))

                                        ;
;(q/line 100 300 299 100)
      )

(def viz (atom []))
(def vizcount (atom 0))


(defn draw [x channel]

  (q/with-translation [ 1000 100 -500]
    (dotimes [x1 10]
      (dotimes [y1 10]
        (dotimes [z1 10]
          (q/with-rotation [ 30 1 1 0 ]
            (q/with-translation [0 0 0]
              (let [k 100]
                (q/line (* k x1) (* k  y1) (* k z1)   (* k x1) (* k  (+ 1 y1 ))   (* k z1)  ))))

          ))))

  )




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

      (q/with-translation [ 0 500 0]
        (q/stroke-weight 2)
        (draw w channel)))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false }))
  )



(defn updatech6
  []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! viz update-in [n :y] (fn [y] (- y 50)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )

(add 500 200 -500 120)
(updatech6)
