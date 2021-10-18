(ns main.instruments.botpopphase0
  (:require
   [main.util :refer [drop-nth update-each]]
   [main.botpop :refer [measure]]
   [main.instruments.booting]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))
(def status (atom {:bootphase 0 :highestfreq 0} ))


(defn module [x y z seed a b c d]
 ;; (q/ortho)
 ;; (q/perspective)
  ;;draws some squares
  (q/noise-seed seed)
  (q/fill 255 (* 2 d))
  (q/stroke-weight 0.5)
  (q/stroke 255)
  (let [size (+ 1 a)
        layers (int (/ b 2))]
    (dotimes [n layers]
      (q/with-translation [x y (* n (* c 1))]
        (q/with-rotation [ (* (main.botpop/tr) 0) 0 0 1]
          (q/rect   (* size (q/noise n))
                    (* size (q/noise (+ 0.5 n)))
                    (* size (q/noise n))
                    (* size (q/noise (+ 0.5 n)))))))

      )
  )


(defn decordraw []





  (q/with-translation [150 150 0]
    (q/with-rotation [(* (get @main.botpop/measure :measure) 45) 0 1 0])
    (q/fill 255 0 0))
     ;   (q/box 100)


     ; ( dotimes [n a])


  (q/with-translation [20 (* 2 50) 30]
    (q/box 30)
    )




  )

(defn draw [x y z a b c d ttl freq peak beat id seed]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)



  (dotimes [n (+ 1 b)]
    ;(println (nth bootprocess (get @status :bootphase) ))
    )

  (q/fill
   (if (> x 500) 255 0)
   255
   0
   (if (>  ttl 255) 255 ttl ))

  ;(println x y z)
  (q/with-translation [x y z]
    (module x y z seed a b c d))

  (if (> freq (get @status :highestfreq))
    (do
      (swap! status assoc :highestfreq freq)
      (swap! status update-in [:bootphase] inc )
      )))



(defn render [channel]
  ;;; if channeldata
  (if (get  channel :rendering)
    (do
      (decordraw)

      (dotimes [n (count @viz)]
                                        ;  ( println n channel)
        (let [x (get (nth @viz n) :x)
              y (get (nth @viz n) :y)
              z (get (nth @viz n) :z)
              ttl (get (nth @viz n) :ttl)
              seed (get (nth @viz n) :seed)

              a (get channel :a)
              b (get channel :b)
              c (get channel :c)
              d (get channel :d)

              freq (get channel :freq)
              peak (get channel :peak)
              beat (get channel :beatnumber)
              id (get channel :id)
              ]
          (draw x y z a b c d ttl freq peak beat id seed)
          )
        )))
  (if (get channel :debug)
    (do
      (q/fill 255 )
      (q/text (str "booting BOTPOP" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )


(defn add [channel]
  (let [x -250
        y (rand-int 400)
        z 0
        ttl (+ 10 (* 5 (get channel  :a)))
        seed (rand-int 500)]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :seed seed :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :seed seed :sticky false })))
  )

(defn destinationupdate [x y z]
  {:x x :y y :z z}
  )

(defn noise [x]
  (let [o 5
        n (rand-int o);
        ]
    (+ x (- o n)) )
  )

(defn noiseX [x]
;;(+ 0 900)
  (noise x)
  )



(defn noiseY [y]
  (+  (* 200 (get @main.botpop/measure :measure)) (noise 170))
  )

(defn updateviz [ channel]
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      (do
        (swap! viz update-in [n :x] (fn [x] (+ x 1)))
        ;
        (if (> (get (nth @viz n) :x) 620)
         ; (println (nth @viz n))
          (do
            (swap! viz update-in [n :ttl] dec)
            (swap! viz update-in [n :x] noiseX)
            (swap! viz update-in [n :y] noiseY)
            (swap! viz update-in [n :z] noise )
            )
          )
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
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
