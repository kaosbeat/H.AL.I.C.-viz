(ns main.instruments.squaretunnel
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))


(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(def tunneldepth (atom 30))


(defn draw [x y z q r s ttl a b c d freq peak beat id]
  "main draw for this visual instrument"
  (comment  (let [ measure (mod beat 4)]
              (q/fill 255 0 0)
              (q/stroke 225 0 255)
              (q/with-translation [(q/random 1000) (q/random 1000) (q/random 100) ]
                (case measure
                  0 (q/box 10 10 10 )
                  1 (q/box 1000 10 10)
                  2 (q/box 10 1000 10)
                  3 (q/box 10 10 1000)
                  )) ))




  ;;;left plane
  (q/with-rotation [1.57 0 1 0]
    (boxextruder x y (* -1  z) a b c d q r s)

    )



  (q/with-translation  [1900 0 0]
    (q/with-rotation [1.57 0 1 0]

 (lineartunnel x y z a b c d q r s)
      ))

  (q/with-translation  [250 0 0]
    (q/with-rotation [-1.57 1 0 0]
(noiseplane x y z a b c d q r s)
      ))

  (q/with-translation  [250 1400 0]
    (q/with-rotation [-1.57 1 0 0]
      (lineartunnel x y z a b c d q r s)
      ))



  )

(defn lineartunnel [x y z a b c d q r s]
(dotimes [xpos @tunneldepth]
      (if (= x xpos)
        (q/fill 255)
        (q/fill (nth r 0) 255) )
      (dotimes [ypos 6]
        (if (= y ypos)
          (q/with-translation [(* xpos  250) (* ypos 250) 0]
            (q/rect 0 0 200 200)))))
  )

(defn boxextruder [x y z a b c d q r s]
  (dotimes [xpos @tunneldepth]
        (dotimes [ypos 6]
          (if (= x xpos)
            (if (= y ypos)
              (do
                (q/fill (nth r 0) (nth r 1) (nth r 2))
                (let [boxx (/ s 2)
                      boxy (/ s 2)
                      boxz (/ z 2)]
                  (q/with-translation [(* xpos  250) (* ypos 250) 0]
                                        ;(q/no-fill)
                    (q/stroke-weight 2)
                                        ;(q/rect 0 0 s s)
                    (q/with-translation [boxx boxy boxz]
                      (q/box s s z))
                    ))))))))

(defn noiseplane [x y z a b c d q r s]
  (dotimes [xpos 6]
        (dotimes [ypos @tunneldepth]
          (q/with-translation [(* xpos  250) (* ypos 250) (rand-int 30)]
            (q/rect 0 0 200 200)))))


(defn render [channel]
  ;;; if channeldata
  (q/perspective)
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
  (let [ x (rand-int 25)
        y (rand-int 6)
        z 0
        q (mod (get channel :beatnumber) 4)
        r [
           (rand-int 255) (rand-int 255) (rand-int 255) ]
        s (+ 200 (rand-int 50))
        ttl 10]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :q q :r r :s s :ttl ttl :sticky true })
      (swap! viz conj {:x (rand-int x) :y (rand-int y) :z (rand-int z) :q q :r r :s s :ttl ttl :sticky false })))
  )




(defn updateviz [channel]
  ;viz objects have properties:
  ;x y z position arguments
  ;q r s arbitrary atributes, set per particle
  ;ttl  time-to-live >by default decreases per updaterun
  ;sticky bit, can make it stay, be carefull what you whish for
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :z] (fn [z] (- z (rand-int 100))))
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
