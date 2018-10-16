(ns main.core
  (:require [main.kaos]))

(defn draw [state]
  (q/background 20 33 44)
  (q/stroke  240 25 25)

  (dotimes [n 10]
    (addpill (* 700 n)  (* n -100) -10 20)
    )
  (updatepills)
                                        ;
    (let [n (get (:bd state) :note)
        vel (get (:bd state) :velocity)
        pos (nth (nth @midibd 2)  (.indexOf (nth @midibd 1) n) 1 )
        ]
      (addpill 0 200 0 (/ vel 5))
      (dotimes [i 1]
        (q/with-translation [(+ ( * 10  i) (:x pos) ) (-   (:y pos) 50) (:z pos )]
                                        ; (q/with-translation [ (*  (:tr state) 100) (* (* 5 (:tr state)) (:tr state)) -2000 ])
                                        ;  (q/stroke-weight (/ (:tr state) 20))
                                       ; (rotatedlines 2000 (* 50 (get (:sd state) :note)) state )
          (q/fill 55 (* 30 vel) (- 255 n )  vel)
          (q/with-rotation [vel 0 1 1]
            (q/stroke 255 255 0)
            (if (= vel 0)
              (q/stroke-weight 0)
              (q/stroke-weight 10))
            (q/box   (* 1 vel) (* n 2) 255 )

            ))




          ;(doboxes midibd state (* 20 (:tr state)))

          (q/with-rotation [1 1 1 0]
            (q/stroke-weight 1)
            (renderpills state)
            (dotimes [p 10]

              (clicktrack state))

                                        ;          (joywaves 2000 23 200)
            )




          )



                                        ;    (Println vel)
    )

    ;render mididebugtool here

(q/camera (/ width 2) (/ height 2) 20 (/ width 2) (/ height 2) 0  0 1 0    )
    (mididebugger state )
; (q/camera 1000 0 -200 0 (get (:pc1 state) :velocity) 100 0 1 0   )
    (.sendScreen @server)
  )
