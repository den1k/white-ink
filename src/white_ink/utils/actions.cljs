(ns white-ink.utils.actions
  (:require [om.core :as om]
            [cljs.core.match :refer-macros [match]]
            [cljs.core.async :refer [>! <! put!]]
            [white-ink.utils.state :refer [save-note!
                                           save-new-insert!
                                           update-cur-insert!
                                           persist-typed-text
                                           persist-scroll-offset]]
            [white-ink.utils.styles.transition :as trans]
            )
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn toggle-search [app-state tasks]
  (do (when (:searching? @app-state)
        (put! tasks [:editor :focus]))
      (om/transact! app-state :searching? not)))

(defn match-dispatch [app-state tasks action-vec]
  (match [action-vec]
         [[:key-down :app :backslash]] (toggle-search app-state tasks)
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
         [[:scrolling :reviewer]] (put! tasks [:notepad-reviewer :check-notes-viz])
         [[:persist-scroll-offset :reviewer el]] (persist-scroll-offset app-state el)

         [[:editor :focus]] (do
                              (put! tasks [:notepad-reviewer :selection-change [0 0]])
                              (put! tasks [:editor :focus]))

         [[:reviewer :search query]] (put! tasks [:reviewer :search query])
         [[:reviewer :scroll-to idx]] (put! tasks [:reviewer :scroll-to idx])

         [[:toggle-search]] (toggle-search app-state tasks)
         [[:search-off]] (do (put! tasks [:editor :focus])
                             (om/update! app-state :searching? false))

         [[:save-note note-map]] (do (save-note! note-map)
                                     (put! tasks [:editor :focus]))

         [[:editor-typing]] (trans/fade :typing app-state)

         [[:app-mouse]] (trans/fade :mouse app-state)

         [[:start-insert text idx]] (save-new-insert! app-state text idx)
         [[:persist-insert-text new-text]] (persist-typed-text app-state new-text)

         :else (.warn js/console "Unknown action: " (clj->js action-vec))
         ))


(defonce start-actions-handler
         (fn [{:keys [actions tasks]} app-state]
           (go-loop []
                    (when-let [action-vec (<! actions)]
                      (match-dispatch app-state tasks action-vec)
                      (recur)))))

