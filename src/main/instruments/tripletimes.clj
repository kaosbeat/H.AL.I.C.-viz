(ns main.instruments.tripletimes
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))



(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"

  (dotimes [n (mod beat 8 )]

                                        ; (q/with-translation [(* (get channel :freq) n) 100 -1000])

   ; (q/with-rotation [(get channel :freq) 0 1 2])
    ;;(q/stroke 134 0 34 120)
    )

  (let [rx x
        ry y
        rz z
        cubesize   (*  ( - freq 100) 10)
        cubespace 50]


   ; (q/with-translation [(* rx (+ cubesize cubespace)) (* ry (+ cubesize cubespace) )  (* rz (+ cubesize cubespace) )])

    (q/with-rotation [ (rand-int 360) 5 0 1]
      (q/fill 25 (rand-int 25) 10 (* 10 ttl))
                                        ; (q/box cubesize (get channel :freq) 100 )
      (q/stroke 200)
      (q/stroke-weight b)
      (q/with-translation [(q/random ( q/width )) (q/random (q/height)) (q/random 100) ]
        (q/box (/ freq 2 ))))
        )
  )


(defn draw [x y z q r s ttl a b c d freq peak beat id]

  (q/with-rotation [y 0 0 1]
    (q/with-translation [(* x (/ (q/width) 127))  (* y (/ (q/height) 127))    (* 10 (- z 100 )) ]
      ;(println peak)
      (q/fill (* 2 a) (* 50 (- 80 peak)) 0)
      (q/stroke-weight (* b 10))
      (q/box (* a peak))))
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
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  )

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
