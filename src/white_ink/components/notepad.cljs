(ns white-ink.components.notepad
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :refer [<! sub chan]]
            [white-ink.utils.state :refer [make-squuid]]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [white-ink.styles.styles :as styles])
  (:require-macros [white-ink.macros :refer [process-task]]))

(defn notepad-editor [{:keys [notes] :as draft} owner]
  (reify
    om/IInitState
    (init-state [_]
      :focus-last-note false)
    om/IWillMount
    (will-mount [_]
      (process-task :notepad-editor
                     :new-note (fn []
                                 (om/transact! draft [:notes] #(conj % {:text        " "
                                                                            :draft-index (count (:text draft))
                                                                            :id          (make-squuid)}))
                                 (om/set-state! owner :focus-last-note true))))
    om/IDidUpdate
    (did-update [_ _ _]
      (when (om/get-state owner :focus-last-note)
        (utils.dom/set-cursor-to-end (.-lastChild (om/get-node owner "notes")))
        (om/set-state! owner :focus-last-note false)))
    om/IRenderState
    (render-state [_ _]
      (html [:div
             [:h6 "notepad-editor"]
             [:ul {:ref "notes"}
              (for [note notes]
                [:li
                 {:content-editable true
                  :key              (:id note)
                  :style            styles/note-editor
                  ;; todo can be sent to action handler instead
                  :on-blur          #(white-ink.utils.state/save-note! {:note  note
                                                                        :notes notes
                                                                        :text  (.. % -target -innerText)})
                  :on-key-down      #(handle-shortcuts :notepad-editor {:note        note
                                                                        :draft-index (count (:text draft))} %)}
                 (:text note)])]]))))

(defn notepad-reviewer [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-reviewer"]
           [:ul (for [note notes]
                  [:li {:key   (:id note)
                        :style styles/note-reviewer}
                   (:text note)])]])))
