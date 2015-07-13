(ns white-ink.utils.actions
  (:require [om.core :as om]
            [cljs.core.async :refer [>! <! put!]]
            [cljs.core.match :refer-macros [match]]
            [white-ink.utils.state :refer [save-note!]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn start-actions-handler [{:keys [actions tasks]} app-state]
  (async/go-loop []
                 (when-let [action-vec (<! actions)]
                   ((fn [action-vec]
                      (match [action-vec]
                             [[:key-down :app :backslash]] (recur [:toggle-search])
                             [[:key-down :editor :tab]] (put! tasks [:notepad-editor :new-note])

                             [[:key-down :editor :arrow-left]] (constantly nil)
                             [[:key-down :editor :arrow-up]] (constantly nil)
                             [[:key-down :editor :arrow-right]] (constantly nil)
                             [[:key-down :editor :arrow-down]] (constantly nil)
                             ;; todo it persists note, impl refocus editor
                             [[:key-down :notepad-editor :return opts]] (do (save-note! opts)
                                                                            (put! tasks [:editor :focus]))
                             [[:reviewer :search query]] (put! tasks [:reviewer :search query])

                             [[:toggle-search]] (do (when (:searching? @app-state)
                                                      (put! tasks [:editor :focus]))
                                                    (om/transact! app-state :searching? not))
                             [[:search-off]] (do (put! tasks [:editor :focus])
                                                 (om/update! app-state :searching? false))

                             :else (.warn js/console "Unknown action: " (clj->js action-vec)))) action-vec)
                   (recur))))
