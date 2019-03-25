(ns main.instruments.piracetambd
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))



(defn draw [x y z a b c d freq peak beat id ttl]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)
  (dotimes [n (- 20 ttl)]
    (q/with-translation [(+ x (rand-int  (* 10 n))) (+ x (rand-int  (* 10 )))  0 ]
      (q/with-rotation [ 5 (mod beat 4) 1 0]
        (q/fill a 255 0 (* 3 y))
        (q/stroke-weight 10)
        (q/stroke 25 freq 0)
        (q/box (+ 1 (* 5 ttl))))))
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

            a (get (nth @viz n) :a)
            ;a (get channel :a)
            b (get channel :b)
            c (get channel :c)
            d (get channel :d)

            freq (get channel :freq)
            peak (get channel :peak)
            beat (get @main.kaos/midibd :beat)
            id (get channel :id)
            ]
        (draw x y z a b c d freq peak beat id ttl)
        )
      ))
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing box" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
  (let [ x (rand-int 1920)
        y (rand-int 1080)
        z 0
        a (rand-int 255)
        ttl 40]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :a a :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :a a :ttl ttl :sticky false })))
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

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
