(ns main.botpop

  (:require
   [clojure.string :refer [replace split]]

   [main.instruments.bpstrings :as bps]
   [quil.core :as q])
  )

;(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering true :vizsynth box/add :render box/render :update box/updateviz}))
;;helpers


(defn printChannelData [name channel]
  (println
   name
   (if (get @channel :debug) "|    ON   | " "|    OFF  | " )        (nth (clojure.string/split (clojure.string/replace (str (get @channel :vizsynth)) #"main.instruments." "") #"\$") 0 )
   "| beat" (get @channel :beatnumber)  "| freq"  (get @channel :freq)  )
  )


(defn listVizChannels []
  (printChannelData "ch1" ch1)
  (printChannelData "ch2" ch2)
  (printChannelData "ch3" ch3)
  (printChannelData "ch4" ch4)
  (printChannelData "ch5" ch5)
  (printChannelData "ch6" ch6)
  (printChannelData "ch7" ch7)
  (printChannelData "ch8" ch8)


  )

(defn toggleRender [channel]
  (if (get @channel :rendering)
    (swap! channel assoc :rendering false)
    (swap! channel assoc :rendering true)))



(defn setVizToChannel [viz channel ]
  (let [vizadd (ns-resolve viz 'add)
        vizrender (ns-resolve viz 'render)
 ;       vizaudiochannel (ns-resolve viz 'audioChannel)
        vizupdate (ns-resolve viz 'updateviz)]

    (swap! channel assoc :vizsynth vizadd :render vizrender :update vizupdate)
;    (reset! vizaudiochannel channel)
    )
  )

(def instrumentslist ["box" "boxes" "boxgrid" "modrotate" "flatcircles" "phaselines" "measurebox" "tripletimes" "backflow" "chainbox" "superstack" "spaceshape" "squaretunnel" ])

(defn help []
  (println "helperfunctions")
  (println "try 'instrumentnamespace/channel ch1' ")
  (println "don't forget to '(toggleRender channel)' to enable rendering ")
  (println "fakedata channel can be used for testing")
  (println "")
  (listVizChannels)
  (println "")
  (map println instrumentslist)

    )


(defn fakedata  [channel]
  (swap! channel assoc :freq (rand-int 2000) :peak (rand-int 500) :beatnumber (rand-int 200)   :x (rand-int 1920) :y (rand-int 1080) :z (- 100 (rand-int 200)) :a  (rand-int 127) :b  (rand-int 127)  :c (rand-int 127) :d (rand-int 127))
  )

(defn soloviz [channel]
  (doall [
         ; (reset! channels [ch1 ch2 ch3 ch4 ch5 ch6 ch7 ch8 ch9 ch10])
          (map #(swap! % assoc :rendering false) @channels)
          (swap! channel assoc :rendering true)
          (println ( count @channels) )]
    )
  )

(defn allchannelsviz []
  (map #(swap! % assoc :rendering true) channels)
  )


(defn loadsoffakedata []
  (map fakedata channels)
  )







(def bp (atom {:active 5
               0 {:active true
                  :phase "off"
                  :update [bps/updateviz []]
                  :init [println ["init phase 0" "yeah"]
                         bps/fillvizbiz [1]
                         setcubetween [0 260 100]
                         bps/resetviz ["resetting inputs"]
                         ]
                  :debug [audiodebugger [100 100 channels]
                          ;debugstringtype [30 770 "debugstrings"]
                          debugstring [30 770 "violin1" 0]
                          debugstring [330 770 "violin2" 1]
                          debugstring [630 770 "alto" 2]
                          debugstring [930 770 "cello" 3]
                          ]
                  :render [bps/renderStringNotes []
                           ;bps/printrandomstuff [100]
                           ]
                  :preset {}
                  }
               1 {:active false
                  :phase "ouverture"
                  :update [bps/updateviz [] followcubetween  []]
                  :init [println ["init phase 1"]
                         bps/fillvizbiz [2]
                         setcubetween [1000 500 -500]]
                  :debug [debugmidistrings [:ch4 @ch3 20 20 "violin1"]
                          debugmidistrings [:ch5 @ch4 20 240 "violin2"]
                          debugmidistrings [:ch6 @ch5 20 460 "alto"]
                          debugmidstrings [:ch7 @ch6 20 680 "cello"]
                          audiodebugger [1600 10 channels]
                          ;;emptydebug [1270 950 0 300 200]

                          debugnotestatistics ["ch5" 100 900 115 50]

                          q/ortho []                                        ;
                          bps/cubeView [0 0 0]
                          bps/stringDebugannotateframe []
                          q/perspective []

                          bootingdebug [1570 900 0]
                          ]
                  :render [bps/renderStringNotes []



                           ]
                  }
               2 {:active false
                  :phase "prepulsar"
                  :update [bps/updateviz [] updatecubetween [] bps/updatekick []]
                  :init [println ["init phase 2"] bps/fillvizbiz [8] resettweeners [550 40 -1800
                                                                                    960 540 -1500
                                                                                    5 10 10 ]]
                  :debug [;debugmidistrings [:ch4 @ch3 1600 20 "violin1"]
                          ;debugmidistrings [:ch5 @ch4 1600 240 "violin2"]
                          ;debugmidistrings [:ch6 @ch5 1600 460 "alto"]
                          ;debugmidistrings [:ch7 @ch6 1600 680 "cello"]
                          audiodebugger [100 100 channels]
                          ;debugstringtype [1600 89 "debugstrings"]
                          ;debugnotestatistics ["ch5" 100 1150 115 50]
                          ;emptydebug [1270 950 0 300 200]
                          bootingdebug [1570 900 0]
                          ;bps/cubeDebugannotateframe []
                          ]
                  :render [q/perspective []
                           bps/cubeView [0 0 0]
                           ;;bps/renderStringNotes []
                            ]
                  }
               3 {:active false
                  :phase "pulsar"
                  :update [q/perspective []  bps/updateviz []]
                  :init [println ["init phase 3"]]
                  :debug [audiodebugger [100 100 channels]
                          bootingdebug [1570 900 0]
                          ]
                  :render [bps/cubeView [0 0 0]
                            ]
                  }
               4 {:active false
                  :phase "preewi"
                  :update [q/perspective []  bps/updateviz []]
                  :init [println ["init phase 1"] ]
                  :debug [audiodebugger [100 100 channels]
                          bootingdebug [1570 900 0]
                          ]
                  :render [q/ortho []
                           bps/cubeView [0 0 0]
                            ]
                  }
               5 {:active false
                  :phase "ewi"
                  :update [q/perspective []  bps/updateewistream [] bps/updateviz []]
                  :init [println ["init ewi phase"]
                         bps/resetviz ["resetting string  & drum inputs"]
                         bps/resetewiviz ["resetting ewi inputs"]
                         ]
                  :debug [audiodebugger [100 100 channels]
                          ewidebug [1600 30 0]
                          bootingdebug [1570 900 0]
                          ]
                  :render [bps/ewiView [0 0 0]
                           bps/cubeView [0 0 0]
                            ]
                  }


               }))

(defn phaseswitch [phase]
  ;; switch to active
  (dotimes [n (count @bp)] (if (get (get @bp n) :active) (swap! bp assoc-in [n :active] false)))
  (println phase)
  (swap! bp assoc-in [phase :active] true)
  (swap! bp assoc :active phase)
  (let [init (get (get @bp phase) :init)]
    (dotimes [n (/ (count  init) 2)]
      ;; ((nth init (* 2 n)) (nth init (+ (* 2 n) 1)))
      ;; ((nth init (* 2 n))  (nth (nth init (+ (* 2 n) 1) ) 0) )
      (apply (nth init (* 2 n)) (nth init (+ (* 2 n) 1)))
      ))
  ;; enables debuggers
  ;; debuggers are enabled in botpop main
  )
