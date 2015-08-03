(ns white-ink.utils.actions
  (:require [om.core :as om]
            [cljs.core.async :refer [>! <! put!]]
            [cljs.core.match :refer-macros [match]]
            [white-ink.utils.state :refer [save-note!
                                           save-new-insert!]]
            [white-ink.utils.styles.transition :as trans])
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

                             [[:key-down :notepad-editor :return]] (put! tasks [:editor :focus])
                             [[:key-down :notepad-editor :tab]] (put! tasks [:editor :focus])
                             [[:key-down :search :right-bracket]] (put! tasks [:reviewer :search-dir :forward])
                             [[:key-down :search :left-bracket]] (put! tasks [:reviewer :search-dir :backward])

                             [[:selection-change :reviewer range]] (put! tasks [:notepad-reviewer :selection-change range])

                             [[:editor :focus]] (do
                                                  (put! tasks [:notepad-reviewer :selection-change [0 0]])
                                                  (put! tasks [:editor :focus]))

                             [[:reviewer :search query]] (put! tasks [:reviewer :search query])
                             [[:reviewer :scroll-to idx]] (put! tasks [:reviewer :scroll-to idx])

                             [[:toggle-search]] (do (when (:searching? @app-state)
                                                      (put! tasks [:editor :focus]))
                                                    (om/transact! app-state :searching? not))
                             [[:search-off]] (do (put! tasks [:editor :focus])
                                                 (om/update! app-state :searching? false))

                             [[:save-note note-map]] (do (save-note! note-map)
                                                         (put! tasks [:editor :focus]))

                             [[:editor-typing]] (trans/fade :typing app-state)
                             [[:app-mouse]] (trans/fade :mouse app-state)

                             [[:start-insert text idx]] (save-new-insert! app-state text idx)

                             :else (.warn js/console "Unknown action: " (clj->js action-vec)))) action-vec)
                   (recur))))

