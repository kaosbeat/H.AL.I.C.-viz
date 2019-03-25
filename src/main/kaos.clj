(ns main.kaos
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.earlymacros]
            [main.instruments.box :as box]
            [main.instruments.boxgrid :as boxgrid]
            [main.instruments.boxes :as boxes]
            [main.instruments.modrotate :as modrotate]
            [main.instruments.flatcircles :as flatcircles]
            [main.instruments.phaselines :as phaselines]
            [main.instruments.backflow :as backflow]
            [main.instruments.tripletimes :as tripletimes]
                                        ; [main.instruments.particleslide :as ps]
           ; [main.instruments.channel02 :as ch2]
           ; [main.instruments.channel03 :as ch3]
           ; [main.instruments.channel04 :as ch4]
            [main.instruments.measurebox :as measurebox]
            [main.instruments.chainbox :as chainbox]
            [main.instruments.superstack :as superstack]
            [main.instruments.spaceshape :as spaceshape]
            [main.instruments.squaretunnel :as squaretunnel]
            [main.instruments.boostbox :as boostbox]
            [main.instruments.piracetambd :as piracetambd]
            [main.instruments.piracetamsd :as piracetamsd]
            [main.channelmapping]
            [main.kaososcfilters]
            [main.kaosmidifilters]
            [main.macros]

            )
    (:import ( 'codeanticode.syphon.SyphonServer))
    (:import ('jsyphon.JSyphonServer))

    )





(def server (atom nil))
(def serverch1 (atom nil))
(def serverch2 (atom nil))
;(println (. System getProperty "java.library.path"))
;

(defn setup []
  (q/frame-rate 30)
  (reset! server (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "H.AL.I.C. viz"))
  (reset! serverch1 (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "ch1"))
  (reset! serverch2 (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "ch2"))
  (def w (q/width))
  (def h (q/height))
  (def cameraZ (/ (/ h 2) (q/tan (* 3.1415 (/ 60 360)  ) ) ))



  )


(defn updatestate [state]
  {
   :bd @midibd
   :sd @midisd
   :oh @midioh
   :ch @midich
   :pc1 @midipc1
   :pc2 @midipc2
   :ld1 @midild1
   :ld2 @midild2
   :chords @midichords
   :bass @midibass
   :keyz @midikeyz
   }

  )







(defn updatestuff []
  ((get @ch1 :update) @ch1)
  ((get @ch2 :update) @ch2)
  ((get @ch3 :update) @ch3)
  ((get @ch4 :update) @ch4)
  ((get @ch5 :update) @ch5)
  ((get @ch6 :update) @ch6)
  ((get @ch7 :update) @ch7)
  ((get @ch8 :update) @ch8)

  )

(defn renderstuff []
  (q/background 0)
  ((get @ch1 :render) @ch1)

  ;(q/background 0)
  (q/fill 255)
;  (q/rect 0 0 1000 1000)
  ((get @ch2 :render) @ch2)
 ; (.sendScreen @serverch2)
  ((get @ch3 :render) @ch3)
  ((get @ch4 :render) @ch4)
  ((get @ch5 :render) @ch5)
  ((get @ch6 :render) @ch6)
  ((get @ch7 :render) @ch7)
  ((get @ch8 :render) @ch8)

  )


(defn draw [state]

  (updatestuff)                                        ;updatestufff
                                        ;  (renderstuff)

  (do
    (q/background 0)
    ((get @ch1 :render) @ch1)
    (.sendScreen @serverch1))

  (do
    (q/backgr [<0;168;49M
               ] ound 0)
    ((get @ch2 :render) @ch2)
    (.sendScreen @serverch2))

;; (q/perspective)
;;  (mididebugger state)
;(q/camera)
;  (q/camera 1000 600 2000 1000 600 20 0 (tr) 0)
  (q/perspective  (/ 3.14 3.0) (/  (q/width) (q/height)) (/ cameraZ 10) (* cameraZ 10000 ) )
;;  (q/camera (- 500 (* (tr) 100)) 600 500 0 0 -150 0 1 0)
 (.sendScreen @server )

  ;(q/camera)
  )

(q/defsketch halic
  :title "halic"
  :size :fullscreen
;  :size [(/  width 2) (/ height 2)]
  ;:size [(/  width 1) (/ height 1)]
  :features [:present]
  :setup setup
  :update updatestate
  :draw draw
  :renderer :p3d
  :middleware [m/fun-mode]
  )
