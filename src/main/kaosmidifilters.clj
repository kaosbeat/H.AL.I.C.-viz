(ns main.kaos)


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


;;midimaps
(def midimap0 (atom []))(def midimap1 (atom []))(def midimap2 (atom []))(def midimap3 (atom []))(def midimap4 (atom []))(def midimap5 (atom []))(def midimap6 (atom []))(def midimap7 (atom []))(def midimap8 (atom []))(def midimap9 (atom []) )(def midimap10 (atom []) )
(def midimap0map (atom []))(def midimap1map (atom []))(def midimap2map (atom []))(def midimap3map (atom []))(def midimap4map (atom []))(def midimap5map (atom []))(def midimap6map (atom []))(def midimap7map (atom []))(def midimap8map (atom []))(def midimap9map (atom []))(def midimap10map (atom []))

(defn midiparse [midimap midimapatom midimapmap midimapmapatom channelmap channelname channelnamekey note vel ]
  (do
    (if (= false (.contains @midimap note))
      (do ;(println midimap8)
        (swap! midimap conj note)
        (reset! midimap (vec (sort @midimap)))
        (swap! midimapmap conj {:x (rand-int (q/width)) :y (rand-int (q/height)) :z (rand-int 500) })
        (reset! channelmap [{:channel channelnamekey} @midimap @midimapmap])
        ) )

    (swap! channelname assoc :note note :velocity vel)
    ))


(on-event [:midi :note-on]
          (fn [e]
              (let [note (:note e)
                    vel  (:velocity e)
                    channel (:channel e)]
;                (println note vel channel)
;                (println channel note vel)
                (case channel
;;                  10 (midiparse midimap10 @midimap10 midimap10map @midimap10map midikeyz keyz :keyz note vel )
  ;;                15 (if (= note 60) (swap! bbeat inc) (println "ch15 unbeat"))
                  14 (case note
                       0 (do
                           (piracetambd/add ch1)
                           (swap! midibd assoc :velocity vel :beat (inc (get @midibd :beat))))
                       1 (swap! midisd assoc :velocity vel)
                       2 (swap! midich assoc :velocity vel)
                       3 (swap! midioh assoc :velocity vel)

                       (+ 1 1)
                       ;(println "no match")
                       )
                 (+ 1 1)
;                 (println "unchannelled midi")
                  )

                )
              )
          ::keyboard-handler)

(on-event [:midi :note-off]
          (fn [e]
            (let [note (:note e)
                  channel (:channel e)
                  vel 0]

             ; (println  note channel)
              (case channel

                14 (case note
                       0 (swap! midibd assoc :velocity vel)
                       1 (swap! midisd assoc :velocity vel)
                       2 (swap! midich assoc :velocity vel)
                       3 (swap! midioh assoc :velocity vel)

                       (+ 1 1)
                       ;(println "no match")
                       )

                 ; 0 (midiparse midimap0 @midimap0 midimap0map @midimap0map midibd bd :bd note vel )
                 ; 1 (midiparse midimap1 @midimap1 midimap1map @midimap1map midisd sd :sd note vel )
                 ; 2 (midiparse midimap2 @midimap2 midimap2map @midimap2map midich ch :ch note vel )
                 ; 3 (midiparse midimap3 @midimap3 midimap3map @midimap3map midioh oh :oh note vel )
                 ; 4 (midiparse midimap4 @midimap4 midimap4map @midimap4map midipc1 pc1 :pc1 note vel )
                 ; 5 (midiparse midimap5 @midimap5 midimap5map @midimap5map midipc2 pc2 :pc2 note vel )
                 ; 6 (midiparse midimap6 @midimap6 midimap6map @midimap6map midild1 ld1 :ld1 note vel )
                 ; 7 (midiparse midimap7 @midimap7 midimap7map @midimap7map midild2 ld2 :ld2 note vel )
                 ; 8 (midiparse midimap8 @midimap8 midimap8map @midimap8map midichords chords :chords note vel )
                 ; 9 (midiparse midimap9 @midimap9 midimap9map @midimap9map midibass bass :bass note vel )
                 ; 10 (midiparse midimap10 @midimap10 midimap10map @midimap10map midikeyz keyz :keyz note vel )
;                  15 (if (= note 60) (swap! bbeat inc) (println "ch15 unbeat"))
                  (+ 1 1)
                 ; (println "unchannelled midi")
                  )
              )

            )
          ::keyboard-off-handler
          )

(on-event [:midi :control-change]
          (fn [e]
            (println e)
            (let [data1 (:data1 e)
                  data2 (:data2 e)
                  channel (:channel e)]
              (case channel
                0 (case data1
                    13 (do (swap! midibd assoc :pitch data2) (swap! ch1 assoc :a data2))
                    29 (do (swap! midibd assoc :decay data2) (swap! ch1 assoc :b data2))
                    49 (do (swap! midibd assoc :noise data2) (swap! ch1 assoc :c data2) )
                    77 (do (swap! midibd assoc :gain data2) (swap! ch1 assoc :d data2))
                    14 (do (swap! midisd assoc :pitch data2) (swap! ch2 assoc :a data2))
                    30 (do (swap! midisd assoc :decay data2) (swap! ch2 assoc :b data2))
                    50 (do (swap! midisd assoc :noise data2) (swap! ch2 assoc :c data2) )
                    78 (do (swap! midisd assoc :gain data2) (swap! ch2 assoc :d data2)))


                (+ 1 1))


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

    (if-not (= (get (:bd state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:bd state) :gain)) 0 0 255)
        (q/rect w h s s)
        (q/fill 255)
        (q/text "bd" (+ w 8) (+ h 26) ))

      )
    (if-not (= (get (:sd state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:sd state) :velocity)) 0 0 120)
        (q/rect (* 2 w) h s s)
        (q/fill 255)
        (q/text "sd" (+ (* 2 w) 8) (+ h 26) ))
      )

    (if-not (= (get (:ch state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:ch state) :velocity)) 0 0 120)
        (q/rect (* 3 w) h s s)
        (q/fill 255)
        (q/text "ch" (+ (* 3 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:oh state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:oh state) :velocity)) 0 0 120)
        (q/rect (* 4 w) h s s)
        (q/fill 255)
        (q/text "oh" (+ (* 4 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:pc1 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:pc1 state) :velocity)) 0 0 120)
        (q/rect (* 5 w) h s s)
        (q/fill 255)
        (q/text "p1" (+ (* 5 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:pc2 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:pc2 state) :velocity))  0 0 120)
        (q/rect (* 6 w) h s s)
        (q/fill 255)
        (q/text "p2" (+ (* 6 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:ld1 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:ld1 state) :velocity))  0 0 120)
        (q/rect (* 7 w) h s s)
        (q/fill 255)
        (q/text "l1" (+ (* 7 w) 8) (+ h 26) ))

      )

    (if-not (= (get (:ld2 state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:ld2 state) :velocity))  0 0 120)
        (q/rect (* 8 w) h s s)
        (q/fill 255)
        (q/text "l2" (+ (* 8 w) 8) (+ h 26) ))

      )

    (if-not (= (get (:chords state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:chords state) :velocity))  0 0 120)
        (q/rect (* 9 w) h s s)
        (q/fill 255)
        (q/text "c1" (+ (* 9 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:bass state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:bass state) :velocity))  0 0 120)
        (q/rect (* 10 w) h s s)
        (q/fill 255)
        (q/text "b1" (+ (* 10 w) 8) (+ h 26) ))

      )
    (if-not (= (get (:keyz state) :velocity) 0)
      (do
        (q/fill (* 2 (get (:keyz state) :velocity)) 0 0 120)
        (q/rect (* 11 w) h s s)
        (q/fill 255)
        (q/text "k1" (+ (* 11 w) 8) (+ h 26) ))

      )

    )
  )
