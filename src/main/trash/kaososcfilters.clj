(ns main.kaos )


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



(osc-handle OSCs "/ch1attack" (fn [msg] ((get @ch1 :vizsynth) @ch1)))
(osc-handle OSCs "/ch2attack" (fn [msg] ((get @ch2 :vizsynth) @ch2)))
(osc-handle OSCs "/ch3attack" (fn [msg] ((get @ch3 :vizsynth) @ch3)))
(osc-handle OSCs "/ch4attack" (fn [msg] ((get @ch4 :vizsynth) @ch4)))
(osc-handle OSCs "/ch5attack" (fn [msg] ((get @ch5 :vizsynth) @ch5)))
(osc-handle OSCs "/ch6attack" (fn [msg] ((get @ch6 :vizsynth) @ch6)))
(osc-handle OSCs "/ch7attack" (fn [msg] ((get @ch7 :vizsynth) @ch7)))
(osc-handle OSCs "/ch8attack" (fn [msg] ((get @ch8 :vizsynth) @ch8)))
(osc-handle OSCs "/ch9attack" (fn [msg] ((get @ch9 :vizsynth) @ch9)))
(osc-handle OSCs "/ch10attack" (fn [msg] ((get @ch10 :vizsynth) @ch10)))


(osc-handle OSCs "/ch1att" (fn [msg] (swap! ch1 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch2att" (fn [msg] (swap! ch2 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch3att" (fn [msg] (swap! ch3 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch4att" (fn [msg] (swap! ch4 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch5att" (fn [msg] (swap! ch5 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch6att" (fn [msg] (swap! ch6 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch7att" (fn [msg] (swap! ch7 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch8att" (fn [msg] (swap! ch8 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch9att" (fn [msg] (swap! ch9 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))
(osc-handle OSCs "/ch10att" (fn [msg] (swap! ch10 assoc :a (nth (get msg :args) 0) :b (nth (get msg :args) 1) :c (nth (get msg :args) 2) :d (nth (get msg :args) 3))))




(osc-handle OSCs "/ch1debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch1 assoc :debug false )
                                            (swap! ch1 assoc :debug true )))) ))
(osc-handle OSCs "/ch2debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch2 assoc :debug false )
                                            (swap! ch2 assoc :debug true )))) ))
(osc-handle OSCs "/ch3debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch3 assoc :debug false )
                                            (swap! ch3 assoc :debug true )))) ))
(osc-handle OSCs "/ch4debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch4 assoc :debug false )
                                            (swap! ch4 assoc :debug true )))) ))
(osc-handle OSCs "/ch5debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch5 assoc :debug false )
                                            (swap! ch5 assoc :debug true )))) ))
(osc-handle OSCs "/ch6debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch6 assoc :debug false )
                                            (swap! ch6 assoc :debug true )))) ))
(osc-handle OSCs "/ch7debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch7 assoc :debug false )
                                            (swap! ch7 assoc :debug true )))) ))
(osc-handle OSCs "/ch8debug" (fn [msg] (do
                                        (let [debug (nth  (get msg :args) 0)]
                                          (if (== debug 1)
                                            (swap! ch8 assoc :debug false )
                                            (swap! ch8 assoc :debug true )))) ))


(osc-handle OSCs "/ch1render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch1 assoc :rendering false)
                                             (swap! ch1 assoc :rendering true )))) ))
(osc-handle OSCs "/ch2render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch2 assoc :rendering false)
                                             (swap! ch2 assoc :rendering true )))) ))
(osc-handle OSCs "/ch3render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch3 assoc :rendering false)
                                             (swap! ch3 assoc :rendering true )))) ))
(osc-handle OSCs "/ch4render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch4 assoc :rendering false)
                                             (swap! ch4 assoc :rendering true )))) ))
(osc-handle OSCs "/ch5render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch5 assoc :rendering false)
                                             (swap! ch5 assoc :rendering true )))) ))
(osc-handle OSCs "/ch6render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch6 assoc :rendering false)
                                             (swap! ch6 assoc :rendering true )))) ))
(osc-handle OSCs "/ch7render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch7 assoc :rendering false)
                                             (swap! ch7 assoc :rendering true )))) ))
(osc-handle OSCs "/ch8render" (fn [msg] (do
                                         (let [render (nth  (get msg :args) 0)]
                                           (if (== render 1)
                                             (swap! ch8 assoc :rendering false)
                                             (swap! ch8 assoc :rendering true )))) ))
