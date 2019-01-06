(ns main.instruments.backflow
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]
   [main.kaos :refer [tr]
    ]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(defn draw [x y z q r s ttl a b c d freq peak beat id]

  (dotimes [n (mod beat 4 )]

    (q/with-translation [(+ 65 (* 10 (+ ttl (* (tr) b)))) (+ 400  (* n 100)) 0]
      (q/with-rotation [beat 1 n 5 ]
        (q/with-rotation [(* 4 a) 0 (* 0.4  q) 0]
          (q/fill q r 23  )
          (q/stroke-weight 3)
;          (q/stroke 1 (q/random  123) 290 100)
          (dotimes [y 30]
            (q/with-translation [200 (* y 200) 0]
              (q/box 40 (* y 12) 100 )))))))
  )


(defn render [channel]
  ;;; if channeldata
  (if (get  channel :rendering)
    (dotimes [n (count @viz)]
;;      ( println n channel)
      (let [x (get (nth @viz n) :x)
            y (get (nth @viz n) :y)
            z (get (nth @viz n) :z)
            q (get (nth @viz n) :q)
            r (get (nth @viz n) :r)
            s (get (nth @viz n) :s)
            ttl (get (nth @viz n) :ttl)
            a (get channel :a)
            b (get channel :b)
            c (get channel :c)
            d (get channel :d)

            freq (get channel :freq)
            peak (get channel :peak)
            beat (get channel :beatnumber)
            id (get channel :id)
            ]
        (draw x y z q r s ttl a b c d freq peak beat id)
        )
      ))
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing boxgrid" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )

(defn add [channel]
  (let [ x 0
        y 0
        z 0
        q (rand-int 255)
        r (rand-int 255)
        s (+ 50 (rand-int 50))
        ttl 10]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x (rand-int x) :y (rand-int y) :z (rand-int z) :q q :r r :s s :ttl ttl :sticky false })))
  )


(defn updateviz [channel]
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
;        (swap! viz update-in [n :y] (fn [y] (- y 10)))
        (swap! viz update-in [n :q] (fn [q] (* -0.5 q)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  ;(println @pills)
  )


(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
