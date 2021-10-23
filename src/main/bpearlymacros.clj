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
(def pirad (seq->stream (cycle-between 0 6.2830 0.02 6.2830)))
(def zerorounddeg (seq->stream (cycle-between 0 360 0.1 360 )))
(def binary01 (seq->stream (cycle-between 0 1 1)))

;;(def phase1x  (let [k (seq->stream (range -300) 300)] (if (k)) ))

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


;;((nth (get (get @bp 0) :init) 0) 4)

(def midid1 (atom {:velocity 0 :beat 0}))
(def midid2 (atom {:velocity 0 :beat 0}))
(def midid3 (atom {:velocity 0 :beat 0}))
(def midid4 (atom {:velocity 0 :beat 0}))


(def measure (atom 0))
(def lastnote (atom {:ch1 0 :ch2 0 :ch3 0 :ch4 0 :ch5 0 :ch6 0 :ch7 0 :ch8 0}))
(def lasttype (atom 0))



(def midiREPL (atom false)) ;; print midi notes to REPL
(def ctrlREPL (atom false))
;; print midi controller events to repl



;;(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch2 (atom {:id 2 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch3 (atom {:id 3 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch4 (atom {:id 4 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch5 (atom {:id 5 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch6 (atom {:id 6 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch7 (atom {:id 7 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch8 (atom {:id 8 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch9 (atom {:id 9 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
;;(def ch10 (atom {:id 10 :freq 0.0 :peak 0.0 :beatnumber 0 :debug true}))
