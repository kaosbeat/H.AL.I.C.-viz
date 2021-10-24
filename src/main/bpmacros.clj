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
   (if (get @channel :rendering) "|    ON   | " "|    OFF  | " )        (nth (clojure.string/split (clojure.string/replace (str (get @channel :vizsynth)) #"main.instruments." "") #"\$") 0 )
           "| beat" (get @channel :beatnumber)  "| freq" (int  (get @channel :freq))  )
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




(def bp (atom {:active 0
               0 {:active true
                  :phase "off"
                  :update [bps/updateviz []]
                  :init [println ["init phase 0" "yeah"] bps/fillvizbiz [2]]
                  :debug [audiodebugger [100 100 channels]  ]
                  :render [bps/renderStringNotes []]
                  :preset {}
                  }
               1 {:active false
                  :phase "ouverture"
                  :update [bps/updateviz []]
                  :init [println ["init phase 1"]
                         bps/fillvizbiz [2]
                         setcubetween [2200 1900 -1800]]
                  :debug [debugmidistrings [:ch4 @ch1 1600 30 "violin1"]
                          debugmidistrings [:ch5 @ch2 1600 260 "violin2"]
                          debugmidistrings [:ch6 @ch1 1600 490 "alto"]
                          debugmidistrings [:ch7 @ch1 1600 720 "cello"]
                          audiodebugger [100 100 channels]
                          debugstringtype [1600 950 "debugstrings"]
                          debugnotestatistics ["ch5" 100 1000 115 50]
                          emptydebug [1270 950 0 300 200]

                          bps/cubeView []
;;                          bootingdebug [ 500 500 500]
                          ]
                  :render [bps/renderStringNotes []

                           ]
                  }
               2 {:active false
                  :phase "prepulsar"
                  :update [bps/updateviz [] updatecubetween []]
                  :init [println ["init phase 1"] bps/fillvizbiz [8] resettweeners []]
                  :debug [debugmidistrings [:ch4 @ch1 1600 30 "violin1"]
                          debugmidistrings [:ch5 @ch2 1600 260 "violin2"]
                          debugmidistrings [:ch6 @ch1 1600 490 "alto"]
                          debugmidistrings [:ch7 @ch1 1600 720 "cello"]
                          audiodebugger [100 100 channels]
                          debugstringtype [1600 950 "debugstrings"]
                          debugnotestatistics ["ch5" 100 1000 115 50]
                          ;emptydebug [1270 950 0 300 200]
                          ]
                  :render [bps/cubeView []
                            ]
                  }
               3 {:active false
                  :phase "pulsar"
                  :update [bps/updateviz []]
                  :init [println ["init phase 1"] bps/fillvizbiz [8] resettweeners []]
                  :debug [audiodebugger [100 100 channels]
                          ]
                  :render [bps/cubeView []
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
