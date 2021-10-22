(ns main.botpop
  (:require [quil.core :as q]))





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


