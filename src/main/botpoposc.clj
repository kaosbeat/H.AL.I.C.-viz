(ns main.botpop)


; start a server and create a client to talk with it
(def PORT 4243)
;(def spacePORT 9243)
;(def spaceOSCs (osc-server spacePORT))
(def OSCs (osc-server PORT))
(def OSCc (osc-client "localhost" 4242))


;//keyboard-handler






;(osc-handle OSCs "/ch1attack" (fn [msg] (println (first (get msg :args)))))




                                        ;[main.kaososcfilters]



(osc-handle OSCs "/ch1" (fn [msg] (swap! ch1 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 )) ))
(osc-handle OSCs "/ch2" (fn [msg] (swap! ch2 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch3" (fn [msg] (swap! ch3 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch4" (fn [msg] (swap! ch4 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch5" (fn [msg] (swap! ch5 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch6" (fn [msg] (swap! ch6 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch7" (fn [msg] (swap! ch7 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch8" (fn [msg] (swap! ch8 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch9" (fn [msg] (swap! ch9 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch10" (fn [msg] (swap! ch10 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))



;;(osc-handle OSCs "/ch1attack" (fn [msg] ((get @ch1 :vizsynth) @ch1)))
(osc-handle OSCs "/ch1attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch2attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch3attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch4attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch5attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch6attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch7attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch8attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch9attack" (fn [msg] (+ 1 1)))
(osc-handle OSCs "/ch10attack" (fn [msg] (+ 1 1)))





;(osc-handle OSCs "/ch3debug" (fn [msg] (do (let [debug (nth  (get msg :args) 0)](if (== debug 1)(swap! ch3 assoc :debug false )(swap! ch3 assoc :debug true )))) ))
;(osc-handle OSCs "/ch1render" (fn [msg] (do (let [render (nth  (get msg :args) 0)](if (== render 1)(swap! ch1 assoc :rendering false)(swap! ch1 assoc :rendering true )))) ))


(osc-handle OSCs "/measure" (fn [msg] (do
                                       (reset! measure (nth (get msg :args) 0))
                                       (swap! absolutemeasure inc)
                                       )))
