(ns main.instruments.bpstrings
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]
   [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
   [main.botpop :refer [ch1 ch2 ch3 ch4 ch5 ch6 ch7 ch8]]

   [main.instruments.bpstrings :as bps]))


(def viz (atom []))
(def vizbiz (atom []))
(def vizcount (atom []))
(def vizbizcount (atom []))
(def ewiviz (atom []))
(def ewivizcount (atom []))
(def ewisecviz (atom []))
(def ewisecvizcount (atom []))
(def rendering (atom false))
;; (def lasttype (atom 0))


(def status (atom {:debug true :violin1 true :size3D :5}))
(def params (atom {:p1 64 :p2 64 :p3 64 :p4 64 :p5 64 :p6 64 :p7 64 :p8 64
                   :q1 64 :q2 64 :q3 64 :q4 64 :q5 64 :q6 64 :q7 64 :q8 64
                   :b1 false :b2 false :b3 false :b4 false :b5 false :b6 false :b7 false :b8 false} ))
(def dynamics (atom {:bootphase 0 :hfreq1 1200 :hfreq2 600 :hfreq3 300 :hfreq4 200 :nfreq1 0 :nfreq2 0 :nfreq3 0 :nfreq4 0 :amp1 0 :amp2 0 :amp3 0 :amp4 0} ))

(def vizbizsize (atom 1))
(defn fillvizbiz [size3D]
  (reset! vizbiz [])
  (dotimes [x size3D]
    (dotimes [y size3D]
      (dotimes [z size3D]
        (reset! vizbizsize size3D)
        (swap! vizbiz conj {:x x :y y :z z :ttl 10 :type 4})
        ))))

(fillvizbiz 2)

(defn resetviz [message]
  (reset! viz [])
  (println message)
  )


(def radrot (seq->stream (cycle-between 0 6.2830 0.01 6.2830))  )

(defn module [x y z seed p1 type]
;  (println p)
  (q/noise-seed seed)
  (q/stroke-weight 0.5)
  (let [a (get @ch3 :peak)
        b (get @ch4 :peak)
        c (get @ch5 :peak)
        d (get @ch6 :peak)
        peak [a b c d]
        p2 (get @params :p2)
        p3 (get @params :p3)
        size p1

        layers (int (* p3 (nth peak type )))]
    (case type
      0 (q/fill 23 25 250 (* 2 p2))
      1 (q/fill 255 25 0 (* 2 p2))
      2 (q/fill 0 225 250 (* 2 p2))
      3 (q/fill 255 255 0 (* 2 p2))
      (q/fill  255 (* 2 p2)))
    (dotimes [n layers]
      (let [sizex (* size (q/noise (* n 0.2)))
            sizey (* size (q/noise (* n 0.3)))]
;        (q/with-rotation [(main.botpop/pirad) 1 1 0])
        (q/with-translation [ (/ sizex 2) (/ sizey 2) (* n 9)]

          (q/box sizex sizey 0))


))
    (q/with-translation [0 0 50]
      (q/text-size 12)
      (q/fill 255 0 0)
      (q/text (str "seed " seed ) 0 0))
    )

  )
(defn module2 [x y z seed peak]
  ;(println peak)
  (q/noise-seed seed)
  (q/stroke-weight 0.5)
  (let [p1 (get @params :p1)
        p2 (get @params :p2)
        p3 (get @params :p3)
        size p1
        layers (int (* p3 (* peak peak)))]
    (q/fill 255 (* 2 p2))
    ;(println layers)
    (dotimes [n layers]
      (let [sizex (* size (q/noise (* n 0.2)))
            sizey (* size (q/noise (* n 0.3)))]
;        (q/with-rotation [(main.botpop/pirad) 1 1 0])
        (q/with-translation [ (/ sizex 2) (/ sizey 2) (* n 9)]

          (q/box sizex sizey 0))


))
    (q/with-translation [0 0 50]
      (q/text-size 12)
      (q/fill 255 0 0)
      (q/text (str "seed " seed ) 0 0))
    )

  )


(defn draw [x y z ttl type seed]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)
  (q/with-translation [x y z]
    (q/fill 255 255 255 ttl)
    ;(q/box (* type  50) 50 0)
    (q/perspective)
    (q/with-translation [0 (- (* 3  seed) 150) 0]

      (module x y z seed (get @params :p1) type))

  )
  )

(defn printrandomstuff [seed]
  (q/random-seed seed)
  (println "---------------------")
  (dotimes [n  5]
    (println (q/random 100))))


(defn cubeModule [x y z w h d ttl type ]
  (let [colors [(q/color 0 0 250 150)
                (q/color 55 0 155 255)
                (q/color 25 233 334 154)
                (q/color 255 225 5 105)
                (q/color 0 255 255 128)]]
    (q/fill (nth colors type )))
  (if (= type 4) (q/no-fill))
  (q/random-seed (* type 513))

  (q/with-translation [x y z]
    (let [a (/ (q/random 240) 1)
          b (/ (q/random 240) 1)
          c (/ (q/random 240) 1)
          d (/ (q/random 240) 1)
          e (/ (q/random 240) 100)
          p (get @(get main.botpop/channels (+ type 2)) :peak)
          r1 (get @ch4  :beatnumber)
          ;r1 3
          ]

        (q/with-rotation [e 1 0 0]
          (q/box (* a p)(* b p)(* c p))
          (q/stroke 255 0 0 )
          (q/stroke-weight  (* 0.1 (mod r1 8)) )
          (q/box   (* p w)  (* p h) (*  p  d))
          (q/with-rotation [r1 1 0 1]
            (dotimes [ n 1]
              (q/stroke 0 255 0)
              (q/with-translation [(* n 290 ) 20 20]
               ;(q/box   (* 1 (* c h)) (/ (* 1 (get @main.botpop/ewidata :breath1)  (* a w)) 100) (*  e (* 3 (get @main.botpop/ewidata :breath1))))
                )))

          (q/with-rotation [0 1 0 1]
            (dotimes [ n 1]
              (q/stroke 0 0 255)
              (q/with-translation [(* n -90 ) 40 20]
               ; (q/box   (* 1 (* c h)) (/ (* 1 (get @main.botpop/ewidata :breath1)  (* a w)) 100) (*  e (* 3 (get @main.botpop/ewidata :breath1))))
                )))
          )))
  )

;;; render datafeedercube
(def rotoff (atom 0.0))
(def kickspace (atom 0.0))
(defn updatekick []
(swap! kickspace * 0.95)
  )


(defn renderCube [x y z]
  (q/with-translation [x y z]

    (q/with-rotation [(* (+ @rotoff (radrot)) 1) (if (get @params :b1) 1 0) (if (get @params :b2) 1 0) (if (get @params :b3) 1 0) ]
      (let [size      50
            space     (+ 200 (* @kickspace 30))
            order     @vizbizsize
            rotoffset (- 0 (/(* order space)2 ))]
        (q/with-translation [rotoffset rotoffset rotoffset ]
          (dotimes [n (count @vizbiz)]
            (let [x    (get (nth @vizbiz n) :x)
                  y    (get (nth @vizbiz n) :y)
                  z    (get (nth @vizbiz n) :z)
                  ttl  (get (nth @vizbiz n) :ttl)
                  type (get (nth @vizbiz n) :type)
                  ydiv (/ (q/height) (+ 1  n))

                  ]
              (q/stroke-weight 1)
              (cubeModule (* 1 (* space  x)) (* 1 (* space  y)) (* 1 (* space z)) (* type size) size size ttl type)
              (q/stroke-weight 1.5)
              (q/stroke 255)

              (q/line (* 1 (* space  x)) (* 1 (* space  y)) (* 1 (* space z))  10000 (* n ydiv) 10000  )
              ;(print (q/random 1))

              ))))
      (if (> 10 (q/random 1))
        (do (print "doing lines")
            (let [space (+ 200 (* @kickspace 30))
                  ]
              (dotimes [n (count @vizbiz)]
;                (q/line (* 1 (* space  x)) (* 1 (* space  y)) (* 1 (* space z))  10000 (* n ydiv) 10000  )
                )

              )
            )
        (+ 1 1))
      )
    ))


(defn stringDebugannotateframe []
  (let [x (get @main.botpop/cubetween :x)
        y (get @main.botpop/cubetween :y)
        z (get @main.botpop/cubetween :z)]
    (q/stroke 0 255 0)
    (q/stroke-weight 3)
    (q/no-fill)
    (q/rect (- x 250) (- y 220) 300 300 )
    (q/line (- x 250) y z  320 (+ (* 100 (get @(nth main.botpop/channels @main.botpop/lasttype) :peak))  (* 220 @main.botpop/lasttype)) 0)
    ;(q/text "converting RNN outputs" (- x 230) (- y 60))
    (q/text "cubeView Called" (- x 230) (- y 60))
    )
  )

(defn cubeDebugannotateframe []
  (let [x (get @main.botpop/cubetween :x)
        y (get @main.botpop/cubetween :y)
        z (get @main.botpop/cubetween :z)]
    (q/stroke 0 255 0)
    (q/stroke-weight 3)
    (q/no-fill)
    (q/rect (- x 250) (- y 220) 300 300 )
    (q/line (- x 250) y z  320 (+ (* 100 (get @(nth main.botpop/channels @main.botpop/lasttype) :peak))  (* 220 @main.botpop/lasttype)) 0)
    ;(q/text "converting RNN outputs" (- x 230) (- y 60))
    (q/text "cubeView Called" (- x 230) (- y 60))
    )
  )


(defn cubeView []
  (let [x (get @main.botpop/cubetween :x)
        y (get @main.botpop/cubetween :y)
        z (get @main.botpop/cubetween :z)]

    (renderCube x y z)
   ;; (println "cubeView Called" x y z )
    )
  )



(defn renderStringNotes []
  ;;; render stringnotes
  (dotimes [n (count @viz)]
    (let [x    (get (nth @viz n) :x)
          y    (get (nth @viz n) :y)
          z    (get (nth @viz n) :z)
          ttl  (get (nth @viz n) :ttl)
          type (get (nth @viz n) :type)
          seed (get (nth @viz n) :seed )]
         (draw x y z ttl type seed)
        )
    )
  )

(defn renderEwi  []
  (dotimes [n (count @ewiviz)]
    (let [x    (get (nth @ewiviz n) :x)
          y    (get (nth @ewiviz n) :y)
          z    (get (nth @ewiviz n) :z)
          type (get (nth @ewiviz n) :type)
          note (get (nth @ewiviz n) :note)
          ttl  (get (nth @ewiviz n) :ttl)
          cnt  (get (nth @ewiviz n) :count)
          b1   (get @main.botpop/ewidata :breath1)
          b2   (get @main.botpop/ewidata :breath2)
          q1   (get @params :q1)
          q2   (get @params :q2)
          w    (/  (* cnt q1) (+ 1 q2))
          ]

      (q/with-translation [(+ 0 x) y z]
        (q/with-rotation [1.64 0 1 0]
          (q/fill 255)
          (q/text-size 50)
          (q/text (str  cnt) 100 100)
          (dotimes [n 1]
            (q/with-rotation [(main.botpop/pirad) 1 0 0]
              (q/with-rotation [note 1 0 0]

                (dotimes [m 100]
                  (if (<  (* m 20) cnt)
                    (q/with-translation [(* m 20) 0 0]
                                        ;(q/random-seed m)
                      (q/box 1 1 (q/random  220) ))))
                (q/with-translation [(/ w 2) 0 0 ]
                  (q/fill 255 ttl)
                  (q/box  w 50  50 ))
                )))))))

  )

(defn ewiView [x y z]
  (q/with-translation [x y z]
    (renderEwi) )
  )







(defn addewi [note type]
  (let [x (rand-int 1900)
        y  (rand-int 1000)
        z 0
        type type
        note note
        ttl 130
        ]
    ;(println  x y z)

    (swap! ewiviz conj {:x x :y y :z z :type type :note note :ttl ttl :count 0 })
    )
  )




(defn removeewi [note]
  (reset! ewiviz [])
  ;(swap! ewiviz #(remove (fn [ewinote] (= (:note ewinote) note)) %))
  ;(reset! ewiviz (into [] ewiviz))
  )




(defn add [type note]
  (let [x 300
        y (* 200 (+ 1 type))
        z 0
        ttl 100
        note note
        type type
        a (get @ch4 :peak)
        b (get @ch5 :peak)
        c (get @ch6 :peak)
        d (get @ch7 :peak)
        p [a b c d]
        ]
    ;(println  p)
    (if (= 0 (count @viz))
      (reset! viz []))
    (swap! viz conj {:x x :y y :z z :ttl ttl :seed note :type type :p (nth p type)})
  )
)

(defn updateviz []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (reset! vizbizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      (do
        ;;(swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :x] (fn [x] (+ 5  (+ x (/ (get @params :p4) 4)))))
        (if (> (get  (get @viz n) :x) 1500)
          ;;(println "fgound one")
          (do
            (if (= 0 (count @vizbiz))
              (reset! vizbiz []))
            (let [m (rand-int  (count @vizbiz))
                  ;ob (update-in (get @vizbiz m) [:type] (get (get @viz n) :type))
                  ]
              (swap! vizbiz assoc-in [m :type] (get (get @viz n) :type))
              (reset! main.botpop/lasttype (get (get @viz 0) :type))
             ;; (println vizbiz)
              )
            ;; (println "adding here")
            (swap! vizcount conj n)
            ) ;else mark pill for deletion
          )
        )
      )
    )
  (dotimes [n (count @vizcount)]
    ;;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz))
    )

  (dotimes [n (count @vizbiz)]
    (if (false? (= 0 (get (get @vizbiz n) :ttl)))
      (do
      ;;  (swap! vizbiz update-in [n :ttl] dec)
      ;;  (swap! vizbiz update-in [n :y] (fn [y] (+ y 1)))
        )
    ;;  (swap! vizbizcount conj n) ;; not deteing themn even if ttl = 0
     )
   )
  (dotimes [n (count @vizbizcount)]
    ;;    (println " really dropping stuff")
   ;; (reset! vizbiz  (drop-nth (nth @vizbizcount n) @vizbiz))
    )
  )

;(defn channel [channel] (swap! channel assoc :vizsynth add :render renderStringNotes :update updateviz))


(defn updateewistream []
  (reset! ewivizcount [])
  (dotimes [n (count @ewiviz)]
    (let [x (get (get @ewiviz n) :x)
          y(get (get @ewiviz n) :y)
          z (get (get @ewiviz n) :z)
          type (get (get @ewiviz n) :type)
          note (get (get @ewiviz n) :note)
          note1 (get @main.botpop/ewidata :note1)
          note2 (get @main.botpop/ewidata :note2)
          ]
      (if (false?  (and (= 1 type) (= note note1)))
        (swap! ewiviz update-in [n :ttl] dec)
        (swap! ewiviz update-in [n :count] (fn [x] (*  (+ x (get @main.botpop/ewidata :breath1)) 1 )))
        )
      (if (false?  (and (= 2 type) (= note not)))
        (swap! ewiviz update-in [n :ttl] dec)
        (swap! ewiviz update-in [n :count] (fn [x] (*  (+ x (get @main.botpop/ewidata :breath1)) 1 )))
        )

      (if (> 0 (get (get @ewiviz n) :ttl))
        (do
          (swap! ewivizcount conj n)
          )

        )

     ; (if (= 0 (mod  (get (get @ewiviz n) :count) 10 )) (addewisec x y z))
      ;(if (false? (= 0 (get (get @ewiviz n) :type 0))))

      )
    )

  (dotimes [n (count @ewivizcount)]
       ; (println " really dropping stuff")
    (reset! ewiviz  (drop-nth (nth @ewivizcount n) @ewiviz))

    )
  )
