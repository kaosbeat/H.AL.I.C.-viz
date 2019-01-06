(ns main.instruments.phaselines
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
  (q/stroke 32 23 3)
  (q/stroke-weight 1)
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
;          (q/stroke 255)
          (q/stroke-weight (rand-int 20))
          (q/line v w -10 v w 100))

        )))


)

(defn draw [x y z q r s ttl a b c d freq peak beat id]
 ; (background 255)
  (q/with-translation [100 400 100]
    (dotimes [z 10]
      (let [amp peak
            phase peak
            n 10
            zz (* z -10)]

        (dotimes [n 100]
          (q/with-rotation [ (* n 0.2) 0 1 0]
            (q/stroke-weight d)
                                        ;          (q/line (* phase n) 0 zz (+ (* phase n) (/ phase 2)) amp zz)
            (q/line (+ (* phase n) (/ phase 2)) (* 10 amp) zz (* phase (+ n 1)) -1000 zz)))))))



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
        ;(swap! viz update-in [n :y] (fn [y] (- y (rand-int 10))))
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
