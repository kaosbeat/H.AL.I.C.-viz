(ns main.botpop
  (:require [quil.core :as q]
            [main.instruments.bpstrings :as bps]))





(defn annotate [x1 y1 z1 x2 y2 z2 text]
  (q/stroke 255)
  (q/fill 255 0 0)
  (q/stroke-weight 2)
  (q/line x1 y1 z1 x2 y2 z2)
  (q/text text x1 y1 z1)
  )



(def minfreq (atom 20000))
(def maxfreq (atom 0))

(defn drawDebug [channel]
  (if (> (get @channel :freq) @maxfreq)
    (reset! maxfreq (get @channel :freq)))
  (if (< (get @channel :freq) @minfreq)
    (reset! minfreq (get @channel :freq)))

  (let [a    (get @channel :a)
        b    (get @channel :b)
        c    (get @channel :c)
        d    (get @channel :d)
        x    (get @channel :x)
        y    (get @channel :y)
        z    (get @channel :z)
        peak (- (get @channel :peak ) 50 )
        modbeat (mod (get @channel :beatnumber) 8)
        freq (q/map-range (get @channel :freq ) @minfreq @maxfreq 0 100)
        ]




    (q/with-translation [1000 200 0]
      (annotate 100 90 0 0 0 0 (str "peak " peak))
      (q/rect 100 100 peak 20 )

      (annotate -150 200 0 0 0 0 (str "beat " ( + 1 modbeat)))
      (dotimes [n ( + 1 modbeat)]
        (q/with-translation [-150 205 0]
          (q/rect (* n 12) 0 10 10 )))


      (annotate -150 (- (* a 2) 127) 0 0 0 0 (str "a " a))
      (annotate -200 (- (* b 2) 127) 0 0 0 0 (str "b " b))
      (annotate -250 (- (* c 2) 127) 0 0 0 0 (str "c " c))
      (annotate -300 (- (* d 2) 127) 0 0 0 0 (str "d " d))

      (q/with-rotation [(zerorounddeg) (tr) 1 0]
        (q/fill 255 525 20 110)
        (q/shininess 250)
        (q/box freq)
        (q/random-seed 52)
        (dotimes [n ( / (* a peak) 30)]
          (q/stroke-weight 0.3)
          (q/line 0 0 0 (- (q/random 1000) 500)(- (q/random 1000) 500)(- (q/random 1000) 500)))
        )
      (q/with-translation [-30 80 0]
        (q/fill 255 0 0)
        (q/text (str  "channel " (get @channel :id) " " (get @channel :freq) )  0 0 200)
        )))


  )






(defn  debugmidistrings [channel audiochannel x y title]
  (q/ortho)
  (q/fill 255 4)
  (q/rect x y 300 200)
  (q/fill 255 255 255)
  (q/rect (+  (* 15 @measure) (+ x 240)) (+ 0 y) 15 15 )
  (q/text title (+ x 150) (+ y 30) )
  (let [msize 60]
    (q/with-translation [(+ 10 x) (+ 10 y) 0]
      (bps/module x y 0 (get @lastnote channel) msize)
      )
    (q/with-translation [(+ 10 x) (+ 70 y) 200]
      (q/with-rotation [1.57 0 1 0]

        (q/with-rotation [(tr)  0 0 1])

        (q/stroke 255)
        (q/stroke-weight 2)
        (q/line 0 0 0 0 0 200)
        (q/with-translation [(/ msize 29) (/ msize 4) 0]
          (bps/module x y 0 (get @lastnote channel) msize )))))


  (q/with-translation [(+ 10 x) (+ 180 y) 200]
    (q/fill 255 0 0)
    (q/text-size 20)
    ;;(q/text (str (get @lastnote channel)) 0 0 )
    (q/text (str  "channel "channel" " (get audiochannel :freq) )  0 0 200))


 (q/perspective)
  )

(def titlestring ["debug" "violin1" "violin2" "alto" "cello"])
(defn debugstringtype [x y title]
  (q/ortho)
  (q/no-fill)
  (q/rect x y 300 200)
  (q/with-translation [(+ x 150) (+ y 100) 0]
    (q/with-rotation [@measure 1 1 0]
      (bps/drawbiz 0 0 0 10 @lasttype)))
  (q/fill 0 255 0)
  (let [txt (nth titlestring (- @lasttype 1))]
    (q/text txt x (+ 189 y) ))
  (q/perspective)
  )

(def notestatistics (atom {}))
(defn fillnotestatistics []
  (reset! notestatistics {})
  (dotimes [c 16]
    (swap! notestatistics assoc (str "ch" c) {})
    (dotimes [n 127]
      (swap! notestatistics assoc-in [(str "ch" c) n] 0)
      )))

(fillnotestatistics)
(defn debugnotestatistics [channel x y columns size]
  ;;find note with the highest usage
                                        ; (key (apply max-key val (get @notestatistics "ch4")))
  (q/ortho)
  (q/with-translation [x y 0]
    (q/no-fill)
    (let [collection (get @notestatistics channel)
          coll (filter (fn [[k v]] (< 0 v)) collection )
          maxkey (key (apply max-key val coll))
          max (val (apply max-key val coll))]
      ;(println coll maxkey max)
      (dotimes [n (- (count coll) 1) ]
       ; (let [x (mod n columns) y (int (/ n columns)) s (* size (/ (get coll n) max))])
        (let [x (mod n columns)
              y (int (/ n columns))
              m (val (nth coll n ))
              s (* size (/ m  max))
              ]
         ; (println size)
          (q/with-translation [(* x size) (* y size)] 0
        (bps/module (* x size) (* y size) 0 n s))
          (q/text (str m) (* x 50) size  )
          )
        )


      )


    (q/perspective))
  )
