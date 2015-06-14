(ns white-ink.components.notepad
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :refer [<! sub chan]]
            [white-ink.utils.state :refer [make-squuid]]
            [white-ink.utils.shortcuts :refer [handle-shortcut]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn notepad-editor [{:keys [notes] :as draft} owner]
  (reify
    om/IInitState
    (init-state [_]
      :focus-last-note false)
    om/IWillMount
    (will-mount [_]
      (let [event-chan (sub (om/get-shared owner :events-pub) :notepad-editor (chan))]
        (async/go-loop []
                       (when-let [event (last (<! event-chan))]
                         (print "EVENT" event)
                         (case event
                           :new-note (om/transact! draft [:notes] #(conj % {:text        " "
                                                                            :draft-index (count (:text draft))
                                                                            :id          (make-squuid)})))
                         (om/set-state! owner :focus-last-note true)

                         (recur)))))
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
                  ;; todo pass less detail, too specific for shortcut handler
                  :on-key-down      #(handle-shortcut :notepad-editor {:note note
                                                                       :new-text (.. % -target -innerText)} %)}
                 (:text note)])]]))))

(defn notepad-reviewer [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-reviewer"]
           [:ul (for [note notes]
                  [:li {:key (:id note)}
                   (:text note)])]])))
