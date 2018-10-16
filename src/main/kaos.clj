(ns main.kaos
  (:use [overtone.live])
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil.applet :as qa]
            [main.instruments.box :as box]
            [main.instruments.boxgrid :as boxgrid]
           ; [main.instruments.channel01 :as ch1]
           [main.instruments.boxes :as boxes]
           ; [main.instruments.particleslide :as ps]
           ; [main.instruments.channel02 :as ch2]
           ; [main.instruments.channel03 :as ch3]
           ; [main.instruments.channel04 :as ch4]
            [main.instruments.measurebox :as mb]
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


  ;; (reset! server1 (codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "ipemch1"))
  ;;(:sserver (new SyphonServer [(qa/currentapplet), "test" ]) )
 ; {:server ( codeanticode.syphon.SyphonServer. (quil.applet/current-applet) "test2" )}


 )


;(remove-event-handler ::keyboard-handler)

;;(println @bd)


;;(tr)



(defn updatestate [state]

  {
   ;; :beat @bbeat
   ;; :beatchanged (do (if-not (= @bbeat (get @beatchanged :bbeat))
   ;;                    (swap! beatchanged assoc :change true :bbeat @bbeat)
   ;;                    (swap! beatchanged assoc :change false)
   ;;                    )
   ;;                  @beatchanged)

   ;; ;; :ch1 (main.instruments.template/updatechanneldata @ch1)

   }
)


(defn addstuff []

  )


(defn updatestuff []
  ;(boxes/boxupdate)
  ;(ps/psupdate)
  ;(ch1/updatech1)
  ;(ch2/updatech2)
  ;(ch3/updatech3)
  ;(ch4/updatech4)
  ;(ch5/updatech5)
  ;(ch6/updatech6)
  ;(ch7/updatech7)
  ;(ch8/updatech8)
  ;(ch9/updatech9)
  ;(ch10/updatech10)

  ((get @ch1 :update))
  ((get @ch2 :update))
  ((get @ch3 :update))
  ((get @ch4 :update))
  )

(defn renderstuff []
;;(println "redering pipeline 1")


;  (if @boxes/rendering    (boxes/render @ch1))

  ;; (ps/render @ch1)
;  (if @ch1/rendering   (ch1/render @ch1))

                                        ;
                                        ;(ch2/render @ch2)
  ;;(ch3/render @ch2)
                                        ;(ch4/render @ch4)
                                        ;  (ch5/render @ch5)
                                        ;(ch6/render @ch6)
  ;;  (ch7/render @ch7)
                                        ;(ch8/render @ch8)
  ;;(ch9/render @ch9)
  ;;(ch10/render @ch10)
;  (if @mb/rendering   (mb/render @ch4))


                                        ;(box/render @ch1)
  ((get @ch1 :render) @ch1)
  ((get @ch2 :render) @ch2)
  ((get @ch3 :render) @ch3)
  ((get @ch4 :render) @ch4)

  )


(defn draw [state]
  (updatestuff)                                        ;updatestufff
  (q/background 0)
  (q/stroke-weight 0.2)
  (q/stroke (q/random 155) (q/random 255) (q/random 200) )
  ;
  ;(q/with-translation [ (q/random (/  width 2)) 0 0])
  ;(rotatedlines 1000 (* 2000 (get @ch2 :peak)) )
  ;(q/line 345 0 546 0)
  ;;(q/fill 0 255 0 121)
  ;;
  ;; (q/with-translation [(+ ( q/random 12) 600) 500 400]
  ;;   (q/box 400))
                                        ;(q/camera 0 (* 100 (get @ch9 :peak )) 1000 0 0 -5000 0 1 (* -1 0) )
  ;;(q/camera)
  (renderstuff)
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
