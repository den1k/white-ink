(ns white-ink.utils.async
  (:require [cljs.core.async :refer [timeout]])
  (:require-macros [cljs.core.async.macros :refer [go-loop alt!]]))

(defn act-in-silence [wait-period]
  (fn [receive-chan f]
    (go-loop [do-time (timeout wait-period) rec-v nil]
                   (alt!
                     do-time ([_] (do (f rec-v)
                                      (recur (timeout wait-period) nil)))
                     receive-chan ([v] (recur (timeout wait-period) v))))))
