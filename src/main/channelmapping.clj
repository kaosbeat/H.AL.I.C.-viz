(ns main.kaos)

;;;;;
;; oscchannelmaps
;;(def ch1peak  (atom 0))



(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering true :vizsynth box/add :render box/render :update box/updateviz :debug true}))
(def ch2 (atom {:id 2 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch3 (atom {:id 3 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch4 (atom {:id 4 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch5 (atom {:id 5 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch6 (atom {:id 6 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch7 (atom {:id 7 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch8 (atom {:id 8 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch9 (atom {:id 9 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))
(def ch10 (atom {:id 10 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz :debug false}))


(def channels [ch1 ch2 ch3 ch4 ch5 ch6 ch7 ch8 ch9 ch10])
