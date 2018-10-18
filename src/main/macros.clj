(ns main.kaos

  (:require
   [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
   [clojure.string :refer [replace split]]
   )
)

(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering true :vizsynth box/add :render box/render :update box/updateviz}))
;;helpers
(def tr (seq->stream (cycle-between 1 1 16 0.1 15)))
(def binary01 (seq->stream (cycle-between 0 1 1)))

(defn printChannelData [name channel]
  (println name
           (nth (clojure.string/split (clojure.string/replace (str (get @channel :vizsynth)) #"main.instruments." "") #"\$") 0 )
           "| beat" (get @channel :beatnumber)  "| freq" (int  (get @channel :freq))  (if (get @channel :rendering) "|    ON" "|    OFF" ))
  )


(defn listVizChannels []
  (printChannelData "ch1" ch1)

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
