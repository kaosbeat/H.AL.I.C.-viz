(ns main.botpop
  (:require [main.instruments.bpstrings :as bps]))


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
  ;;              (println note vel channel)
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
                  4 (do
                     ; (println "stuff added")
                      (bps/add 1) ;; type 1 = violin
                      )
                  5 (do
                      (bps/add 2) ;; type 2 = violin
                      (swap! midid4 assoc :velocity vel :note note :beat (inc (get @midid4 :beat)))

                      )
                  6 (do
                      (bps/add 3) ;; type 3 = alto
                      (swap! midid4 assoc :velocity vel :note note :beat (inc (get @midid4 :beat)))
                      )
                  7 (do
                      (bps/add 4) ;; type 1 = cello
                      (swap! midid4 assoc :velocity vel :note note :beat (inc (get @midid4 :beat)))
                      )


                 (+ 1 1)
;                 (println "unchannelled midi")
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
                 (+ 1 1)
;                 (println "unchannelled midi")
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
              (case channel
                13 (case data1    ;;; bottom row sliders on launch control XL 77 > 84
                     13 (swap! bps/params assoc :p1 data2 )
                     14 (swap! bps/params assoc :p2 data2 )
                     15 (swap! bps/params assoc :p3 data2 )
                     16 (swap! bps/params assoc :p4 data2 )
                       78 (println data2)
                       79 (println data2)
                       80 (println data2)
                       81 (println data2)
                       82 (println data2)
                       83 (println data2)
                       84 (println data2)
                       (println "unchanneled midi CC data" data1 data2)
                       ;;(+ 1 1)
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
                  ;(println "unchanneled CC midi Channel")
                  (+ 1 1)
                )
              )
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

    (if-not (= (get (:d1 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:d1 state) :velocity)) 0 0 255)
        (q/rect w h s s)
        (q/fill 255)
        (q/text "D1" (+ w 8) (+ h 26) ))

      )
    (if-not (= (get (:d2 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:d2 state) :velocity)) 0 0 120)
        (q/rect (* 2 w) h s s)
        (q/fill 255)
        (q/text "sd" (+ (* 2 w) 8) (+ h 26) ))
      )

    (if-not (= (get (:d3 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:d3 state) :velocity)) 0 0 120)
        (q/rect (* 3 w) h s s)
        (q/fill 255)
        (q/text "ch" (+ (* 3 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:d4 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:d4 state) :velocity)) 0 0 120)
        (q/rect (* 4 w) h s s)
        (q/fill 255)
        (q/text "oh" (+ (* 4 w) 8) (+ h 26) ))
      )
    )
  )
