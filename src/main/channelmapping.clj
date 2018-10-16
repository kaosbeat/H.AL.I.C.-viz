(ns main.kaos)

;;;;;
;; oscchannelmaps
;;(def ch1peak  (atom 0))

(def ch1 (atom {:id 1 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering true :vizsynth box/add :render box/render :update box/updateviz}))
(def ch2 (atom {:id 2 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch3 (atom {:id 3 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch4 (atom {:id 4 :freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch5 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch6 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch7 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch8 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch9 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))
(def ch10 (atom {:freq 0.0 :peak 0.0 :beatnumber 0 :x 0 :y 0 :z 0 :a 0 :b 0 :c 0 :d 0 :rendering false :vizsynth box/add :render box/render :update box/updateviz}))









;(def instruments ( atom {:ch1 box/add :ch2 box/add}))
