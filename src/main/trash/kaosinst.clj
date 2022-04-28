(ns main.kaos )



(defn bassdrum [state ]
  (q/stroke 255)
  (q/stroke-weight 12)
  (q/fill (* 2 (get ( :bd state) :pan )) 10 10 )
  (dotimes [p 6]
    (q/with-translation [ (* p 300) (q/random 10 90)  (* 100 (mod4))]
      (q/box (* 3 (get (:bd state) :note)) )
      (q/with-translation [0 (* (mod8) 100)  0]
        (q/box (* 3 (get (:bd state) :note)) )))
    )

  )


(defn bassdrum [state]
  (q/box 50)
  )




(defn clicktrack [state]
  (q/stroke-weight (get (:sd state) :velocity))
  (q/stroke 100 50 210)
  (q/line (* (get (:ch state) :pan) 10) (* 1200 (q/noise (q/random 0.5))) 1920 (* 5000 (q/noise (q/random 20))) )
  )



(def linesdyn (atom [0 1 2 7 4 5 -1 -4 1 0]))
(defn dynlines [state]
  (q/stroke (* 15 (tr)) 255 255 128)
  (q/stroke-weight 10)
  (dotimes [p 10]
          (q/with-translation [(* p (tr)) 0 0 ]
            (dotimes [n (-   (count @linesdyn) 1)]
              (q/with-rotation [ (q/random 0 0) 1 0 0]

                (q/line (* n 10) (* (/ (q/height) (count @linesdyn)) n) (* -300 (get @linesdyn n))
                        (* n 10) (* (/ (q/height) (count @linesdyn)) (+ 1 n)) (* (* (mod4) -300) (get @linesdyn n)))
                (q/line (* n 10) (* (/ (q/height) (count @linesdyn)) (+ 1 n)) (* -300 (get @linesdyn n))
                        (* n 10) (* (/ (q/height) (count @linesdyn)) (+ 1 n)) (* -300 (get @linesdyn (+ 1 n)))
                        ))
                                        ;(q/line (* n  100) 0 (* n 50)  1000)
              )))
  )

;;(event-debug-off)

;;(tr)


(defn tester  [bottomsegments minbottom maxbottom waveint]
  (range minbottom (rand-int maxbottom ) waveint ))

(defn tester []
  (fn this [a b] (+ a b) ))

(defn tester [start end step]
  (concat start  (range  (last start) end step))
  )

;( take 3 (iterate  (tester [1] 20 2)) 3)

;(tester 1 5 200 3)

(defn tester []
  (let [x [0 200 150 500 200 0]
        min 0
        max 300
        ]
    (dotimes [n 6]

      )
    )
  )





(defn wavelinesOLD [wavewidth waveint topsegments bottomsegments starttop mintop maxtop startbottom minbottom maxbottom color offset]


  (q/stroke-weight 1)
  (let [top [starttop]
        bottom [startbottom]
        ]
    (dotimes [t topsegments]
      (let [x (q/random mintop maxtop)]
        ;(println)
        (into [] (concat top (range (last top) x waveint )))

        )

      )


    (dotimes [b bottomsegments]
      (let [x (q/random minbottom maxbottom)]
        ( into [] (concat bottom (range (last bottom) x waveint )))
        ))


    (dotimes [n 10]
      (q/line (* n 5) (nth top 0)
              (* n 5) (nth bottom 0)
              )
      ))

  )


(defn segment [size space starttop stoptop startbottom stopbottom]
  (let  [top (range starttop stoptop (/ (- stoptop starttop) size ))
         bottom (range startbottom stopbottom (/ (- stopbottom startbottom) size ))
         ]
       (dotimes [n size]
         (q/line
          (* n space) (nth top n)
          (* n space) (nth bottom n)
          )


         ))

  )

(defn wavelines [wavewidth waveint topsegments bottomsegments starttop mintop maxtop startbottom minbottom maxbottom color offset]

  (q/stroke-weight 2)
  (q/with-translation [(+ (mod4) 500 ) 200 0]
    (q/stroke color color 34)
    (segment 100 (/  (1) 5) (* 4 (1)) (* 100  (get @ch1 :peak)) (* 1 300) 90))
  (q/stroke 55 13 252)
  (segment 100 10 100 1000 300 90)
)



(defn wavelines [wavewidth parts test bottomsegments starttop mintop maxtop startbottom minbottom maxbottom color offset]
  ;(wavelines 100 5 2 3 1 200 1200 14 10 1000 10 10 )
  (q/with-translation [(+ 300 offset) 300 0]
    (q/stroke 0 (q/random 250) color)
    (dotimes [n parts]
      (segment (* (+ n 1) (/ wavewidth parts)) n (* 4 (get @bd :note)) (* 30 (get @sd :note)) 300 90 )
      ))


  )

(defn joywaves [wavewidth parts top]
;  (q/stroke 255)
  (dotimes [n parts]
    (segment (* (+ n 1) (/ wavewidth parts)) n (nth top (mod parts (count top))) (* 30 (get @sd :note)) 300 90  )
    ))







(defn circlejoy [state]
  (dotimes [n (mod16)]
    (q/with-translation [ 0 0 (* -300 (+ 0 n))]
      (q/ellipse 0 0 (* n 200) (* n 200))))

  )


(def kick (atom [0 1 0]))
(reset! kick [0 5 2 6 3 3.4 1 0 2 10 2 20 39 3 4  ] )
(reset! kick [0 1 5 6 4 6  ] )


;define structure to hold pills
(def pills (atom [{:size 10 :x 100 :y 100 }  {:size 20 :x 200 :y 200 }  ]))
(reset! pills [])
                                        ;addpill to atom

(defn addpill [x y z ttl]
  (if (= 0 (count @pills))
    (reset! pills []))
  (swap! pills conj {:x x :y y :z z :ttl ttl })
  )

(addpill 250 500 312 122 )
(addpill 25 2 30 35 )
(addpill 25 3 0 42  )
(addpill 25 4 0 50 )

(def pillcount ( atom []))

(defn updatepills []
  ; for some reason not all pills are deleted
  (reset! pillcount [])
  (dotimes [n (count @pills)]
    (if (false? (= 0 (get (get @pills n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! pills update-in [n :ttl] dec)
        (swap! pills update-in [n :z] (fn [x] (rand-int -670)))
        )
      ;else mark pill for deletion
      (swap! pillcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @pillcount)]
;    (println " really dropping stuff")
    (reset! pills  (drop-nth (nth @pillcount n) @pills)))
  ;(println @pills)
  )

;(updatepills)
(defn renderpills [state]
  (q/no-stroke)
  (dotimes [n (count @pills)]
    (q/with-translation [(get (nth @pills n) :x) (get (nth @pills n) :y) (get (nth @pills n) :z) ]
      (q/fill (* 4 (get (nth @pills n)  :ttl)))
      (q/ellipse 0 0 50 50)
      (q/rect 0 -25 50 50)
      (q/ellipse 50 0 50 50)))
  (q/stroke 255)
  )



(defn renderpills [state]

(dotimes [n (count @pills)]
    (q/with-translation [(get (nth @pills n) :x) (get (nth @pills n) :y) -100]
      (q/fill 24 130 23 (* 4 (get (nth @pills n)  :ttl)))
      (if (= 0 (mod2))
        (q/box 20 20 1000)
        (q/box 20 1000 20))
      ))

)

(def trans (atom 0) )
(defn renderpills [state]
(let [chord  (get (:chords state ) :note) ]
   ;; (println chord)
    (case chord
      44 (reset! trans 200 )
      46 (reset! trans 0 )
      47 (reset! trans  40)
      49 (reset! trans 700 )
      52 (reset! trans  340)
      (reset! trans 0 )
      )
    )
(q/with-translation [0 @trans 0]
  (dotimes [n (/  (count @pills) 1)]
    (q/with-translation [(get (nth @pills n) :x ) (get (nth @pills n) :y )  (* 10  (get (nth @pills n) :z ))]
      (q/fill (q/random 245) 23 (q/random 23) 100)
      (q/stroke-weight (q/random 10))
      (if (= 0 (mod n 2))
        (q/box (* 5 (get (:sd state) :note) ) @trans 40 )
        (q/with-rotation [(* 40 (q/random 20 ))  1 1 0 ]
          (q/box (q/random 500) (* (mod16) 100) 40 ))
                                        ;(q/box (* 100 (get (:ld1 state) :note )) 50 50)
        )
      )
    ))
)

(def transfield (atom []))
(defn wavefield [posy width heigth]
  (let [ w (/ width 12)
         h (/ heigth 20)]
                                        ;l;  (q/stroke-weight 10) ;
       (q/begin-shape)
       (dotimes [n 8]
         (case n
           0 (q/vertex 0 0)
           1 (q/vertex (* w 2) (* h (q/random 10 20)))
           2 (q/vertex (* w 4) (* h (q/random 0 10)))
           3 (q/vertex (+ (-  (q/random w) (/ w) 2) (* w 5)) (* h (q/random 0 5)))
           4 (q/vertex (+ (-  (q/random w) (/ w) 2) (* w 6)) (* h (q/random 0 5)))
           5 (q/vertex (* w 7) (* h (q/random 0 10)))
           6 (q/vertex (* w 9) (* h (q/random 10 20)))
           7 (q/vertex (* w 11) 0)
           )
;         @transfield
         )
       (q/end-shape)
       )
  )

;(wavefield )


(defn doline [p1 p2]
  (q/line p1 p2))
(defn sixtyliner [p1p2 & args]
 ; (println p1p2 )
  ;; (println (first (into [] args)))

  (let [ [x1 y1 x2 y2] (first (into [] args))]
    (q/with-rotation [x2 0 1 1 ]
      (q/line x1 y1 x2 y2 ))
    )
  )
(defn dolines [x y z width height frame size res noisetop noisebottom colorshift r g b ]
  (q/stroke-weight 1)
  (if (= 0 colorshift)
    (q/stroke r g b)
    (q/stroke (* 15 (tr))))
  (q/with-translation [x y z]
    (if (= frame 0) (q/rect 0 0 width height))
    (reduce sixtyliner (into [] (map (fn [a]
                                        ;;(println (q/noise (* a noisetop)))
                                          [(* width  (q/noise (* a noisetop))) 0 (* width (q/noise (* a noisebottom))) height])
                                        (range 0 size (/ size res))))))


)
(defn drawinplace [midichannel state]
 ; ( let [x (((midichannel 2)  (.indexOf (midichannel 1) (get (state ((midichannel 0) :channel) ) :note) )) :x )])
  (println (get (:bass state)  :note)  )
  (q/with-translation [ 10 200 0]
    (q/box 200)))



(defn drawboxinplace [midimapatom midimapmap state]
  (let [midimin 67 ;(apply min midimapatom)
        midimax (apply max midimapatom)]

    (dotimes [n (count midimapatom)]
      (q/with-translation [(get (nth midimapmap n) :x) (get (nth midimapmap n) :y) (* -1  (get (nth midimapmap n) :z)) ]
        (q/box 100 ))))
  )


(defn doboxes [midimapatom state size]
  (dotimes [n (count (@midimapatom 1))]
    (if (= ((@midimapatom 1 ) n )  (get (state ((@midimapatom 0) :channel) ) :note))
      (do
        (q/fill 255 255 255 120)
;        (addpill (* n (/ width (count (@midimapatom 1)))) 200 -200 30 )
        )
      (q/fill 255 0 255 120)
      )

    (q/with-translation [(* n (/ width (count (@midimapatom 1)))) 200 -200 ]
      (q/box size))
   ;; (println ((@midimapatom 1) n))
    )
  )



(defn thatsquare [w h]
  (q/rect 0 0 w h)
  (h)
  )
(def color1 [255 0 0 120])
(defn thosesquares [w h size state ]
  (q/fill (color1 0) (color1 1) (color1 2) (color1 3))
  (q/rect 0 0 w h)
  (dotimes [n size]
    (q/rect 0 (* n (* n (/ h size))) w (* n (/ h size)))
    )

  )


(defn rotatedlines [radius density]
;  (q/stroke 255)
  (dotimes [n density]
    (q/line 0 0 (* radius (q/cos n)) (* radius (q/sin n)) )
    )
  )




(defn linesquare [w h horfillrate verfillrate ]

  (dotimes [n ( + 1 (* w horfillrate)) ]
    (let [
          x1 (* n (/ w  (* w horfillrate)))
          y1  0
          x2 x1
          y2 h]
      (q/line x1 y1 x2 y2 )
      (q/fill 125 23 0 12)
      (q/with-rotation [ (* n 10) 0 1 (* n 0.1)]
        (q/box (* n 5) ))
      )
    (q/line w 0 w h)

    )
  )
