;;start using C-c M-j

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
   (bps/render)
 ;; (bpp/render)
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
;;  (drawDebug ch1)


                                        ;(q/background 255)

  ;; drawing debug tools

;;  (debugmidistrings :ch4 @ch1 1600 30 "violin1")
 ;; (debugmidistrings :ch5 @ch2 1600 260 "violin2")
;;  (debugmidistrings :ch6 @ch1 1600 490 "alto")
;;  (debugmidistrings :ch7 @ch1 1600 720 "cello")
  ;; (debugstringtype 1600 950) "ds" ;;; problem in boot sequence
  ;;
  ;;

  ;; (debugnotestatistics "ch5" 100 1000 115 50)
;;  (audiodebugger 100 100 [@ch1 @ch2 @ch3 @ch4 @ch5 @ch6 @ch7 @ch8])
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
