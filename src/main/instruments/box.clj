(ns main.instruments.box
  (:require [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def debug (atom false))
(def rendering (atom false))
(def audioChannel (atom {} ))


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

(defn draw [x y z a b c d freq peak beat id]
  "main draw for this visual instrument"
 ; (println "drawing " id  x y z freq beat)
  (q/with-translation [(* id 500) y z]
                       (q/fill 255)
                       (q/stroke 255 freq 0)
                       (q/box freq))
  )


(defn render [channel]
  ;;; if channeldata
  (if (get  channel :rendering)
    (dotimes [n (count @viz)]
;;      ( println n channel)
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)

            a (get channel :a)
            b (get channel :b)
            c (get channel :c)
            d (get channel :d)

            freq (get channel :freq)
            peak (get channel :peak)
            beat (get channel :beatnumber)
            id (get channel :id)
            ]
        (draw x y z a b c d freq peak beat id)
        )
      ))
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing box" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
  (let [ x 500
        y 400
        z 0
        ttl 10]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false })))
  )



(defn updateviz []
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
