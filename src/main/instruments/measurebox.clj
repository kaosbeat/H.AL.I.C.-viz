(ns main.instruments.measurebox
  (:require [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def debug (atom false))
(def rendering (atom false))

(defn toggleDebug []
  (if  (= @debug true) (reset! debug false) (reset! debug true))
  )
(defn toggleRender []
  (if  (= @rendering true) (reset! rendering false) (reset! rendering true))
  )


;; template for visual instrument instance
;;
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )

(defn draw [channel]
  "main draw for this visual instrument"
  (let [ measure (mod ( get channel :beatnumber) 4)]
    (q/fill 255 0 0)
    (q/stroke 225 0 255)
    (q/with-translation [(q/random 1000) (q/random 1000) (q/random 100) ]
      (case measure
        0 (q/box 10 10 10 )
        1 (q/box 1000 10 10)
        2 (q/box 10 1000 10)
        3 (q/box 10 10 1000)
        ))

    )

  )


(defn render [channel]
  ;;; if channeldata
  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          w  100
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (draw channel)))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @viz))
    (reset! viz []))
  (swap! viz conj {:x x :y y :z z :ttl ttl })
  )



(defn stateUpdate []
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
  ;(println @pills)
  )
