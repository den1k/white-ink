(ns white-ink.utils.async
  (:require [cljs.core.async :refer [>! chan timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop alt!]]))

(defn take-debounce
  ([wait-period f] (take-debounce wait-period f (chan)))
  ([wait-period f in-chan]
   (go-loop [do-time (timeout wait-period) rec-v nil]
            (alt!
              do-time ([_] (do (f rec-v)
                               (recur (timeout wait-period) nil)))
              in-chan ([v] (recur (timeout wait-period) v))))
   in-chan))

(defn limit-throughput-fn [type]
  (fn do-it
    ([wait-period to-chan] (do-it wait-period (chan) to-chan))
    ([wait-period from-chan to-chan]
     (go-loop [do-time (timeout wait-period) rec-v nil]
              (alt!
                do-time ([_] (do (when rec-v
                                   (>! to-chan rec-v))
                                 (recur (timeout wait-period) nil)))
                from-chan ([v] (case type
                                 :throttle (recur do-time v)
                                 :debounce (recur (timeout wait-period) v)))))
     from-chan)))

(def throttle-chan
  "Given a wait period gurantees that a value will be put onto
  to-chan once per wait cycle. Takes an optional channel to take from
  or will make one. Returns from-chan."
  (limit-throughput-fn :throttle))

(def debounce-chan
  (limit-throughput-fn :debounce))