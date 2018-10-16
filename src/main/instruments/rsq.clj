(ns main.instruments.rsq

  (:require [quil.core :as q]
            [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]
            )
  )


(defn setup []
)


(def cyc (seq->stream (cycle-between 0 100 10)))
(def rotstate (atom  {:current 0 :max 0 :size 0}))

(defn resetstate [] (reset! rotstate {:current 0 :max 0 :size 0} ))
(defn attack [] (swap! rotstate assoc :max (q/random 100 200) ))
(defn updatersq []
  (if (< ( get @rotstate :current)  (get @rotstate :max))

      (swap! rotstate :current (cyc))
      ;(print "test")
      )
 ;; (println rotstate)
  )

(defn render [channel]
  (q/stroke 255)
  (q/stroke-weight 2)
  (q/fill 255 25 0  180 )
  (dotimes [n  10]
    ( q/with-translation [ (-  (* (get channel :x) 1920) (/ 1920 2) ) 500 0]
      (q/with-rotation [ (* 45 n) 0 1 1]
        (q/rect 0 0 (* n  (* (get channel :freq))) 100  5))))
  )
