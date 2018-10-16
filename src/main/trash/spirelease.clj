(ns main.core

  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.util]
            [main.kaosmidifilters]
            [main.vizinstruments]
            )
    (:import ( 'codeanticode.syphon.SyphonServer))
    (:import ('jsyphon.JSyphonServer))

    )




(def server (atom nil))




(defn setup []
  (def beatchanged (atom {:change true :bbeat 0}) )
  (q/frame-rate 30)
 (reset! server (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "test"))
  ;;(:sserver (new SyphonServer [(qa/currentapplet), "test" ]) )
 ; {:server ( codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "test2" )}
  )


;(remove-event-handler ::keyboard-handler)

;;(println @bd)





(defn update [state]
 ;(println "test")
    {
   :beat @bbeat
   :beatchanged (do (if-not (= @bbeat (get @beatchanged :bbeat))
          (swap! beatchanged assoc :change true :bbeat @bbeat)
          (swap! beatchanged assoc :change false)
          )
        @beatchanged)
   :bd @bd
   :sd @sd
   :ch @ch
   :oh @oh
   :pc1 @pc1
   :pc2 @pc2
   :ld1 @ld1
   :ld2 @ld2
   :chords @chords
   :bass @bass
   :keyz @keyz


   :tr (tr)

   :drumbus @(audio-bus-monitor 0)
   ;:contrabus @(audio-bus-monitor 2)
   ;:fmtonesbus @(audio-bus-monitor 4)
   ;:fmchordsbus @(audio-bus-monitor 6)
   :bus  @(get-in intap [:taps :left])
   ;:kickA (getkick)

   ;:snareA (get (get-in @live-pats [snareA]) (mod @bbeat (count (get-in @live-pats [snareA]))))
   ;:c-hat (getchat)
   ;:fmchord (get  (getchords) :carrier)
   ;:fmtones (nth (map #(or (:carrier %) (:depth %) 0)  (get-in @live-pats [fmtones])) (mod @bbeat (count (get-in @live-pats [fmtones]))))
   ;:contra (get (get-in @live-pats [contra])(mod @bbeat (count (get-in @live-pats [contra]))))
    :lines (swap! linesdyn assoc 6 (tr))
     }


)






(defn draw [state]
  (q/background 0 (* 2 (get @sd :velocity)) (* (tr) 2))
  (q/with-translation [0 (get @sd :velocity) -300]
    (q/with-rotation [(* (/ 360 16) (mod16))  1 0 0 ]
      (dotimes [ x (get @bd :velocity)]
        (dotimes [ y (get @bd :velocity)]
          (q/with-rotation [(* 80  (get @sd :note)) 0 1 1])
          (q/with-translation [ (* x (get @bd :note) 10)
                                (* y -100)
                                (* 100 0) ]
            (q/fill (* (get @bd :velocity) 2 ) 0 23)
            (if (= x 2)
              (q/box 50)
              (q/box (* y 5))))
          ))))

  ;(wavefield 100 2000 200)

  (.sendScreen @server)

  )

(defn draw [state]
  (q/background 0)
  (q/fill 0 255 0 21)
  (wavefield 0 1920 1000 state)
  (q/fill 255 254 0 125)
  (wavefield 0 1620 500 state)

;(wavelines 1000 5 2 3 1 200 1200 14 10 1000 100 10 )
  (q/with-rotation [(* 10 (get @bd :velocity)) 0 1 0]
    (q/stroke-weight 1)
    (joywaves 6 100 [(* (get @bd :note ) 10)  200])

                                        ;( q/with-rotation [(get @bd :note) 0 1 0])


 ;   (wavelines 100 10 2 3 1 200 1200 14 10 1000 10 10 )
    (q/stroke 255 2 4 125)
  ;  (wavelines 14 300 2 3 1 200 120 14 10 100 300 30 )
    )



  (mididebugger state )


  (.sendScreen @server))






(defn draw [state]
  (q/background 25 (* 2 (get @sd :velocity)) 255)
  ;;
  (q/stroke 255)

  (q/with-translation [(* ( get @ch :pan) 10  )  (* (get  @bd :velocity ) 10) 1 ]
    (q/fill 230 (get @ch :note ) (* 16 (mod16)))
    (q/rect 15 (* ( mod16) 0) 300 300)
    ;;(println (mod16))
    (wavefield 1000 300 34)
    )
  (q/with-rotation [(get @chords :velocity) 1 1 0]
    (q/with-translation [ (+ 100 (* 50 (mod @bbeat 16))) 100 0]
      (q/fill  (get @sd :velocity ) (* 16 (mod16)) 12 )
      (q/box (* (get @bd :velocity)  10))
      ))
  (dotimes [n (get @sd :velocity)]
    (q/line (* 20 n) 400 (* n (get @bd :velocity ))  10)
    )

  (mididebugger state )
  (.sendScreen @server )
  ;(.sendScreen ( :server state))
  )






(q/defsketch halic
  :title "halic"
  ;;  :size :fullscreen
  :size [width height]
  :features [:present]
  :setup setup
  :update update
  :draw draw
  :renderer :p3d
  :middleware [m/fun-mode]
  )
