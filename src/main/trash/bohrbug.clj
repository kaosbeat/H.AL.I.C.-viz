(ns bossig.core
  (:use [overtone.live]
        [overtone.inst.piano]
        [overtone.inst.drum]
        [overtone.inst.io]
        [overtone.inst.synth]
         )
  )

(piano)

(definst kickA [freq 105 dur 1.2 width 0.5 amp -20 out-bus 0]
  (let [freq-env (* freq (env-gen (perc 0.02 (* 0.49 dur))))
        env (env-gen (perc 0.019 dur) 1 1 0 1 FREE)
        sqr (* (env-gen (perc 0 0.0800)) (pulse (* 2 freq) width))
        src (sin-osc freq-env)
        src2 (sin-osc-fb freq-env)
        filt (lpf (+ sqr src src2) 100)
        drum (+ sqr (* env filt))]

    (compander drum drum 0.2 1 0.1 0.01 0.01)
    )
  )
(kickA)

;; why is kickA grey ?

(define kickdisto (inst-fx! kickA fx-distortion2))
(ctl kickdisto (:amount 0.70))

(midi-connected-devices)

(defsynth fmtones [carrier 440 divisor 8.0 depth 8.0 out-bus 0]
  (let [modulator (/ carrier divisor)
        mod-env (env-gen (lin-rand -0.2 0.4 -2.8))
        amp-env (env-gen (lin 0 -0.2 0.1 1 ) :action FREE)
        filt (glitch-rhpf (+ carrier modulator ) 100 2.6)
             ]
      (out out-bus (pan2 (* 0.60 amp-env
                          (sin-osc (+ carrier
                                     (* mod-env (* carrier depth) (sin-osc  modulator)))))))))
;  Chaos  ()
;  Line   (amp-comp, amp-comp-a, k2a, line )
;  Random (rand-seed, lonrenz-trig )
;  Noise  (lf-noise, hasher , mantissa-mask)



(on-event [:midi :note-on]
          (fn [e]
            (let [note (:note e)
                  vel  (:velocity e)]
              (fmtones note vel)))
          ::keyboard-handler)

(event-debug-on)
(event-debug-off)


;; error : Unable to resolve symbol: define in this context ??
