(ns white-ink.utils.styles.transition
  (:require [om.core :as om]
            [white-ink.utils.data :refer [metrics]]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :as async]))

(defmulti set-opacity
          (fn [type & args] type))

(defmethod set-opacity :move
  [_ app-state]
  (let [cur-op (:speed->opacity @app-state)]
    (when (> 1 cur-op)
      (om/update! app-state [:speed->opacity] (+ 0.05 cur-op)))))

(defmethod set-opacity :speed
  [_ dir speed app-state]
  (let [avg (-> @app-state metrics :avg-typing-speed)
        cur-opa (-> @app-state :speed->opacity)
        sp->opa (- 1 (/ speed avg))]
    (when (and (pos? sp->opa) (>= 1 sp->opa))
      (when
        (or (and (= dir :inc) (< sp->opa cur-opa))
            (and (= dir :dec) (> sp->opa cur-opa))) (om/update! app-state [:speed->opacity] sp->opa)))))


(defn char-speed->opacity [char-speed app-state]
  "http://www.diigo.com/cached?url=http%3A%2F%2Fsmallbusiness.chron.com%2Fgood-typing-speed-per-minute-71789.html"
  (let [avg (-> @app-state metrics :avg-typing-speed)
        cur-opa (-> @app-state :speed->opacity)
        sp->opa (- 1 (/ char-speed avg))]
    (prn "spopa" sp->opa)
    (cond
      (and (pos? sp->opa) (>= 1 sp->opa)) (om/update! app-state [:speed->opacity] sp->opa)
      (zero? cur-opa) (om/update! app-state [:speed->opacity] 0))))

(defn fade*
  "On fast typing text should fade out with a delay of `(-> @app-state :text-fade-delay)`.
  When the mouse is moved quickly text should fade in and outstanding fade events should be
  cancelled. However only if there were enough mouse move events to completely remove the fade,
  else outstanding fade in events should perceed as usual."
  []
  (let [speed (atom 0)]
    (fn [source app-state]
      (let [avg (-> @app-state metrics :avg-typing-speed)
            cur-op (:speed->opacity @app-state)]
        (case source
          :mouse (let [n-op (+ 0.05 cur-op)
                       n-op (if (> 1 n-op) n-op 1)]
                   (om/update! app-state [:speed->opacity] n-op)
                   (when (> n-op 0.6)
                     (reset! speed 0)))
          :typing (async/go
                    (let [n-op (- 1 (/ (inc @speed) avg))]
                      (when (and (> 1 n-op) (>= n-op))
                        (om/update! app-state [:speed->opacity] (- 1 (/ (swap! speed inc) avg)))
                        (async/<! (async/timeout (-> @app-state :text-fade-delay)))
                        ;; todo when speed val on inc was bigger than current speed-val, don't dec
                        (when (> @speed 0)
                          (let [n-op (- 1 (/ (swap! speed dec) avg))]
                            (when (> 1 n-op cur-op)
                              (om/update! app-state [:speed->opacity] n-op))))))))))))

(def fade (fade*))
