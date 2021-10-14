(ns main.botpop

  (:require
   [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
   [clojure.string :refer [replace split]]
   )
)

;(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering true :vizsynth box/add :render box/render :update box/updateviz}))
;;helpers
(def tr (seq->stream (cycle-between 0 1 0.01 0.01)))
(def camrot (seq->stream (cycle-between 0 180 0.01)))
(def pirad (seq->stream (cycle-between 0 6.2830 0.02)))
(def zerorounddeg (seq->stream (cycle-between 0 360 0.1 360 )))
(def binary01 (seq->stream (cycle-between 0 1 1)))



(defn help []
  (println "helperfunctions")
  (println "try 'instrumentnamespace/channel ch1' ")
  (println "don't forget to '(toggleRender channel)' to enable rendering ")
  (println "fakedata channel can be used for testing")
  (println "(fakedata chX) ")
  (println "loadsoffakedata will give fakedata on all channels")
  (println  "(listVizChannels)")
  (println "")
 ; (map println instrumentslist)
    )




(def midibd (atom {:velocity 0 :beat 0 :noise 0 :gain 0}))
(def midisd (atom {:velocity 0}))
(def midioh (atom {:velocity 0}))
(def midich (atom {:velocity 0}))
(def midipc1 (atom {:velocity 0}))
(def midipc2 (atom {:velocity 0}))
(def midild1 (atom {:velocity 0}))
(def midild2 (atom {:velocity 0}))
(def midichords (atom {:velocity 0}))
(def midibass (atom {:velocity 0}))
(def midikeyz (atom {:velocity 0}))
