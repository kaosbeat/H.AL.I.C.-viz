(ns main.kaos

  (:require
            [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
                                        ;[quil.applet :as qa]
            )

    )


;;helpers
(def tr (seq->stream (cycle-between 1 1 16 0.1 15)))

(def binary01 (seq->stream (cycle-between 0 1 1)))

(defn drop-nth [n coll]
  ;;keep indexed changes coll from [] to () so added (vec)
  (vec (keep-indexed #(if (not= %1 n) %2) coll))

  ;  (if (= n 0)
  ;  (vec (drop 1 coll))
  ;(if (= n (count coll))
  ;  (pop coll)
  ;  (vec (concat (subvec coll 0 n) (subvec coll (+ 1 n) (count coll))))))
  )




;; buffers go here
;(defonce buf-0 (buffer 8))
;;to get data from a buffer, do (buffer-get buf-0 7)
;(defonce buf-1 (buffer 8))
;(buffer-write! buf-0 [1 0 1 1 0 0 1 0])
;(buffer-write! buf-1 [10 23 23 4 5 6 7 7])




(def server (atom nil))

;; above function adds a funtion in atom list so it can we easily swapped! in oscfilters

(def bbeat (atom 0))
(def beatchanged (atom {:change true :bbeat 0}) )


(defn mod16 [] (mod @bbeat 16))
(defn mod8 [] (mod @bbeat 8))
(defn mod4 [] (mod @bbeat 4))
(defn mod2 [] (mod @bbeat 2))



(defn listVizChannels []
  (println "boxes   : " @boxes/rendering)
  (println "ch1     : " @ch1/rendering)

  (println "mb      : " @mb/rendering)
  )


(defn resetVizCounts []
  "run when you have too much particles and free up some resources"
  (reset! ch1/vizcount [])
  (reset! ch2/vizcount [])
  (reset! ch3/vizcount [])
  (reset! ch4/vizcount [])
  (reset! ch5/vizcount [])
  (reset! ch6/vizcount [])
  (reset! ch7/vizcount [])
  (reset! ch8/vizcount [])
  (reset! ch9/vizcount [])
  (reset! ch10/vizcount [])

  )

(defn setVizToChannel [viz channel ]
  (let [vizadd (ns-resolve viz 'add)
        vizrender (ns-resolve viz 'render)
 ;       vizaudiochannel (ns-resolve viz 'audioChannel)
        vizupdate (ns-resolve viz 'updateviz)]

    (swap! channel assoc :vizsynth vizadd :render vizrender :update vizupdate)
;    (reset! vizaudiochannel channel)
    )
  )


(defn toggleRender [channel]
  (if (get @channel :rendering)
    (swap! channel assoc :rendering false)
    (swap! channel assoc :rendering true)))
