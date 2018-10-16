(ns main.kaos

  (:require
   [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
   )
)


;;helpers
(def tr (seq->stream (cycle-between 1 1 16 0.1 15)))
(def binary01 (seq->stream (cycle-between 0 1 1)))



(defn listVizChannels []
  (println "box     : " @box/rendering)
;  (println "mb      : " @mb/rendering)
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
