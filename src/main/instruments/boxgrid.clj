(ns main.instruments.boxgrid
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
; (println "drawing " id  x y z freq beat)
  (dotimes [u (/ freq 100)]
    (dotimes [v 10]
      (q/with-translation [(* 150 u) 0 0])
      (q/with-translation [(* (+ 150 b) u ) ( * (+ 150 b ) v) 0]
        (q/fill 255 93 53 120)
        (q/with-rotation [ freq 1 1 0]
          (q/stroke-weight 1)
          (q/stroke 25 freq 0)
          (q/box (* a peak))))))
  )


(defn render [channel]
  ;;; if channeldata
  (if (get  channel :rendering)
    (dotimes [n (count @viz)]
;;      ( println n channel)
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)
            q (get (nth @viz n) :q)
            r (get (nth @viz n) :r)
            s (get (nth @viz n) :s)
            ttl (get (nth @viz n) :ttl)
            a (get channel :a)
            b (get channel :b)
            c (get channel :c)
            d (get channel :d)

            freq (get channel :freq)
            peak (get channel :peak)
            beat (get channel :beatnumber)
            id (get channel :id)

            ]
        (draw x y z q r s ttl a b c d freq peak beat id)
        )
      ))
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing boxgrid" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
  (let [ x 0
        y 0
        z 0
        q (get channel :a)
        r (get channel :b)
        s (+ 50 (rand-int 50))
        ttl (+ 100  (int (/  (get channel :d) 2)))]
;    (println ttl)
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky false })))
  )



(defn updateviz [channel]
  ;viz objects have properties:
  ;x y z position arguments
  ;q r s arbitrary atributes, set per particle
  ;ttl  time-to-live >by default decreases per updaterun
  ;sticky bit, can make it stay, be carefull what you whish for
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :y] (fn [y] (- y (rand-int 10))))
        )
      (swap! vizcount conj n)
      )
    )
  (dotimes [n (count @vizcount)]
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  )

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz))
