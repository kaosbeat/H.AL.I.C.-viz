(ns main.instruments.linesquares
  (:require [main.core]
            [quil.core :as q]))


;(ns main.core)
;;util
(defn drop-nth [n coll]
  (vec (keep-indexed #(if (not= %1 n) %2) coll))
  )



(def linesquares (atom []))
(def linesquarescount (atom 0))

(defn draw [w h horfillrate verfillrate channel ]

  (dotimes [n ( + 1 (* 100 horfillrate)) ]
    (let [
          x1 (* 100 (* (get channel :peak) (* n (/ w  (* w horfillrate)))))
          y1 (q/random 1900)
          x2 (* 8  x1)
          y2 h]
      (q/stroke (/ (get channel :freq) 100) 255 20 269)
      (q/stroke-weight (* 17 (get channel :peak)))
      (q/line x1 y1 x2 y2 )
      (q/fill (get channel :freq) 203 0 12)

      (q/with-translation [(* n (/ (get channel :freq ) 10 )) 0 0 ]
        (q/with-rotation [ (* n 10) 0 1 (* n 0.3)]
          (q/box (* n 50) )
          (q/line x1 y1 x2 y2)))
      )
    (q/line w 0 w h)

    )
  )

(defn render [channel]
  (dotimes [n (count @linesquares)]
    (let [x (get (nth @linesquares n) :x)
          y (get (nth @linesquares n) :y)
          z (get (nth @linesquares n) :z)
          w  100
          h (/  (get  channel :freq) 1)
          hfr  (+ (get channel :peak) 0.001)
          vfr 1
          ]

      (q/with-translation [ x y z]
        (q/stroke-weight 2)
        (draw w h hfr vfr channel )))

    )

  )


(defn add [x y z ttl]
  (if (= 0 (count @linesquares))
    (reset! linesquares []))

  (swap! linesquares conj {:x x :y y :z z :ttl ttl })

  )


(defn updatels []
  ; for some reason not all pills are deleted
  (reset! linesquarescount [])
  (dotimes [n (count @linesquares)]
    (if (false? (= 0 (get (get @linesquares n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! linesquares update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! linesquares update-in [n :y] (fn [y] (- y 10)))
        )
      ;else mark pill for deletion
      (swap! linesquarescount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @linesquarescount)]
;    (println " really dropping stuff")
    (reset! linesquares  (drop-nth (nth @linesquarescount n) @linesquares)))
  ;(println @pills)
  )
