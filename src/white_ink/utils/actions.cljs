(ns white-ink.utils.actions
  (:require [cljs.core.async :as async]
            [cljs.core.match :refer-macros [match]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn start-actions-handler [actions-chan]
  (async/go-loop []
                 (when-let [action-vec (async/<! actions-chan)]
                   ((fn [action-vec]
                      (match [action-vec]
                             [[:key-press :editor event]] (.log js/console "EVENT" event)
                             )) action-vec)
                   (recur))))

