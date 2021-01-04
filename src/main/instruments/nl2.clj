(ns main.instruments.nl2
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))


(def data (atom {}))
(def data (atom {:x 0 :y 0 :z 0 :ttl 10 :a 0 :b 0 :c 0 :d 0 :freq 10 :peak 10 :beat 1 :id 1}) )

(defn datadebug [x y ts data]
                                        ;(q/text-font font)
  (q/text-size ts)
  (q/with-translation [x y 0]

    (dotimes [n (count @data)]
      (q/fill 255)
      (q/stroke 255 0 255)
      ;(q/text (str (mm/pirad)) 0 20)
                                        ;(q/text "bklad" 200 300)
      (q/text (name (nth (map first @data) n))  0 (* 1.2 (* n ts)) )
      (q/text (str (float (nth (map second @data) n))) (* ts 6)  (* 1.2 (* n ts)) )
      ;(q/text (str @inputdata) 0 0 )
      )
    (q/text "nl2" 10 -10)

                                        ;    (println "blah")


                                        ;      (with-open [fd (clojure.java.io/writer "foo.txt")] (binding [*out* fd] (println (str "data " (get @data :size))) (println (str (mm/pirad)))))
    )

  )



(defn draw [x y z a b c d freq peak beat id]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)

  (q/random-seed id)

  (let [r (int (q/random 255))
        g (int (q/random 255))
        b (int (q/random 255)) ]
    (q/stroke-weight c)                                        ;    (println colorseed)
;    (q/stroke-weight peak)
    (q/fill r g b (* 1 (- peak 30 )))

    (q/stroke r g b)
    )

  (q/with-rotation [90 1 0 0])
  (q/with-translation [x (+ 1000(/ y 50)) z]
                                        ;(q/box 100 (* 30 (- peak 60)) 1000)

    (dotimes [n 5]
      (let [x1 (q/random 1920)
            y1 10
            z1 0
            x2 (+ (q/random 100)  x1)
            y2 0
            z2 (q/random 500)]
        (q/line  (+ (* 50 n) x1) y1 z1 x2 y2 z2)))
;    (q/box (q/random 300) 1 (q/random 600)  )
    (dotimes [n 10]
      (q/with-translation [ (* n 50) (rand-int 50) (rand-int 100) ]
        (q/fill 255 10)
        (q/stroke 255)

        ;(q/box 100 100 100 )
        )))

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
            id (get (nth @viz n) :seed)   ;;;abusing ID for SEED
            ]
        (swap! data assoc :a a)
        (swap! data assoc :b b)
        (swap! data assoc :c c)
        (swap! data assoc :d d)

        (swap! data assoc :beat beat)
        (swap! data assoc :freq freq)
        (swap! data assoc :peak peak)

        (draw x y z a b c d freq peak beat id)
        )
      ))
  (if (get channel :debug) (do
                             (q/fill 255)
                             (q/text-size 20)
                             (datadebug 1700 100 20 data )))
  )


(defn add [channel]
  (let [ x (rand-int 1920)
        y (rand-int 1200)
        z -10000
        ttl (+ 10 (* 50 (get channel  :a)))
        seed (get channel :beatnumber)]

    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :seed seed :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :seed seed :sticky false }))

    (swap! data assoc :ttl ttl))
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
        (swap! viz update-in [n :z] (fn [z] (+ z (+ 1 (get channel :a)))))
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
