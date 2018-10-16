(ns main.instruments.boxes
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

(defn draw [x y z t channel]
  "main draw for this visual instrument"

  (dotimes [n (mod (get channel :beatnumber ) 8 )]

                                        ; (q/with-translation [(* (get channel :freq) n) 100 -1000])

   ; (q/with-rotation [(get channel :freq) 0 1 2])
    ;;(q/stroke 134 0 34 120)
    )

  (let [rx x
        ry y
        rz z
        cubesize   (*  ( - (get channel :freq) 100) 10)
        cubespace 50]


   ; (q/with-translation [(* rx (+ cubesize cubespace)) (* ry (+ cubesize cubespace) )  (* rz (+ cubesize cubespace) )])

    (q/with-rotation [ (rand-int 360) 5 0 1]
      (q/fill 25 (rand-int 255) 100 (* 10 t))
                                        ; (q/box cubesize (get channel :freq) 100 )
      (q/stroke 25)
      (q/stroke-weight 10)
      (q/with-translation [(q/random ( q/width )) (q/random (q/height)) (q/random 100) ]
        (q/box (/ (get channel :freq) 2 ))))
        )
  )


(defn draw [x y z t channel]
  (q/background 0)
  (q/with-rotation [y 0 0 1]
    (q/with-translation [(* x (/ (q/width) 127))  (* y (/ (q/height) 127))    (* 10 (- z 100 )) ]
      (q/fill (* 2 y) 255 0)
      (q/box 500 500 500)))
  )




(defn render [channel]
  ;;; if channeldata

  (dotimes [n (count @viz)]
    (let [x (get (nth @viz n) :x)
          y (get (nth @viz n) :y)
          z (get (nth @viz n) :z)
          w  100
          t (get (nth @viz n) :ttl)
          h (/  (get  channel :freq) 10)
          hfr (get channel :peak)
          vfr 1
          ]

      (q/with-translation [0 0 0]
        (draw x y z t channel)
        (q/stroke-weight (/ hfr 10))
   ;    (q/line 0 1000 1000 0)
        ))

    )
  (if @debug (do  (q/fill 255) (q/text "drawing boxes" 100 150)) )
  )


(defn add []
  (let [x   0
        y   1000
        z   0
        ttl 10]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
      ;;  (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false })
      (swap! viz conj {:x (rand-int x) :y (rand-int y) :z (rand-int z) :ttl ttl :sticky false })
      ))
  )



(defn boxupdate []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        ;(swap! viz update-in [n :y] (fn [y] (- y 1)))
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
