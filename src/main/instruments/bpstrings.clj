/(ns main.instruments.bpstrings
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizbiz (atom []))
(def vizcount (atom []))
(def vizbizcount (atom []))
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
(def status (atom {:debug true :violin1 true :size3D :5}))
(def params (atom {:p1 64 :p2 64 :p3 64} ))
(def dynamics (atom {:bootphase 0 :hfreq1 1200 :hfreq2 600 :hfreq3 300 :hfreq4 200 :nfreq1 0 :nfreq2 0 :nfreq3 0 :nfreq4 0 :amp1 0 :amp2 0 :amp3 0 :amp4 0} ))


(defn fillvizbiz [size3D]
  (reset! vizbiz [])
  (dotimes [x size3D]
    (dotimes [y size3D]
      (dotimes [z size3D]
        (let [space 20]
         (swap! vizbiz conj {:x x :y y :z z :ttl 10 :type 1})
         ;; (swap! vizbiz conj {:x (* space x) :y (* space y) :z (* space z) :ttl 0 :type 0 })
          ;;(println "duh")
          )))))

(fillvizbiz 5)


(defn draw [x y z ttl type]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)
  (q/with-translation [x y z]
    (q/fill 255 255 0 ttl)
    (q/box (* type  50) 50 50)
  )
  )

(defn drawbiz [x y z ttl type]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)
  (q/with-translation [x y z]
    (q/fill 255 255 0 ttl)
    (q/box (* type  50) 50 50)
  )
  )





(defn render []
  ;;; if channeldata
  (dotimes [n (count @viz)]
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)
            ttl (get (nth @viz n) :ttl)
            type (get (nth @viz n) :type)]
        (draw x y z ttl type)
        )

      )
  (dotimes [n (count @vizbiz)]
    (let [x (get (nth @vizbiz n) :x)
          y (get (nth @vizbiz n) :y)
          z (get (nth @vizbiz n) :z)
          ttl (get (nth @vizbiz n) :ttl)
          type (get (nth @vizbiz n) :type)
          space 20
          ]
      (draw (* space  x) (* space  y) (* space z) ttl type)
      ))


)

(defn add [type]
  (let [x 200
        y (+ 500 (rand-int 400))
        z 0
        ttl 100
        type type]
    (if (= 0 (count @viz))
      (reset! viz []))
    (swap! viz conj {:x x :y y :z z :ttl ttl :type type})
  )
)

(defn updateviz []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (reset! vizbizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))

                                        ;decrease TTL in pill if ttl > 0
      (do
        ;;(swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :x] (fn [x] (+ x 5)))
        (if (> (get  (get @viz n) :x) 1500)
          ;;(println "fgound one")
          (do
            (if (= 0 (count @vizbiz))
              (reset! vizbiz []))
            ;;(swap! vizbiz conj (get @viz n))
            ;; (println "adding here")
            (swap! vizcount conj n)
            ) ;else mark pill for deletion
          )
        )
      )
    )
  (dotimes [n (count @vizcount)]
    ;;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz))
    )

  (dotimes [n (count @vizbiz)]
    (if (false? (= 0 (get (get @vizbiz n) :ttl)))
      (do
        (swap! vizbiz update-in [n :ttl] dec)
      ;;  (swap! vizbiz update-in [n :y] (fn [y] (+ y 1)))
        )
      (swap! vizbizcount conj n)
    )
   )
  (dotimes [n (count @vizbizcount)]
    ;;    (println " really dropping stuff")
   ;; (reset! vizbiz  (drop-nth (nth @vizbizcount n) @vizbiz))
    )
  )

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
