(ns main.instruments.chainbox
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom []))
(def rendering (atom false))
(def chain ( atom [[]]))


(defn addLayer [size n]
  (if (< (count (last @chain)) n)
    (do    ;;(println "thisworks")
           (swap! chain assoc (-  (count @chain) 1) (conj (last @chain) (rand-int size))))
    (do
      (swap! chain conj [] )
      (swap! chain assoc (-  (count @chain) 1) (conj (last @chain) (rand-int size)))
      )
    )
  (if (> (count @chain) 20)
;    (reset! chain (rest @chain))
                                        ; (println "weewe")
    (+ 1 1)
    )
  )






(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
                                        ;  (println "drawing")
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
    )
  (q/stroke-weight 1)
  (q/with-rotation [(main.kaos/camrot)  0 0 1])
  (dotimes [n (count @chain)]
    (q/stroke (* 255 n) 0 0)
                                        ;(println n)
    (let [ylist (nth @chain n)]
      (q/with-translation [-200 -200 (* -100 (count @chain))]
        (dotimes [m (count ylist)]
          (let [y (nth ylist m)
                xoffset 0]
                                        ;            (q/line (+ xoffset (* 100 m)) (+ 500  y) (* n 100) (+ xoffset  (* 100 m)) 500 (* n 100))
                                        ; (case)
            (q/fill 255 0 0 (- 255 (* n 5)) )
            ;;              (q/no-fill)
            (q/with-rotation [(* (main.kaos/tr) 0) 0 1 0])
            (q/with-translation [(+ xoffset (* 200 m)) (+ -200 (* 3  y)) (* n 100)]
              (q/box s )))
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
  )


)
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing boxgrid" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )

(defn add [channel]
  ;;(println "adding")
  (addLayer 500 4)
  (let [ x 0
        y 0
        z 0
        q 0
        r 0
        s (get channel :peak)
        ttl 100]
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
    (reset! viz  (drop-nth (nth @vizcount n) @viz))))


(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )



(defn resetchain []
  (reset! chain [[]]))
