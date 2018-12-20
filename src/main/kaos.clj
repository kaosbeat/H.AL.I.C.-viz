(ns main.kaos
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.instruments.box :as box]
            [main.instruments.boxgrid :as boxgrid]
            [main.instruments.boxes :as boxes]
            [main.instruments.modrotate :as modrotate]
            [main.instruments.flatcircles :as flatcircles]
            [main.instruments.phaselines :as phaselines]
            [main.instruments.backflow :as backflow]
            [main.instruments.tripletimes :as tripletimes]
                                        ;            [main.instruments.particleslide :as ps]
           ; [main.instruments.channel02 :as ch2]
           ; [main.instruments.channel03 :as ch3]
           ; [main.instruments.channel04 :as ch4]
            [main.instruments.measurebox :as measurebox]
            [main.instruments.chainbox :as chainbox]
            [main.channelmapping]
            [main.kaososcfilters]
            [main.macros]

            )
    (:import ( 'codeanticode.syphon.SyphonServer))
    (:import ('jsyphon.JSyphonServer))

    )





(def server (atom nil))



(defn setup []
  (q/frame-rate 30)
  (reset! server (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "Amsterdam Dance Event"))
(def w (q/width))
(def h (q/height))
)


(defn updatestate [state]
)





(defn updatestuff []
  ((get @ch1 :update))
  ((get @ch2 :update))
  ((get @ch3 :update))
  ((get @ch4 :update))
  ((get @ch5 :update))
  ((get @ch6 :update))
  ((get @ch7 :update))
  ((get @ch8 :update))

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
  (renderstuff)
  (q/camera (- 600 (* (tr) 200)) 500 1000 500 500 -5000 0 1 0 )
  (.sendScreen @server )
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
