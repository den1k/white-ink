(ns white-ink.utils.async
  (:require [cljs.core.async :refer [chan timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop alt!]]))

(defn act-in-silence
  ([wait-period f] (act-in-silence wait-period f (chan)))
  ([wait-period f in-chan]
   (go-loop [do-time (timeout wait-period) rec-v nil]
            (alt!
              do-time ([_] (do (f rec-v)
                               (recur (timeout wait-period) nil)))
              in-chan ([v] (recur (timeout wait-period) v))))
   in-chan))
