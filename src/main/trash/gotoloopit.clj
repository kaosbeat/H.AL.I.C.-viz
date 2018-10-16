(ns main.core
  (:require [main.kaos ]))

(defn draw [state]

  ;(println state)
  (q/stroke-weight 3)
  (q/fill 23 45 3 22)
  (q/background 23 45 30)
(dotimes [ i 10 ]


  (let [n (get (:bd state) :note)
        vel (get (:bd state) :velocity)
        pos (nth (nth @midibd 2) (.indexOf (nth @midibd 1) n) ) ]
                                        ; ( println pos)

    (q/with-translation [(:x pos) (:y pos) (:z pos )]
      (q/fill 255 0 0 )
     (q/box (* 30 vel))
      (q/with-rotation [ (* i 10) 1000  i  0 ]

        (bassdrum state)
                                        ;      (clicktrack state)
        )

      )


                                       (addwavemountain -400 -400 0000 (mountainvector) (get (:bd state) :velocity )  )
                                        (updatewavemountains)
                                        (q/stroke 25 (q/random 245) 0)
                                        (q/no-fill)
                                        (rendermountains state)


    ))


(dotimes [p 10]
  (q/with-translation [100 (* 100  p) 100 ]
    (dolines 10 0 -20 ( * (:tr state ) 200) 200 0 10 23 43 200000000000000 1 123 345  1)
      (dotimes [q 10]
        (q/with-rotation [(* (:tr state) 0)  0 10 0]
          (q/with-translation [(* p 100 ) 100 100]
            (q/box (* (get (:ld2 state) :note) 10))
            )
       ))
      )
    )
(q/camera 0 0 -2000 0 0 0 0 1 0)
(.sendScreen @server )

)









  ;; (println pos)

  ;;(println (:bd  state))
