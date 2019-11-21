(ns main.instruments.carpetcross
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(def series [1 1 1 3 5 11 5 3 1 1 1])
(defn drawCross [ser size rectsize cmod]
  (q/with-translation [(- (/  (* size (count ser)) 2 ) (/ size 2))  (- 0 (/  (* size (count ser)) 2 ))  0]
    (q/with-rotation [(q/radians 45) 0 0 1]
      (q/fill 123 123 0)
      (q/rect 0 0 (* 0.707 (* size (count ser))) (* 0.707 (* size (count ser))))))
  (dotimes [n (count ser)]
    (q/with-translation [(* n size) 0 0]
      (dotimes [m (nth ser n)]
        (q/with-translation [0 (- (* m size) (* (int  (/ (nth ser n) 2)) size ) ) 0 ]
          (q/with-translation [(- 0 (/ rectsize 2)) (- 0 (/ rectsize 2)) 0]
            (q/with-rotation [1 0 0 0]
              (if (=  1 (mod n cmod))
                (q/fill 255 123 0)
                (q/fill 0 123 255))
              (q/rect 0,0,rectsize,rectsize)))))
      )
 ))

(defn draw [data]
  "main draw for this visual instrument"
                                        ; (println "drawing " id  x y z freq beat)
  (dotimes [n 1]
    (q/with-translation [ 500 500 100 ]
      (q/with-rotation [0 0 0 (get data :rot)]
        (q/fill 255  25 0 )
        (q/stroke-weight 1)
        (q/stroke 255 255 0)
                                        ;(q/box (get data :size))

        )))

  (q/with-translation [60 600 0 ]
    (drawCross series 50 40 2)
    )
  (q/with-translation [600 600 0 ]
    (drawCross series 60 30 3)
    )
  (q/with-translation [1200 600 0 ]
    (drawCross series 50 45 4)
    )
  )



(defn render [channel]
  (if (get channel :rendering)
    (let [a (get channel :a)]
         (draw {:rot a}))
    )

  )





(defn add [channel]
  (let [x 20
        y 30
        z 40
        ttl 100]
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
