(ns main.instruments.channel04
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [offset ttl channel]

  ;;(dotimes [n (mod (get channel :beatnumber ) 8 )])

  (q/with-translation [(+ 500 (* 1 (+ ttl offset))) 800 0]
;;    (q/with-rotation [(get channel :beatnumber) 1 n 546 ])
    (q/with-rotation [(* 4 offset) 1 (* 0.4  offset) 0]
      (q/fill (* 255  (/ ttl 500)) )
      (q/stroke 1 (q/random  123) 290 100)
      (dotimes [y 10]
        (q/box 4 (* y 10) 10 ))))

  )

(defn draw [offset ttl channel]
  (q/with-translation [(* 5 10) 0 0 ]
    (q/box 400))
  )


(def viz (atom []))
(def vizcount (atom 0))




(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          offset (* 10  (get (nth @viz n) :offset))
          ttl (get (nth @viz n) :ttl)
          w  100
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (q/stroke-weight 2)
        (draw offset ttl channel)))

    )
  ;;(q/rect 200 200 200 200)
  )



(defn add [x y z offset ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
    (swap! viz conj {:x x :y y :z z :offset offset :ttl ttl :sticky false }))
  )



(defn updatech4 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
;        (swap! viz update-in [n :y] (fn [y] (- y 10)))
        (swap! viz update-in [n :offset] (fn [offset] (* -0.5 offset)))
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
