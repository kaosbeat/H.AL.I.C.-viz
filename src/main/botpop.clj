;; start using C-c M-j
;; if no midi notes are arriving, app will not starts (note statistics can not be zero)

(ns main.botpop
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.bpearlymacros]
            [main.bpdebug]
            [main.instruments.bpstrings :as bps]
            [main.botpoposc]
            [main.bpmacros]
            [main.botpopmidi]
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
  (let [update (get (get @bp (get @bp :active)):update)]
    (dotimes [n (/ (count update) 2)]
      (apply (nth update (* 2 n)) (nth update (+ (* 2 n) 1)))
      )
    )

  )

(defn renderstuff []
  (q/background 0)
  (let [render (get (get @bp (get @bp :active)):render)]
    (dotimes [n (/ (count render) 2)]
       (apply (nth render (* 2 n)) (nth render (+ (* 2 n) 1)))
      )
    )
  ;(bps/renderCube (p12x) (p12y) (p12z))
)

(defn renderdebug []
  (let [debug (get (get @bp (get @bp :active)):debug)]
    (dotimes [n (/ (count debug) 2)]
    (apply (nth debug (* 2 n)) (nth debug (+ (* 2 n) 1)))
      )
    ))




(defn draw [state]




  (updatestuff)
  (q/camera)
  (renderstuff)
  (renderdebug)
  (q/no-fill)
  (q/stroke 255 0 0)
  (q/stroke-weight 1)
  (q/rect 0 0 1920 1080)

  (.sendScreen @server )
  ;; (mididebugger state)



  )



(q/defsketch halic
  :title "halic"
  :size :fullscreen
 ;; :size [1920 1080]
                                        ;:size [(/  (q/width) 2) (/ (q/height) 2)]
                                        ;:size [(/  width 1) (/ height 1)]
  :features [:present]
  :setup setup
  :update updatestate
  :draw draw
  :renderer :opengl
  :middleware [m/fun-mode]
  )
