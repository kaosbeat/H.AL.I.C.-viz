(ns main.instruments.piracetamsd
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))



(defn draw [x y z a b c d freq peak beat id ttl color]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)
  (dotimes [n (int 6)]
    (let [size    (* (* ttl 3.5) 1)
          spreadx (+ x (rand-int (* (* c 5) (/ b 127))))
          spready (+ y (rand-int (* (* c 5) (/ b 127))))]
      (q/with-rotation [(q/random 100) 1 0 0 ]
        (q/with-translation [(+ (* (/ size 2) ttl) 200) (+ (rand-int c) 500) 0]
          (q/fill 255 0 0 (- (* 2 d) c) )
          (q/with-rotation [(q/radians  a) 3 0 0]
            (q/box (+ (* size (/ a 8)) (rand-int c))  120 200))
                                        ;          (q/line 0 0 1000 1000 )
                                        ;         (q/rect 0 0 500 500)
          ))))
  )


(defn render [channel]
  ;;; if channeldata
  (if (get  channel :rendering)
    (dotimes [n (count @viz)]
;;      ( println n channel)
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)
            ttl (get (nth @viz n) :ttl)
            color (get (nth @viz n) :color)

            a (get (nth @viz n) :a)
            b (get (nth @viz n) :b)
            c (get (nth @viz n) :c)
            d (get (nth @viz n) :d)

            freq (get channel :freq)
            peak (get channel :peak)
            beat (get (nth @viz n) :beat)
            id (get channel :id)
            ]
        (draw x y z a b c d freq peak beat id ttl color)
        )
      ))
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing box" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
;  (println channel)
  (let [ x (rand-int 1920)
        y  (rand-int 1080)
        z 0
        a (get channel :a)
        b (get channel :b)
        c (get channel :c)
        d (get channel :d)
        beat 3
        color [(rand-int 255) (rand-int 255) (rand-int 255)]
        ttl (+ 4 (int (/ b 4)))]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :a a :b b :c c :d d :ttl ttl :color color :beat beat :sticky true })
      (swap! viz conj {:x x :y y :z z :a a :b b :c c :d d :ttl ttl :color color :beat beat :sticky false })))
  )



(defn updateviz [ channel]
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        ;(swap! viz update-in [n :z] (fn [z] (- z 10)))
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

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
