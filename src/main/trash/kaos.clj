;;start using C-c M-j

(ns main.kaos
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.earlymacros]
            [main.debug]
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
            [main.instruments.spaceshape :as spaceshape ]
            [main.instruments.squaretunnel :as squaretunnel]
            [main.instruments.boostbox :as boostbox]
            [main.instruments.piracetambd :as piracetambd] ;; needs midi abcd
            [main.instruments.piracetamsd :as piracetamsd]
            [main.instruments.piracetamch :as piracetamch]
            [main.instruments.carpetcross :as carpetcross]
            [main.instruments.carpet :as carpet]
            [main.instruments.droodle :as droodle]  ;;checked OK
            [main.channelmapping]
            [main.kaososcfilters]
            [main.kaosmidifilters]
            [main.macros]

            )
  (:import ( 'codeanticode.syphon.SyphonServer))
    (:import ('jsyphon.JSyphonServer))

    )




;;;
;; CH1/2 stereo mix
;; CH3 BassDrum
;; CH4 Snare
;; CH5 hi-hat
;; CH6 percussie
;; CH7 loops L
;; CH8 loops R




(def server (atom nil))
;(println (. System getProperty "java.library.path"))
;

(defn setup []
  (q/frame-rate 30)
  (reset! server (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "H.AL.I.C. viz"))
   (defonce debugfont (q/load-font (.getPath (clojure.java.io/resource "AndaleMono-48.vlw"))))
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
  ((get @ch2 :render) @ch2)
  ((get @ch3 :render) @ch3)
  ((get @ch4 :render) @ch4)
  ((get @ch5 :render) @ch5)
  ((get @ch6 :render) @ch6)
  ((get @ch7 :render) @ch7)
  ((get @ch8 :render) @ch8)

  )





(defn draw [state]
  (updatestuff)                                        ;updatestufff
  (q/camera)
  (renderstuff)

 (.sendScreen @server )
;; (q/perspective)
 ; (mididebugger state)

;  (q/camera 1000 600 2000 1000 600 20 0 (tr) 0)
;  (q/perspective  (/ 3.14 3.0) (/  (q/width) (q/height)) (/ cameraZ 10) (* cameraZ 10000 ) )
;;  (q/camera (- 500 (* (tr) 100)) 600 500 0 0 -150 0 1 0)
;(drawDebug ch5)
;(q/background 255)

  )



(q/defsketch halic
  :title "halic"
  :size :fullscreen
  ;:size [960 540]
  ;:size [(/  (q/width) 2) (/ (q/height) 2)]
  ;:size [(/  width 1) (/ height 1)]
 :features [:present]
  :setup setup
  :update updatestate
  :draw draw
  :renderer :opengl
  :middleware [m/fun-mode]
  )
