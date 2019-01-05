(ns main.instruments.superstack
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))
(def stack ( atom [[]]))


(defn addLayer [size n]
  (if (< (count (last @stack)) n)
    (do    ;;(println "thisworks")
           (swap! stack assoc (-  (count @stack) 1) (conj (last @stack) (rand-int size))))
    (do
      (swap! stack conj [] )
      (swap! stack assoc (-  (count @stack) 1) (conj (last @stack) (rand-int size)))
      )
    )
  (if (> (count @stack) 20)
;    (reset! chain (rest @chain))
    (println "weewe")
    )
  )






(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"

;  (println (addLayer 100 4))
  (let [ measure (mod beat 4)]
    ;(println peak)
    (q/stroke-weight (/ peak 10))
    (q/stroke 225 255 255)
    ;; (q/with-translation [(q/random 1000) (q/random 1000) (q/random 100) ]
    ;;   (case measure
    ;;     0 (q/box 10 10 10 )
    ;;     1 (q/box 1000 100 10)
    ;;     2 (q/box 10 1000 10)
    ;;     3 (q/box 10 10 1000)
    ;;     ))
    (q/fill r peak 0)
    (q/with-translation [(+  500 x)  (+ 300 y) -1200]
      (q/box (* r 3) (/ freq 10) (* 20 peak) ))
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
  )
)
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing boxgrid" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )

(defn add [channel]
  ;;(println "adding")
;;  (addLayer 500 4)
  (let [ x (* 300 (mod (get channel :beatnumber) 4))
        y 1000
        z 0
        q (+ 20 (rand-int 50))
        r (rand-int 255)
        s (get channel :peak)
        ttl 100
        beat (get channel :beatnumber)]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :beat beat :sticky false })))
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
;        (swap! viz update-in [n :y] (fn [y] (- y (rand-int 10))) )
                                        ; mod teh beat to the vizbeat!!!
        (if (= (mod (get channel :beatnumber) 4) (mod  (get (get @viz n) :beat) 4))
           ; (= (mod (get channel :beatnumber) 4) (mod  (get viz :beat) 4))
         (swap! viz update-in [n :y] (fn [y] (- y (rand-int 10)))  )
;
        ; (+ 1 1)
         ))



      (swap! vizcount conj n))
      )


  (dotimes [n (count @vizcount)]
    (reset! viz  (drop-nth (nth @vizcount n) @viz))))


(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )



(defn resetstack []
  (reset! stack [[]]))
