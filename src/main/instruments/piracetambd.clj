(ns main.instruments.piracetambd
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))

(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

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
    (q/text "piracetam" 10 -10)

                                        ;    (println "blah")


                                        ;      (with-open [fd (clojure.java.io/writer "foo.txt")] (binding [*out* fd] (println (str "data " (get @data :size))) (println (str (mm/pirad)))))
    )

  )


(defn draw [x y z a b c d freq peak beat id ttl color ]
  "main draw for this visual instrument"
;  (println d)
  (if (> d 0)
                                        ; (println "drawing " id  x y z freq beat)
    (dotimes [n (int 6)]
      (let [size (* (/ b 10) 1)
            spreadx (+ x (rand-int (* (* c 5) (/ b 127))))
            spready   (+ y (rand-int (* (* c 5) (/ b 127))))]
        (q/with-translation [ spreadx spready  0]
          (q/with-rotation [beat beat 1 beat]
            (q/fill (nth color 0)(nth color 1)(nth color 2) (* (+ 62 ttl)  (/ d 127)))
            (q/stroke-weight 3)
            (q/stroke 255 (* 2 d))
            (q/box (/ 10000 (* (+ 20 b) 1))))))))




  )


(defn render [channel]
  ;;; if channeldata

  (if (get  channel :rendering)
    (do
      (dotimes [n (count @viz)]
;           ( println n)
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
            ; (swap! data assoc :x x :y y :z z :ttl ttl :a a :b b :c c :d d :freq freq :peak peak :beat beat :id id)
             (draw x y z a b c d freq peak beat id ttl color)

             )
           ) ))


  (if (get channel :debug) (do
                             (q/fill 255)
                             (q/text-size 20)
                             (q/text (str "drawing piracetambd " (get  channel :id))  50 (* (get  channel :id) 100))
                             (q/text (str "beat " (get channel :beatnumber)) 350 (* (get  channel :id) 100) )
                             (datadebug 1700 100 20 data )))


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
;        beat (mod  (get @main.kaos/midibd :beat) 4)
        beat (mod  (get channel :beatnumber) 4)
        color [(rand-int 255) (rand-int 255) (rand-int 255)]
        ttl (+ 400 (int (/ b 4)))]\
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
        (swap! viz update-in [n :z] (fn [z] (- z 10)))
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
