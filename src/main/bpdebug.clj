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

  (let [a       (get @channel :a)
        b       (get @channel :b)
        c       (get @channel :c)
        d       (get @channel :d)
        x       (get @channel :x)
        y       (get @channel :y)
        z       (get @channel :z)
        peak    (get @channel :peak )
        modbeat (mod (get @channel :beatnumber) 8)
        ;;freq (q/map-range (get @channel :freq ) @minfreq @maxfreq 0 100)
        freq    (* 100 (get @channel :freq))
        ]

    (q/with-translation [1000 200 0]
      (if (> peak 0.3)
        (q/fill 255 0 0)
        (q/fill 0 255 0))
      (annotate 100 90 0 0 0 0 (str "peak " peak))
      (q/rect 100 100 (* 50 peak) 20 )

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
        (q/box (* 20 freq))
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
     ; (println (get @(get channels (get audiochannel :id)) :peak))
      (bps/module2 x y 0 (get @lastnote channel)  (get @(get channels  (-  (get audiochannel :id) 1)) :peak))
      )
    (q/with-translation [(+ 10 x) (+ 70 y) 200]
      (q/with-rotation [1.57 0 1 0]

        (q/with-rotation [(tr)  0 0 1])

        (q/stroke 255)
        (q/stroke-weight 2)
        (q/line 0 0 0 0 0 200)
        (q/with-translation [(/ msize 29) (/ msize 4) 0]
          (bps/module2 x y 0 (get @lastnote channel)  (get @(get channels (-  (get audiochannel :id) 1)) :peak))
))))


  (q/with-translation [(+ 10 x) (+ 180 y) 200]
    (q/fill 255 0 0)
    (q/text-size 20)
    ;;(q/text (str (get @lastnote channel)) 0 0 )
    (q/text (str  "channel "channel" " (get audiochannel :freq) )  0 0 200))


 (q/perspective)
  )

(def titlestring [ "violin 1" "violin 2" "alto" "cello" "debug" ])
(defn debugstringtype [x y title]
  (q/ortho)
  (q/no-fill)
  (q/rect x y 300 200)
  (q/with-translation [(+ x 150) (+ y 100) 0]
    (q/with-rotation [@measure 1 1 0]
      (bps/cubeModule 0 0 0 (* @lasttype 50) 50 20 50 @lasttype)))
  (q/fill 0 255 0)
  (let [txt (nth titlestring @lasttype) ]
    (q/text (str txt " ") x (+ 189 y) ))
  (q/perspective)
  )

(defn debugstring [x y title type]
  (q/ortho)
  (q/no-fill)
  (q/stroke-weight 1)
  (q/stroke 255)
  (q/rect x y 300 200)
  (q/with-translation [(+ x 150) (+ y 100) 0]
    (q/with-rotation [2 1 1 0]
      (bps/cubeModule 0 0 0 150 50 20 50 type)))
  (q/fill 35 138 196)
  (let [txt (nth titlestring type) ]
    (q/text (str txt " ") (+ 10  x) (+ 189 y) ))
  (q/perspective)
  )


(def notestatistics (atom {}))
(defn fillnotestatistics []
  (reset! notestatistics {})
  (dotimes [c 16]
    (swap! notestatistics assoc (str "ch" c) {})
    (dotimes [n 128]
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
            (bps/module (* x size) (* y size) 0 n s  @lasttype))
          (q/text (str m) (* x 50) size  )
          )
        )
      )
    (q/perspective))
  )

(def audioREPL (atom false))
(defn audiodebugger [x y channels]
  (q/ortho)
  (let [a1 (get @(get channels 0) :peak)
        a2 (get @(get channels 1) :peak)
        a3 (get @(get channels 2) :peak)
        a4 (get @(get channels 3) :peak)
        a5 (get @(get channels 4) :peak)
        a6 (get @(get channels 5) :peak)
        a7 (get @(get channels 6) :peak)
        a8 (get @(get channels 7) :peak)
        f1 (get @(get channels 0) :freq)
        f2 (get @(get channels 1) :freq)
        f3 (get @(get channels 2) :freq)
        f4 (get @(get channels 3) :freq)
        f5 (get @(get channels 4) :freq)
        f6 (get @(get channels 5) :freq)
        f7 (get @(get channels 6) :freq)
        f8 (get @(get channels 7) :freq)
        b1 (mod (get @(get channels 0) :beatnumber) 8)
        b2 (mod (get @(get channels 1) :beatnumber) 8)
        b3 (mod (get @(get channels 2) :beatnumber) 8)
        b4 (mod (get @(get channels 3) :beatnumber) 8)
        b5 (mod (get @(get channels 4) :beatnumber) 8)
        b6 (mod (get @(get channels 5) :beatnumber) 8)
        b7 (mod (get @(get channels 6) :beatnumber) 8)
        b8 (mod (get @(get channels 7) :beatnumber) 8)
        a [a1 a2 a3 a4 a5 a6 a7 a8]
        b [b1 b2 b3 b4 b5 b6 b7 b8]
        f [f1 f2 f3 f4 f5 f6 f7 f8]]
    (if @audioREPL
      (println "debugging audio" "ch" a f b))
    (q/with-translation [x y 0]
      (dotimes [n (count a)]
       ; (println n a)
        (q/fill 0 (* (get f n) 255) 0)
        (q/rect 0 (* n 20) (* 120 (get a n)) 15))
      (q/fill 255)
      (q/with-translation [0 0 0]
        (dotimes [n (count b)]
          (dotimes [o (+ 1 (get b n))]
            (if (> o 5)
              (q/fill 255 0 0)
              (q/fill 255))
            (q/rect (* o -10) (* n 20) 6 15 ))))
      )
    )
 )
(defn emptydebug [x y z w h]
    (q/no-fill)
    (q/with-translation [x y z]
      (q/rect 0 0 w h)))

(def bootprocess [
                  "initializing RNN"
                  "enabling model"
                  "loading preseed variables"
                  "starting midi clock"
                  "establishing link"
                  "sending notes"
                  "booting string mode"
                  "getting errors from database"
                  "learning from previous mistakes"
                  "be more human"
                  "be more robot"
                  "be more robot"
                  "be more robot"
                  "be more robot"
                  "be more robot"
                  "be more robot"
    ])


(def bp (atom nil)) ;;; dirty workaround
(defn bootingdebug [x y z]
  (q/no-fill)
  (q/with-translation [x y z]
(q/fill 18)
    (q/rect 0 0 350 180)
    (q/fill 0 255 0)
    (q/text-size 25 )
    (if (= @bp nil)
      (q/text "booting atoms" 10 25)
      (q/text  (get  (get @bp (get @bp :active)) :phase) 10 25 )) ;;;@bp cannot yet be resolved at startup

    (q/text-size 10)
    (dotimes [k 5]
      (let [n (mod @absolutemeasure (- (count bootprocess) 5))]
        ;; (println n (nth bootprocess n))
        (q/text (nth bootprocess (+ n k)) 15 (* (+ 2 k) 25) )
        (q/text (str @main.botpop/lastnote)   15 (+ 10 (* (+ 2 k) 25)) )
        ))

    )

  )

(defn ewidebug [x y z]
  (q/ortho)
  (let [note1 (get @ewidata :note1)
        breath1 (get @ewidata :breath1)
        note2 (get @ewidata :note2)
        breath2 (get @ewidata :breath2)]
    (q/with-translation [x y z]
      (q/no-fill)
      (q/rect 0 0 300 200)
      (q/fill note1 0 128 255)
      (q/rect 20 20 breath1  20)
      (q/fill note2 0 128 255)
      (q/rect 20 50 breath2  20))
    )
  q/perspective
  )
