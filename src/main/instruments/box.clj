(ns main.instruments.box
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))



(defn draw [x y z a b c d freq peak beat id]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)
  (dotimes [n (+ 1 b)]
    (q/with-translation [(* (rand-int 100) 20) (rand-int  1200) z]
      (q/with-rotation [ (* 1 (mod beat 64)) (mod beat 1) 0 0]
        (q/fill (rand-int 25)  a 150 225)
        (q/stroke-weight 2)
        (q/random-seed x)
        (q/stroke 2 20 (q/random 25))
        (dotimes [f 1]
          (dotimes [e 1]

            (q/with-translation [(* 100 f) (* 100 e ) (q/random 1000)]
              (q/random-seed e)
              (q/with-rotation [(q/random  (mod beat 4)) 1 e 0]
                (q/box (+ 120 100) 500 (rand-int 200) ))
              ))))))
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
  (if (get channel :debug)
    (do
      (q/fill 255)
      (q/text (str "drawing box" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
  (let [ x 50
        y (rand-int 400)
        z 0
        ttl (+ 1000 (* 5 (get channel  :a)))]
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






   _____ ________________           ___    ____
  / ___// ____/_  __/  _/  _  __   /   |  /  _/
  \__ \/ __/   / /  / /   | |/_/  / /| |  / /
 ___/ / /___  / / _/ /   _>  <   / ___ |_/ /
/____/_____/ /_/ /___/  /_/|_|  /_/  |_/___/




    __  __   ___    __      ____  ______
   / / / /  /   |  / /     /  _/ / ____/
  / /_/ /  / /| | / /      / /  / /
 / __  /_ / ___ |/ /____ _/ / _/ /___ _
/_/ /_/(_)_/  |_/_____(_)___/(_)____/(_)
