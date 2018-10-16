(ns main.instruments.depthbox
  (:require [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def debug (atom false))
(def rendering (atom false))
(def audioChannel )


(defn toggleDebug []
  (if  (= @debug true) (reset! debug false) (reset! debug true))
  )

(defn toggleRender []
  (if  (= @rendering true) (reset! rendering false) (reset! rendering true))
  )

;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [channel]
  "main draw for this visual instrument"
  (dotimes [n (mod (get channel :beatnumber ) 8 )]


                                        ;
    (q/with-translation [ (+ 500 (* (get channel :freq) n)) 100 -1000]

      (q/with-rotation [(get channel :freq) 0 n 2]
        (q/stroke-weight 50)
        (q/stroke 134 0 34)
        (q/fill 255 259 0 20)
        (q/box ( * (get channel :freq) n)))))

  )


(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)

          a (get channel :a)
          b (get channel :b)
          c (get channel :c)
          d (get channel :d)

          f (get  channel :freq)
          p (get channel :peak)
          beat (get channel :beatnumber)
          ]
      (draw channel)
      ))
  (if @debug (do  (q/fill 255) (q/text "drawing channel1" 100 250)) )
  )


(defn add [channel]
  (let [ x 0
        y 0
        z 0
        ttl (get channel :d)]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false })))
  )



(defn updatech1 []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! viz update-in [n :y] (fn [y] (- y 1)))
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
