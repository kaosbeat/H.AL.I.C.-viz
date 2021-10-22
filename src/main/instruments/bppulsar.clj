(ns main.instruments.bppulsar
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))
(def status (atom {:bootphase 0 :highestfreq 0} ))

(defn draw [x y z]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)
  (q/with-translation [x y z]
    (q/box 50)
  )
)

(defn render []
  ;;; if channeldata
  (dotimes [n (count @viz)]
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)]
        (draw x y z)
      )
  )
)

(defn add []
  (let [x 500
        y (rand-int 400)
        z 0
        ttl 100]
    (if (= 0 (count @viz))
      (reset! viz []))
    (swap! viz conj {:x x :y y :z z :ttl ttl})
  )
)

(defn updateviz []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :y] (fn [y] (- y 1)))
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
