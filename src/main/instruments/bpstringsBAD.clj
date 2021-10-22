(ns main.instruments.bpstrings
  (:require
   [main.util :refer [drop-nth update-each]]
   [main.botpop :refer [measure]]
   [quil.core :as q]))


(def viz (atom []))
(def vizbiz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(def bootprocess [
    "initializing RNN"
    "enabling model"
    "loading preseed variables"
    "starting midi clock"
    "establishing link"
    "sending notes"
    "booting string mode"
    "getting errors from database"
    "learning from previous mistakes"
    "be more human"
    "be more robot"
    ])
(def status (atom {:debug true :violin1 true}))
(def params (atom {:p1 64 :p2 64 :p3 64} ))
(def dynamics (atom {:bootphase 0 :hfreq1 1200 :hfreq2 600 :hfreq3 300 :hfreq4 200 :nfreq1 0 :nfreq2 0 :nfreq3 0 :nfreq4 0 :amp1 0 :amp2 0 :amp3 0 :amp4 0} ))





(defn module [x y z seed]
 ;; (q/ortho)
 ;; (q/perspective)
  ;;draws some squares
  (q/noise-seed seed)
  (q/stroke-weight 0.5)
  (q/stroke 255)
  (let [size (+ 1 (get params :p1))
        layers (int (/(get params :p2)  2))]
    (q/fill 255 0 0)
    (q/with-translation [0 0 500]
      (q/text (str "seed " seed ) x y))
    (dotimes [n layers]
      (q/fill 255 (* 2 (get params :p3)))
      (q/with-translation [ x y (* n (* (get params :p4) 1))]
        (q/with-rotation [ (* (main.botpop/tr) 0) 0 0 1]
          (q/rect   (* size (q/noise n))
                    (* size (q/noise (+ 0.5 n)))
                    (* size (q/noise n))
                    (* size (q/noise (+ 0.5 n)))))
      )
    )
  )
)


(defn decordraw []
  (q/with-translation [150 150 0]
    (q/with-rotation [(* (get @main.botpop/measure :measure) 45) 0 1 0])
    (q/fill 0 255 0)
    (q/box 100)
    )
     ; ( dotimes [n a])
  (q/with-translation [20 (* 2 50) 30]
    (q/box 30)
  )
)

(defn draw [x y z ttl seed]
  "main draw for this visual instrument"
  (q/fill
   (if (> x 500) 255 0)
   255
   255
   (if (>  ttl 255) 255 ttl ))
  (q/with-translation [x y z]
    (q/fill 255)
    (q/box 1500)
    (module (+ 400  x) y z ttl seed))


)



(defn render []
  (decordraw)
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          ttl (get (nth @viz n) :ttl)
          seed (get (nth @viz n) :seed)]
      (draw x y z ttl seed)
      )
    )
  ;; (if (get status :debug)
  (dotimes [n (count @viz)]
    (do
      (q/fill 255 )
      (q/text (str "booting BOTPOP" (get status :violin1) ) 50 100)
    )
  )
)


(defn add []
 ; (println "add called")
  (let [x 250
        y (rand-int 400)
        z 0
        ttl (+ 100 (* 5 (get status :p1)))
        seed (rand-int 500)]
    (if (= 0 (count @viz))
      (reset! viz []))
   ; (println viz)
    (swap! viz conj {:x x :y y :z z :ttl ttl :seed seed :virgin true}))
  (println viz)
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

(defn updateviz []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :x] (fn [x] (+ x 1)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      )
  )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  )




(defn updatevizBAD []
  ;(println "updateviz called")
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      (if (get (get @viz n) :virgin)
        (do ;; virgins only
          (swap! viz update-in [n :x] (fn [x] (+ x 1)))
          ;; (println viz)
          (if (> (get (nth @viz n) :x) 620)
            ;; (println (nth @viz n))
            (do
              (swap! viz update-in [n :virgin] false)

              ;; (swap! viz update-in [n :x] noiseX)
              ;; (swap! viz update-in [n :y] noiseY)
              ;; (swap! viz update-in [n :z] noise )
              )
            )
          )
        (do ;;non virgins only!
          (swap! vizbiz conj (nth @viz n))
          (swap! viz update-in [n :ttl] dec)
          )

        )
      ;;else mark pill for deletion
      (do
      ;  (println "deleting")
        (swap! vizcount conj n)))
    )
  (dotimes [n (count @vizcount)]
    ;;(println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz))
    )

  )

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
