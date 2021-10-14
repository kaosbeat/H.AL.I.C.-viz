(ns main.botpop

  (:require

   [clojure.string :refer [replace split]]
   )
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
  (println "


")
  (listVizChannels)
  (println "


")
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
