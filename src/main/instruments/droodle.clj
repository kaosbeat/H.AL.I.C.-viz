(ns main.instruments.droodle
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def debug (atom false))
(def rendering (atom false))

(defn toggleDebug []  (if  (= @debug true) (reset! debug false) (reset! debug true))
  )
(defn toggleRender []
  (if  (= @rendering true) (reset! rendering false) (reset! rendering true))
  )


;; template for visual instrument instance
;;
;;util



(letfn [(f [randomizer bounds]  (lazy-seq (cons (.nextInt randomizer bounds) (f randomizer bounds))))]
  (defn create-random
    ([bounds] (f (java.util.Random.) bounds))
    ([bounds seed] (f (java.util.Random. seed) bounds))
    ))

(defn drawRandomLine [bounds & [seed]]
  (let [coords (if seed
                 (take 6 (create-random bounds seed))
                 (take 6 (create-random bounds)))
        x1 (nth coords 0)
        y1 (nth coords 1)
        z1 (nth coords 2)
        x2 (nth coords 3)
        y2 (nth coords 4)
        z2 (nth coords 5)]
    (q/line x1 y1 0 x2 y2 0)
    ;(println x1 y1 z1 x2 y2 z2)
    ))

(defn drawRandomParallel [bounds size & [seed]]
  (let [coords (if seed
                 (take size (create-random bounds seed))
                 (take size (create-random bounds)))
        yoff (/ bounds size)]
    (dotimes [n (/ size 2)]
      (let [y (* n yoff)
            z (rand-int 30)]
        (q/line (nth coords (* n 2)) y 0 (nth coords (+ 1 (* 2 n))) y 0)
        ))))


(defn drawOrthoStruct [bounds size & [seed]]
 (let [coords (if seed
                (take (* 4 size) (create-random bounds seed))
                (take (* 4 size) (create-random bounds)))
       x (atom 0)
       y (atom 0)
       length (* 4 (- size 4))]
   (dotimes [n (/ length 4)]
;     (println n)
     (let [a (nth coords (* n 4))
           b (nth coords (+ 1 (* n 4)))
           c (nth coords (+ 2 (* n 4)))
           d (nth coords (+ 3 (* n 4)))]
                                        ;(println a b c d)

       (if (> (- bounds @x) a)
         (do  (q/line @x @y a @y) (reset! x a) )
         (do  (q/line @x @y (- 0 a) @y) (reset! x (- 0 a)) ))
       (if (> (- bounds @y) b)
         (do (q/line @x @y @x b) (reset! y b))
         (do (q/line @x @y @x (- 0 b)) (reset! y (- 0 b))))
       (if (and (< (- bounds @x) c) (< (- bounds @y) d))
           (do  (q/line @x @y c d) (reset! x c) (reset! y d)))
       (if (and (>= (- bounds @x) c) (< (- bounds @y) d))
           (do  (q/line @x @y (- 0 c) d) (reset! x (- 0 c)) (reset! y d)))
       (if (and (>= (- bounds @x) c) (>= (- bounds @y) d))
           (do  (q/line @x @y (- 0 c) (- 0 d)) (reset! x (- 0 c)) (reset! y (- 0 d))))
       (if (and (< (- bounds @x) c) (>= (- bounds @y) d))
         (do  (q/line @x @y c (- 0 d)) (reset! x c) (reset! y (- 0 d))))
       )
     )
   )
  )



(defn droodle1 [seedlist bounds]
  (dotimes [n 10]
    (drawRandomLine bounds n))
  )





(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
  (let [ measure (mod beat 1)]
    (q/fill 2 0 0)
    (q/stroke 0 0 5)
    (q/with-translation [x y z]
      (case measure
        0 (q/box 10 10 10)
        1 (q/box 1000 10 10)
        2 (q/box 10 1000 10)
        3 (q/box 10 10 1000)
        ))

    )
  (q/stroke-weight 10)
  (q/stroke (nth s 0) (nth s 1) (nth s 2) 230)
  ;(drawRandomLine 1000 q)
;  (droodle1 (take 5 (create-random 1000 5)) 1000)
  (q/with-translation [x y 0 ]
                                        ;(drawRandomParallel 1500 (+ a 10) q)
    (q/stroke 25 255 (q/random 10) (* 2 ttl))
    (drawOrthoStruct 150 (int (/ 50 5)) q))
  )

(defn render [channel]
  ;;; if channeldata


  (if (get  channel :rendering)
    (dotimes [n (count @viz)]
      ;( println n channel)
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
  (if (get channel :debug) (do  (q/fill 255) (q/text (str "drawing droodle" (get  channel :id) ) 50 (* (get  channel :id) 100))))
  )



(defn add [channel]
  ;(println "adding stuff")
  (let [x (rand-int 1000)
        y (rand-int 1000)
        z 0
        q (rand-int 200)
        r (rand-int 50)
        s [(rand-int 255) (rand-int 255) (rand-int 255)]
        ttl  25]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky false }))))



(defn updateviz [channel]
  "viz objects have properties:
  x y z position arguments
  q r s arbitrary atributes, set per particle
  ttl  time-to-live >by default decreases per updaterun
  sticky bit, can make it stay, be carefull what you whish for"
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :y] (fn [y] (- y (rand-int 10))))
        (if (< 18 (get (get @viz n) :ttl))
          (swap! viz update-in [n :q] (fn [q] (rand-int 200))))
        )
      (swap! vizcount conj n)
      )
    )
  (dotimes [n (count @vizcount)]
    (reset! viz  (drop-nth (nth @vizcount n) @viz)))
  )





(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
