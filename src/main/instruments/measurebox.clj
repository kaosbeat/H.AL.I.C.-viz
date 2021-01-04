(ns main.instruments.measurebox
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))


(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
  (let [ measure (mod beat 4)]
    (q/fill 55 a (rand-int 251))
    (q/stroke 225 0 255)
    (q/with-rotation [a 0 1 0 ]
      (q/with-translation [(q/random 1000) (q/random 1000) (q/random 100) ])
      (q/with-translation [900  (q/random 500) (q/random 100) ]
        (case measure
          0 (do (q/fill 255 255 0 120) (q/box 100 100 100 ))
          1 (do (q/fill 255 0 0) (q/box 1000 10 10))
          2 (do (q/with-rotation [a 1 1 0 ]   (q/box 2500 1000 250)))
          3 (q/box 10 100 100)
          )))

    )

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
        q 0
        r 0
        s (+ 50 (rand-int 50))
        ttl 10]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x (rand-int x) :y (rand-int y) :z (rand-int z) :q q :r r :s s :ttl ttl :sticky false })))
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
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
