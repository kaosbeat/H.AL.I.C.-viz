(ns main.instruments.channel09
  (:require [quil.core :as q]))

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [x channel]

  ;(println ("drawing"))

  (q/fill 255 30 34 13)
  (dotimes [n 10]
    ;(q/with-translation [(* n (/ (get channel :freq ) 10 )) 0 0 ])
    ;(q/fill (* 255 (q/noise n )) (* 120 (q/noise n ))  (* 25 (q/noise n )) )
                                        ;     (println "noisn = " (q/noise n ) )

    ;;(q/box (/ (get channel :freq ) 20 )  2000 (* 12000 (get channel :peak)))
    (q/ellipse 0 0 200 200 )
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

      (q/with-translation [x y z]
                                        ;(q/stroke-weight 2)

        (draw w channel))
     ; (print "renderdrawing channel 9")
      )

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (if (= ttl 0)
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
    (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false }))
  )



(defn updatech9 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
   ; (if (false? (get  (nth @viz n)  :sticky) ))
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
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )
