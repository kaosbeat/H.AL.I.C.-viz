(ns main.instruments.channel02
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [x channel]
  "main draw for this visual instrument"
  (let [ p (get channel :peak)
        f (get channel :freq)]
    (q/with-rotation [ p 0 (rand-int 2) 0]
     ; (println p)
      (dotimes [n (/ p 50)]
        (q/with-translation [(* 10 p) (* (mod  (get channel :beatnumber) 8 ) 100) -100]
          (q/stroke 34 (/ f 2) 244 233)
          (q/stroke-weight (/ f 200))
          (q/fill 23 (q/random 255) 230 10)
          (q/with-rotation [ (q/random f)  0 0 1  ]
            (q/box 110 (* (q/noise n) 100) (* (get  channel :peak) 200 ))
            )
  /        (q/ellipse 30 30 20 20)
          )
        )))
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



(defn updatech2 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! viz update-in [n :y] (fn [y] (- y 10)))
        )
      ;else mark pill for deletion unless sticky
      (if (false? (get (get @viz n):sticky ))
        (swap! vizcount conj n)
        )
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )
