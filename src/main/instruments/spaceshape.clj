(ns main.instruments.spaceshape
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))



(defn draw [x y z a b c d freq peak beat id]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)
  (dotimes [n 0]
    (q/with-translation [(* (rand-int 100) 200) (rand-int  1200) z]
      (q/with-rotation [ (* a (mod beat 8)) (mod beat 4) 1 0]
        (q/fill (rand-int 255)  255 0 100)
        (q/stroke-weight d)
        (q/stroke 255 freq 0)
        (q/box (+ 100 (* freq 0.9))))))



(q/fill b 3 232 20)
  (q/stroke 255 0 0)
  (q/stroke-weight 20)
  (q/with-rotation [(q/radians z) 0 0 0]
    (dotimes [p 2]
      (q/with-translation [(+  ( * p  20) (rand-int 100)) 600 0]
        (q/begin-shape)
                                        ;top
        (let [divs ( + 3 (mod beat 8))
              div (/ 360 divs)
              r (* 2 freq)]
          (dotimes [n divs]
            (let [xp (* r (q/cos (q/radians  (* n div))))
                  yp (* r (q/sin (q/radians  (* n div))))
                  zp z]
              (q/vertex xp yp zp ))))


                                        ;(q/vertex 50 400 z)
                                        ;(q/begin-contour)
                                        ;(q/vertex 200 200 z)
                                        ;(q/vertex 300 200 z)
                                        ;(q/vertex 250 380 z)
                                        ;(q/end-contour)
        (q/end-shape :close))))
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

  (let [beat (mod (get channel :beatnumber) 4)
        x 50
        y 100
        z (rand-int 360)
        ttl 10]
    (case beat
      0 (do  (* x -1) (* y -1))
      1 (* x -1)
      2 (* y -1)
      3 (do (* x -1) (* y -1)))
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false })))
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
        (swap! viz update-in [n :y] (fn [y] (- y 10)))
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
