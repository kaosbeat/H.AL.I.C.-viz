(ns main.instruments.carpet
  (:require
   [main.util :refer [drop-nth]]
   [quil.core :as q]))



(def viz (atom []))
(def vizcount (atom 0))
(def rendering (atom false))

(defn alternatingBlocks [x y z size color1 color2]
  (q/with-translation [x y z]
    (dotimes [n 100]
      (if (even? n)
        (q/fill color1)
        (q/fill color2))
      (q/with-translation [(* n 50) 0 0]
        (q/box size size 50)))))

(defn fullRow [x y z size color]
  (q/with-translation [(+ x (/ (q/width) 2)) y z]
    (q/fill color)
    (q/box (q/width) size 50 )))

(defn blockRow [x y z offsetY rows colorRow blockHeight blockWidth]
  (q/with-translation [x y z]
    (dotimes [r rows]
      (q/with-translation [(* r offsetY) (* r blockHeight) 0]
        (let [times (int (/ (q/width) blockWidth))]
          (dotimes [n times]
            (q/with-translation [(* n blockWidth) 0 0]
              (q/fill (nth (cycle colorRow) n))
              (q/box blockWidth blockHeight 50))

              )))))
  )

(defn blockRow2 [x y z offsetY rows colorRow blockMod blockHeight]
  (let [blockWidth (* rows blockMod)
        ]
    (q/with-translation [x y z]
      (dotimes [r rows]
        (q/with-translation [(* -1 (* r (/ blockMod 2))) (* r blockHeight) 0]
          (let [module (+ (* rows blockMod) (* 2 blockWidth))
                times (int (+ 1 (/ (q/width) module))) ]
            (dotimes [n times]
              (q/with-translation [(* n module) 0 0]
                (q/fill (nth (cycle colorRow) n))
                (q/box blockWidth blockHeight 50)
                (q/fill (nth (cycle colorRow) (+ 1 n)))
                (q/with-translation [(/ (+ blockWidth (* r blockMod)) 2) 0 0]
                  (q/box (* r blockMod) blockHeight 50)
                  )
                (q/fill (nth (cycle colorRow) (+ 2 n)))
                (q/with-translation [(+ blockWidth (* r blockMod)) 0 0 ]
                  (q/box blockWidth blockHeight 50)
                  )
                (q/fill (nth (cycle colorRow) (+ 3 n)))

                (q/with-translation [(+ (/ (* r blockMod) 2)  (* blockWidth 2)) 0 0]
                  (q/box (* (- rows r) blockMod) blockHeight 50)
                  )
                )1

              ))))))



  )

(defn stepRow [x y z offsetY rows colorRow blockWidth blockHeight]
  (q/with-translation [x y z]
    (let [blocks (/ (q/width) (* 2 blockWidth))]
      (dotimes [b blocks]
        (q/with-translation [(* b (* blockWidth rows)) 0 0]
          (dotimes [r rows]
            (q/with-translation [0 (* r blockHeight)]
              (dotimes [c (- rows r)]
                (q/with-translation [(* c blockWidth) 0 0]
                                        ;            (q/fill (nth (cycle colorRow) (+ 2 c)))

                  (q/fill 255 0 0)
                  (q/box blockWidth)))
              (dotimes [c r]
                (q/with-translation [(* (+ c (- rows r)) blockWidth) 0 0 ]
                                        ; (q/fill (nth (cycle colorRow) (+ 1 c)))
                  (q/fill 0 25 255)
                  (q/box blockWidth)))

              )
            ))))
    )
  )

(defn stepRow2 [x y z offsetY rows colorRow blockWidth blockHeight]
  (q/with-translation [x y z]
    (let [blocks  150]
      (dotimes [r rows]
        (dotimes [b blocks]
          (q/with-translation [(* blockWidth b) (* blockHeight r) 0 ]

            (q/fill 255 0 0)
            (q/box 10)
            )
          ))))

  )

(defn draw [data]
  "main draw for this visual instrument"
  (q/stroke 255)
  (q/stroke-weight 2)
  ;; layer 1
  (alternatingBlocks 0 0 0 50 (q/color 195 152 106) (q/color 215 167 131))
  ;; layer2
  (fullRow 0 50 0 50 (q/color 248 212 150))
  ;; layer3
  (fullRow 0 90 0 30 (q/color 220 155 116))
  ;; layer4
 (alternatingBlocks 0 130 0 50  (q/color 195 152 106) (q/color 215 167 131)  )
  ;;  layer5
  (blockRow2 0 165 0 250 5 [(q/color 194 128 96) (q/color 247 200 153) (q/color 226 140 95) (q/color 143 106 72)] (+ 1 (get data :size)) 20)
(alternatingBlocks 0 280 0 50 (q/color 165 152 106) (q/color 215 167 131))
  (fullRow 0 315 0 20 (q/color 248 212 120))
 ; (blockRow2 0 365 0 250 15 [(q/color 194 128 96) (q/color 247 (+ 100 (get data :rot)) 153) (q/color 226 140 95)] (+ 1 (get data :size)) 60)
(stepRow2 0 365 0 250 15 [(q/color 194 128 96) (q/color 247 (+ 100 (get data :rot)) 153) (q/color 226 140 95)] (+ 1 (get data :size)) 15)

  )

(defn render [channel]
  (if (get channel :rendering)
    (let [a (get channel :a)
          b (get channel :b)]
         (draw {:rot 100 :size 10}))
    )
  )



(defn add [channel]
  (let [x 20
        y 30
        z 40
        ttl 100]
    (if (= 0 (count @viz))
      (reset! viz []))
    (if (= ttl 0)
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky true })
      (swap! viz conj {:x x :y y :z z :ttl ttl :sticky false })))
  )

(defn updateviz [ channel]
  ; for some reason not all pills are deleted
  (reset! vizcount [])
  (dotimes [n (count @viz)]
    (if (false? (= 0 (get (get @viz n) :ttl)))
      ;decrease TTL in pill if ttl > 0
      (do
        (swap! viz update-in [n :ttl] dec)
;        (swap! linesquares update-in [n :z] (fn [x] (rand-int -670)))
        (swap! viz update-in [n :y] (fn [y] (- y 1)))
        )
      ;else mark pill for deletion
      (swap! vizcount conj n)
      ;(reset! @pills [0 9 0])
      )
    )
  (dotimes [n (count @vizcount)]
;    (println " really dropping stuff")
    (reset! viz  (drop-nth (nth @vizcount n) @viz))) )



(defn channel [channel]
  (swap! channel assoc :vizsynth add :render render :update updateviz)
;  (swap! rendering true)
  )
