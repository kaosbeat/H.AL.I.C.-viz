(ns main.instruments.bpstrings
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]
   [quil.helpers.seqs :refer [seq->stream range-incl cycle-between steps]]))


(def viz (atom []))
(def vizbiz (atom []))
(def vizcount (atom []))
(def vizbizcount (atom []))
(def rendering (atom false))
;; (def lasttype (atom 0))


(def status (atom {:debug true :violin1 true :size3D :5}))
(def params (atom {:p1 64 :p2 64 :p3 64 :p4 64 :p5 64 :p6 64 :p7 64 :p8 64
                   :b1 false :b2 false :b3 false :b4 false :b5 false :b6 false :b7 false :b8 false} ))
(def dynamics (atom {:bootphase 0 :hfreq1 1200 :hfreq2 600 :hfreq3 300 :hfreq4 200 :nfreq1 0 :nfreq2 0 :nfreq3 0 :nfreq4 0 :amp1 0 :amp2 0 :amp3 0 :amp4 0} ))


(defn fillvizbiz [size3D]
  (reset! vizbiz [])
  (dotimes [x size3D]
    (dotimes [y size3D]
      (dotimes [z size3D]
        (let [space 20]
         (swap! vizbiz conj {:x x :y y :z z :ttl 10 :type 1})
         ;; (swap! vizbiz conj {:x (* space x) :y (* space y) :z (* space z) :ttl 0 :type 0 })
          ;;(println "duh")
          )))))

(fillvizbiz 4)

(def radrot (seq->stream (cycle-between 0 6.2830 0.1 6.2830))
  )

(defn module [x y z seed  p1 ]
  (q/noise-seed seed)
  (q/stroke-weight 0.5)
  (let [
        p2 (get @params :p2)
        p3 (get @params :p3)
        size (+ 10 p1)
        layers (+ 1 (int (/ p2 4)))]
    (q/fill 255 (* 2 p3))
    (dotimes [n layers]
      (let [sizex (* size (q/noise (* n 0.2)))
            sizey (* size (q/noise (* n 0.3)))]
        (q/with-translation [ (/ sizex 2) (/ sizey 2) (* n 9)]

          (q/box sizex sizey 0))


          ))
    (q/with-translation [0 0 50]
      (q/text-size 12)
      (q/fill 255 0 0)
      (q/text (str "seed " seed ) 0 0))
    )

  )


(defn draw [x y z ttl type seed]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)
  (q/with-translation [x y z]
    (q/fill 255 255 255 ttl)
  ;  (q/box (* type  50) 50 0)
    (q/perspective)
    (module x y z seed (get @params :p1))

  )
  )


(defn drawbiz [x y z ttl type]
  "main draw for this visual instrument"
  ;(println "drawing " id  x y z freq beat)

  (let [colors [(q/color 0 0 0 0) (q/color 55 0 255 128) (q/color 255 0 0 128) (q/color 255 255 255 128) (q/color 0 0 255 128)]]
       (q/fill (nth colors (- type 1)))
       )
  (q/with-translation [x y z]

    ;;(q/fill 255 255 0 ttl)
    (q/box (* type  50) 50 50)
    )
  )

;;; render datafeedercube
(defn renderCube [x y z]
(q/with-translation [x y z]
    (q/with-rotation [(* (radrot) 1) (if (get @params :b1) 1 0) (if (get @params :b2) 1 0) (if (get @params :b3) 1 0) ]
      (q/with-translation [-240 -240 0]
        (dotimes [n (count @vizbiz)]
          (let [x (get (nth @vizbiz n) :x)
                y (get (nth @vizbiz n) :y)
                z (get (nth @vizbiz n) :z)
                ttl (get (nth @vizbiz n) :ttl)
                type (get (nth @vizbiz n) :type)
                space 120
                ]
            (drawbiz (* space  x) (* space  y) (* space z) ttl type)
            )))))
  )


(defn renderStringNotes []
  ;;; render stringnotes
  (dotimes [n (count @viz)]
    (let [x    (get (nth @viz n) :x)
          y    (get (nth @viz n) :y)
          z    (get (nth @viz n) :z)
          ttl  (get (nth @viz n) :ttl)
          type (get (nth @viz n) :type)
          seed (get (nth @viz n) :seed )]
        (draw x y z ttl type seed)
        )
    )
  )



(defn add [type note]
  (let [x -200
        y (* 200 type)
        z 0
        ttl 100
        note note
        type type]
    (if (= 0 (count @viz))
      (reset! viz []))
    (swap! viz conj {:x x :y y :z z :ttl ttl :seed note :type type})
  )
)

(defn updateviz []
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (reset! vizbizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      (do
        ;;(swap! viz update-in [n :ttl] dec)
        (swap! viz update-in [n :x] (fn [x] (+ x 5)))
        (if (> (get  (get @viz n) :x) 1500)
          ;;(println "fgound one")
          (do
            (if (= 0 (count @vizbiz))
              (reset! vizbiz []))
            (let [m (rand-int  (count @vizbiz))
                  ;ob (update-in (get @vizbiz m) [:type] (get (get @viz n) :type))
                  ]
              (swap! vizbiz assoc-in [m :type] (get (get @viz n) :type))
              (reset! main.botpop/lasttype (get (get @viz 0) :type))
             ;; (println vizbiz)
              )
            ;; (println "adding here")
            (swap! vizcount conj n)
            ) ;else mark pill for deletion
          )
        )
      )
    )
  (dotimes [n (count @vizcount)]
    ;;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz))
    )

  (dotimes [n (count @vizbiz)]
    (if (false? (= 0 (get (get @vizbiz n) :ttl)))
      (do
      ;;  (swap! vizbiz update-in [n :ttl] dec)
      ;;  (swap! vizbiz update-in [n :y] (fn [y] (+ y 1)))
        )
    ;;  (swap! vizbizcount conj n) ;; not deteing themn even if ttl = 0
     )
   )
  (dotimes [n (count @vizbizcount)]
    ;;    (println " really dropping stuff")
   ;; (reset! vizbiz  (drop-nth (nth @vizbizcount n) @vizbiz))
    )
  )

(defn channel [channel]
  (swap! channel assoc :vizsynth add :render renderStringNotes :update updateviz)
;  (swap! rendering true)
  )
