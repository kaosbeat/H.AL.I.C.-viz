(ns main.core
  (:require [main.kaos ]))


(defn draw [state]
  (q/background  (get (:bd state) :note) 20 0)
                                        ;(println (:lines state))

                                        ; (addpill (* 50 (get (:ld1 state) :note)) (* -20 (get (:ld1 state) :velocity)) 0  200)
;(let [pos ( (.indexOf (nth @midibd 1) (get (:bd state) :note)) )])

  (let [n (get (:bd state) :note)
        vel (get (:bd state) :velocity)
        pos (nth (nth @midibd 2)  (.indexOf (nth @midibd 1) n) 1 )
        ]

    (q/with-translation [(:x pos) (:y pos) (:z pos )]

      (q/fill 255 0 0 23)
     ; (circlejoy state)
      )
     ; (addwavemountain 0 0 0 (mountainvector) vel )
;    (println vel)
    )

  (q/fill 55 255 0 10)
  (q/stroke-weight 0)
  (updatewavemountains)
  (rendermountains state)

   ;;almost there!
  ;(take 5 (iterate (partial randseed 1) 30))

  ;(println (mecsize 10 10))
  (dotimes [i 10]
    (q/stroke 0)
    (q/with-translation [(* i 100) 0 0 ]
      (q/stroke 34 56 0)
      (drawgeomec 0 0 -1000 (* (nth mecsize1 i) (* (mod4) 100 )  ) (nth mec i) (mod2))
      (q/stroke 255 255 0))
                                        ;      (drawgeomec 0 0 -600 (* i (q/random 30)) (+ 1 (mod2) ) 0)

    )

  (q/with-rotation [300])
  (q/with-translation [ 0 ( + 50 (* (* 4 (tr)) 10)) 0]
    (q/fill 255 255 0)
    (q/stroke-weight 10)
    (q/stroke 255 0 0)
    (dotimes [n 23]
      (q/rect 0 300 width (* (get  (:bd state) :velocity) n))))


; (q/camera)
;(q/camera (/ width 2) (/ width 2) -1000 0 0 0 0 1 0)
;(q/perspective (/ 3.14 2.0) (/ width height) 10 -100)
;(q/perspective)
(.sendScreen @server))
