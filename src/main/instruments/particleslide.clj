(ns main.instruments.particleslide
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [startxy stopxy vec step steps beat ttl]
  "main draw for this visual instrument"
  (q/stroke-weight 5)
  (q/stroke 255 255 255 ttl)
  (q/line startxy stopxy)
  (dotimes [n steps]
    (let [x1 (* step (get vec 0))
          y1 (* step (get vec 1))
          x2 (+ x1 (rand-int 1000))
          y2 (+ y1 (rand-int 1000))]
                                        ;(q/line x1 y1 x2 y2)
      (q/fill 245 34 20 100)
      (q/with-translation [ x1 x2 0]
        (dotimes [n (rand-int 50)]
          (q/no-stroke)
          (q/box (* n 10 )))
        )
      )
    )



  )




(def viz (atom []))
(def vizcount (atom 0))

(def points [ [ 0 100]   [ 250 250] [ 400 50] [ 50 400] [200 300] ] )




(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [startxy (get (nth @viz n) :startxy)
          stopxy (get (nth @viz n) :stopxy)
          interval (get (nth @viz n) :interval)
          step (get (nth @viz n) :step)
          steps (get (nth @viz n) :steps)
          vec [ (/  (- (get stopxy 0) (get startxy 0)) steps ) (/  (- (get stopxy 1) (get startxy 1)) steps)]
          ttl (get (nth @viz n) :ttl)
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          beat (mod (get channel :beatnumber) 4)
          ]

      (q/with-translation [0 0 0]

        (draw startxy stopxy vec step steps beat ttl )
        (q/stroke-weight 10)
     (q/line 0 1000 1000 0)
        ))

    )

  )


(defn add [startxy, stopxy, interval,steps, ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:startxy startxy :stopxy stopxy :interval interval :step 0 :steps steps :ttl ttl :sticky true })
    (swap! viz conj {:startxy startxy :stopxy stopxy :interval interval :step 0 :steps steps :ttl ttl :sticky false }))
  )



(defn psupdate []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :step] inc)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        ;(swap! viz update-in [n :y] (fn [y] (- y 1)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))

  )
