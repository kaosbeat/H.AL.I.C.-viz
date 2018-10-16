(ns main.core
  (:require [main.kaos ]))

(defn draw [state]
  (q/background  0 20 0)
  ;(println state)
;  (println (get (:chords state) :note ))
  ;(q/with-translation [50 50 0])

                                        ;(drawinplace @midichords state)

    (wavelines 10 50 2 3 1 200 100 14 10 1000 100 10 )


  ;(wavelines 1500 6 6 4 22 0 120 0 100 500 280 2 )

  ;(wavelines 500 3 6 4 22 0 1200 0 100 500 28 2 )

 ;(addpill (q/random 920) 100 100  10)
  ;(addwavemountain 0 0 0 [1 2 (/  (get (:pc1 state) :velocity) 20) 4 (q/random 0 20) 8 (/  (get (:bd state) :velocity) 8) 8] (get (:bd state) :velocity) )
  (addpill (* (* 2 (mod4)) (get (:ld1 state) :note)) (* 2 5) 10  10)
  (updatepills) ;;;pillstate is a bit boring at the moment....
  ;(updatewavemountains)
                                        ;(q/with-translation [50 50 (* 0 (get (:ch state) :pan ))])

  (q/with-rotation [ (get (:bd state) :velocity ) 1 1 0]
    (q/stroke (get (:sd state) :velocity))
   ; (renderpills state)

  ; (wavefield 100 1920 1500)

    )
  (q/stroke-weight 1)
  (q/fill (get (:ch state) :note) 25 0 120)
    ;(rendermountains state)
                                        ;(q/with-rotation [(:tr state ) 0 1 1])
    (q/stroke 255 (get (:ld1 state) :note) 0)
    ;(thosesquares (* 1 (get (:pc1 state) :velocity)) 50 (* 20 (get (state :pc1) :note)) state )







;(drawinplace @midichords state)
                                        ;(q/fill 255 255 255 )
    (q/stroke-weight (get (:bd state) :velocity)
                     )
    (q/fill 255 55 0 120)
  ;  (wavemountain [0 5 (q/random 0 10) 2 10 5 2 1 5] 1920 1500)
(q/stroke 34 23 55)


;(q/with-translation [0 100 1000])
;(doboxes midild1 state 20)

(q/with-translation [-300  (* (:tr state) 100) -100]
  ;(clicktrack state)
  )


(q/with-rotation [ (get (:sd state) :velocity) 0 1 0]
 ; (rotatedlines 2000  (* (get (:bd state) :note) 1) state)
  )
(mididebugger state)
                                        ;(q/camera 0 100 (* 100 (:tr state)) 0 (get (:bd state) :velocity) -100 0 1 0)
(q/camera)
  (.sendScreen @server))
