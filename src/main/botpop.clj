3;;start using C-c M-j

(ns main.botpop
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.bpearlymacros]
            [main.bpdebug]
            [main.instruments.box :as box]
            [main.instruments.bpstrings :as bps]
            [main.instruments.bppulsar :as bpp]
            [main.instruments.bpewi :as bpe]
            [main.bpchannelmapping]
            [main.botpoposc]
            [main.botpopmidi]
            [main.bpmacros]
            )
  (:import ( 'codeanticode.syphon.SyphonServer))
    (:import ('jsyphon.JSyphonServer))
    )


(def server (atom nil))
;(println (. System getProperty "java.library.path"))
;

(defn setup []
  (q/frame-rate 30)
  (reset! server (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "botpop viz"))
  (defonce debugfont (q/load-font (.getPath (clojure.java.io/resource "AndaleMono-48.vlw"))))
  (def w (q/width))
  (def h (q/height))
  (def cameraZ (/ (/ h 2) (q/tan (* 3.1415 (/ 60 360)  ) ) ))
  )


(defn updatestate [state]
  {
   :d1 @midid1
   :d2 @midid2
   :d3 @midid3
   :d4 @midid4
   }
  )

(defn updatestuff []
;;  ((get @ch1 :update) @ch1)
;;  ((get @ch2 :update) @ch2)
;;  ((get @ch3 :update) @ch3)
;;  ((get @ch4 :update) @ch4)
;;  ((get @ch5 :update) @ch5)
;;  ((get @ch6 :update) @ch6)
;;  ((get @ch7 :update) @ch7)
  ;;  ((get @ch8 :update) @ch8)
  (bps/updateviz)
;;  (bpp/updateviz)
  )

(defn renderstuff []
  (q/background 0)
  (let [render (get (get @bp (get @bp :active)):render)]
    (dotimes [n (/ (count render) 2)]
       (apply (nth render (* 2 n)) (nth render (+ (* 2 n) 1)))
      )
    )
  ;(bps/renderCube (p12x) (p12y) (p12z))


  (defn renderdebug []
    (let [debug (get (get @bp (get @bp :active)):debug)]
      (dotimes [n (/ (count debug) 2)]
        (apply (nth debug (* 2 n)) (nth debug (+ (* 2 n) 1)))
        )
      )))


(defn draw [state]
  (updatestuff)
  (q/camera)
  (renderstuff)
  (renderdebug)
  (.sendScreen @server )
  ;; (mididebugger state)
  )



(q/defsketch halic
  :title "halic"
  :size :fullscreen
  ;;size [960 540]
                                        ;:size [(/  (q/width) 2) (/ (q/height) 2)]
                                        ;:size [(/  width 1) (/ height 1)]
  :features [:present]
  :setup setup
  :update updatestate
  :draw draw
  :renderer :opengl
  :middleware [m/fun-mode]
  )
