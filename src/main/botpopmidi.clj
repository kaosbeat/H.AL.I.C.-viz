(ns main.botpop
  (:require [main.instruments.bpstrings :as bps]
;;            [quil.core :as q]
            ))


(on-event [:midi :timing-clock]
          (fn [e]
            (println e)
            ;(println  (:channel e) )
            ;(print " ")
             ;(print (:note e) )
            ;(print " ")
            )
          ::clock-handler
          )


(on-event [:midi :midi-time-code]
          (fn [e]
            ;(println e)
            ;(println  (:channel e) )
            ;(print " ")
            ;(print (:note e) )
            ;(print " ")
            )
          ::clock-handler2
          )
(remove-event-handler ::clock-handler)
(remove-event-handler ::clock-handler2)


(on-event [:midi :note-on]
          (fn [e]
              (let [note (:note e)
                    vel  (:velocity e)
                    channel (:channel e)]

                ;(println "note " note "channel " channel)
                (swap! notestatistics update-in [(str "ch" channel) note] inc)
                (if @midiREPL
                  (println note vel channel))
                (case channel
                  0 (do
                      (swap! midid1 assoc :velocity vel :note note :beat (inc (get @midid1 :beat)))
                      (reset! bps/rotoff (/ (rand-int 100) 100 ))
                      (reset! bps/kickspace 2.0)
                      (swap! lastnote assoc :ch0 note)
                      )
                  1 (do
                      (swap! midid2 assoc :velocity vel :note note :beat (inc (get @midid2 :beat)))
                      (swap! lastnote assoc :ch1 note)
                      )
                  2 (do
                      (swap! midid3 assoc :velocity vel :note note :beat (inc (get @midid3 :beat)))
                      (swap! lastnote assoc :ch2 note)
                      )
                  3 (do
                      (swap! midid4 assoc :velocity vel :note note :beat (inc (get @midid4 :beat)))
                      (swap! lastnote assoc :ch3 note)
                      )
                  4 (do
                      (swap! lastnote assoc :ch4 note)
                      (bps/add 0 note) ;; type 0 = violin

                      )
                  5 (do
                      (swap! lastnote assoc :ch5 note)
                      (bps/add 1 note) ;; type 1 = violin

                      )
                  6 (do
                      (swap! lastnote assoc :ch6 note)
                      (bps/add 2 note) ;; type 2 = alto

                      )
                  7 (do
                      (swap! lastnote assoc :ch7 note)
                      (bps/add 3 note) ;; type 3 = cello

                      )
                  8 (do
                      (swap! lastnote assoc :ch8 note)
                      )
                  9 (do
                      ;(println "lets add ewi")
                      (swap! ewidata assoc :note1 note)
                      (bps/addewi note 1)
                      (swap! lastnote assoc :ch9 note)
                      )
                  10 (do
                       (swap! ewidata assoc :note2 note)
                       (bps/addewi note 2)
                       (swap! lastnote assoc :ch10 note)
                       )
                  13 (do
                       (if (= note 41)
                         (swap! bps/params assoc :b1 (not (get @bps/params :b1))))
                       (if (= note 42)
                         (swap! bps/params assoc :b2 (not (get @bps/params :b2))))
                       (if (= note 43)
                         (swap! bps/params assoc :b3 (not (get @bps/params :b3))))
                       (if (= note 44)
                         (swap! bps/params assoc :b4 (not (get @bps/params :b4))))
                       (if (= note 57)
                         (swap! bps/params assoc :b5 (not (get @bps/params :b5))))
                       (if (= note 58)
                         (swap! bps/params assoc :b6 (not (get @bps/params :b6))))
                       (if (= note 59)
                         (swap! bps/params assoc :b7 (not (get @bps/params :b7))))
                       (if (= note 60)
                         (swap! bps/params assoc :b8 (not (get @bps/params :b8))))
                       (if (= note 73)
                         (do
                           (phaseswitch 0)

                           ))
                       (if (= note 74)
                         (do
                           (phaseswitch 1)

                           ))
                       (if (= note 75)
                         (do
                           (phaseswitch 2)

                           ))
                       (if (= note 76)
                         (do
                           (phaseswitch 3)

                           ))
                       (if (= note 89)
                         (do
                           (phaseswitch 4)

                           ))
                       (if (= note 90)
                         (do
                           (phaseswitch 5)

                           ))
                       (if (= note 91)
                         (do
                           (phaseswitch 0)

                           ))
                       (if (= note 92)
                         (do
                           (phaseswitch 0)

                           ))
                       (if (= note 105)
                         (do
                          ; (phaseswitch 0)
                           (reset! midiREPL (not @midiREPL) )
                           ))
                       (if (= note 106)
                         (do
                          ; (phaseswitch 1)
                          (reset! ctrlREPL (not @ctrlREPL))
                           ))
                       (if (= note 107)
                         (do
                          ; (phaseswitch 2)
                           (reset! audioREPL (not @audioREPL))
                           ))



                       )
               ;;  (+ 1 1)
                  (do
                    ;(println "unchannelled midi"  (println e))
                    )
                  )


                )
              )
          ::keyboard-handler)
 ;;(remove-event-handler ::keyboard-handler)


(on-event [:midi :note-off]
          (fn [e]
            (let [note (:note e)
                  channel (:channel e)
                  vel 0]
             ; (println  note channel)
              (case channel
                0 (do

                      (swap! midid1 assoc :velocity vel :note note :beat (inc (get @midid1 :beat)))
                    )
                1 (do
                      (swap! midid2 assoc :velocity vel :note note :beat (inc (get @midid2 :beat)))
                    )
                2 (do
                      (swap! midid3 assoc :velocity vel :note note :beat (inc (get @midid3 :beat)))
                   )
                3 (do
                      (swap! midid4 assoc :velocity vel :note note :beat (inc (get @midid4 :beat)))
                      )

;                 (println "unchannelled midi")
                9 (do
                                        ;   (bps/removeewi note)
                   ; (swap! ewiviz update-in)
                    )
                10 (do
                ;   (bps/removeewi note)
                    )
                (+ 1 1)
                )

              )
            )
          ::keyboard-off-handler
          )


;;(remove-event-handler ::keyboard-off-handler)

(on-event [:midi :control-change]
          (fn [e]
;            (println e)
            (let [data1 (:data1 e)
                  data2 (:data2 e)
                  channel (:channel e)]
              (if @ctrlREPL
                (println "CC data received on channel " channel "data " data1 data2))
              (case channel
                9 (case data1
                    2 (do
                        (swap! ewidata assoc :breath1 data2)

                        ))
                10 (case data1
                    2 (do
                        (swap! ewidata assoc :breath2 data2)
                        ))


                13 (case data1    ;;; top row knobs on launch control XL 77 > 84
                     13 (swap! bps/params assoc :p1 data2 )
                     14 (swap! bps/params assoc :p2 data2 )
                     15 (swap! bps/params assoc :p3 data2 )
                     16 (swap! bps/params assoc :p4 data2 )
                     17 (swap! bps/params assoc :p5 data2 )
                     18 (swap! bps/params assoc :p6 data2 )
                     19 (swap! bps/params assoc :p7 data2 )
                     20 (swap! bps/params assoc :p8 data2 )


                     ;(println "unchanneled midi CC data" data1 data2)


                     ;;; second knobs on launch control XL 77 > 84

                     29 (swap! bps/params assoc :q1 data2 )
                     30 (swap! bps/params assoc :q2 data2 )
                     31 (swap! bps/params assoc :q3 data2 )
                     32 (swap! bps/params assoc :q4 data2 )
                     33 (swap! bps/params assoc :q5 data2 )
                     34 (swap! bps/params assoc :q6 data2 )
                     35 (swap! bps/params assoc :q7 data2 )
                     36 (swap! bps/params assoc :q8 data2 )


                                        ;(println "unchanneled midi CC data" data1 data2)

                     (+ 1 1)
                       )
                14 (case data1    ;;; bottom row sliders on launch control XL 77 > 84
                     77 (println data2)
                     78 (println data2)
                     79 (println data2)
                     80 (println data2)
                     81 (println data2)
                     82 (println data2)
                     83 (println data2)
                     84 (println data2)
                                        ;                       (println "unchanneled midi CC data" data1 data2)
                     ;;(+ 1 1)
                     )
                15 (case data1    ;;; bottom row sliders on launch control XL 77 > 84
                     77 (println data2)
                     78 (println data2)
                     79 (println data2)
                     80 (println data2)
                     81 (println data2)
                     82 (println data2)
                     83 (println data2)
                     84 (println data2)
                                        ;                       (println "unchanneled midi CC data" data1 data2)
                     ;;(+ 1 1)
                     )
                (do
                  (+ 1 1)
                  ))

            )
          )
        ::control-handler
      )



(defn mididebugger [state]

  (let [w (/ (q/width) 16)
        h 100
        s 40
        ts 20]
    (q/text-size ts)
    (q/with-translation [800 900 0]
      (if-not (= (get (:d1 state) :velocity) 0)
        (do
          (q/fill (* 2 (get (:d1 state) :velocity)) 0 0 255)
          (q/rect w h s s)
          (q/fill 255)
          (q/text "kck" (+ w 8) (+ h 26) ))

        )
      (if-not (= (get (:d2 state) :velocity) 0)
        (do
          (q/fill (* 2 (get (:d2 state) :velocity)) 0 0 120)
          (q/rect (* 2 w) h s s)
          (q/fill 255)
          (q/text "prc" (+ (* 2 w) 8) (+ h 26) ))
        )

      (if-not (= (get (:d3 state) :velocity) 0)
        (do
          (q/fill (* 2 (get (:d3 state) :velocity)) 0 0 120)
          (q/rect (* 3 w) h s s)
          (q/fill 255)
          (q/text "sn" (+ (* 3 w) 8) (+ h 26) ))

        )
      (if-not (= (get (:d4 state) :velocity) 0)
        (do
          (q/fill (* 2 (get (:d4 state) :velocity)) 0 0 120)
          (q/rect (* 4 w) h s s)
          (q/fill 255)
          (q/text "hh" (+ (* 4 w) 8) (+ h 26) ))
        ))
    )
  )
