(ns main.kaos )


; start a server and create a client to talk with it
(def PORT 4243)
;(def spacePORT 9243)
;(def spaceOSCs (osc-server spacePORT))
(def OSCs (osc-server PORT))
(def OSCc (osc-client "localhost" 4242))


;//keyboard-handler


;;(osc-handle OSCs "/ch1" (fn [msg] (println (first (get msg :args)))))




                                        ;[main.kaososcfilters]



(osc-handle OSCs "/ch1" (fn [msg] (do
;                                   (println (nth (get msg :args) 2 ) )
                                   (swap! ch1 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))
                                   ;;(println  "ch1" (nth (get msg :args)0  )  )
                                   )  )

            )

(osc-handle OSCs "/ch2" (fn [msg] (swap! ch2 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))

(osc-handle OSCs "/ch3" (fn [msg] (swap! ch3 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch4" (fn [msg] (swap! ch4 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch5" (fn [msg] (swap! ch5 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch6" (fn [msg] (swap! ch6 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch7" (fn [msg] (swap! ch7 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch8" (fn [msg] (swap! ch8 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch9" (fn [msg] (swap! ch9 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))
(osc-handle OSCs "/ch10" (fn [msg] (swap! ch10 assoc :freq (nth (get msg :args) 0 )  :peak (nth (get msg :args) 1 ) :beatnumber (nth (get msg :args) 2 ))  ))

(boxes/add )



(osc-handle OSCs "/ch1attack" (fn [msg] (do
                                        ;(println "ch1attack")
                                         ((get @ch1 :vizsynth))
                                         ;(box/add)
                                         )))
(osc-handle OSCs "/ch1att" (fn [msg] (do
;                                      (println "chqat1")
                                      ( swap! ch1 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2))) :d (nth (get msg :args) 3) ) )

(osc-handle OSCs "/ch2attack" (fn [msg] (do
                                        ;(b                             (println "ch2")
                                         ((get @ch2 :vizsynth )))))
(osc-handle OSCs "/ch3attack" (fn [msg] (do
                                         ;(ch3/add 340 100 200  30)
                                         )))
(osc-handle OSCs "/ch4attack" (fn [msg] (do
                                         ;(ch4/add 100 100 200 20 500)
                                         )))
(osc-handle OSCs "/ch5attack" (fn [msg] (do
                                         ;(ch5/add (rand-int 800) 100 -200  30)
                                         )))
(osc-handle OSCs "/ch6attack" (fn [msg] (do
                                         ;(ch6/add 1000 1000 200 3)
                                         )))
(osc-handle OSCs "/ch7attack" (fn [msg] (do
                                         ;(ch7/add 1000 1000 200 20)
                                         )))
(osc-handle OSCs "/ch8attack" (fn [msg] (do
                                         ;(ch8/add 1000 1000 200 20)
                                         )))
(osc-handle OSCs "/ch9attack" (fn [msg] (do
                                         ;(ch9/add 1000 1000 200 20)
                                         )))
(osc-handle OSCs "/ch10attack" (fn [msg] (do
                                          ;(ch10/add 1000 1000 200 20)
                                         )))
