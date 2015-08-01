(ns white-ink.components.editor
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :as async]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.data :as data]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [cljs.pprint :refer [pprint]])
  (:require-macros [cljs.core.async.macros :as async]
                   [white-ink.macros :refer [process-task
                                             send-action!]]))
;; move to utils dom
(defn cursor->end-and-scroll [node]
  (doto node
    utils.dom/scroll-to-bottom
    utils.dom/set-cursor-to-end))

(defn editor [{:keys [current-draft] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IWillMount
    (will-mount [_]
      (process-task :editor
                    :focus #(utils.dom/set-cursor-to-end (om/get-node owner "text"))))
    om/IDidUpdate
    (did-update [_ {:keys [current-draft]} _]
      (when (not= current-draft (:current-draft data))
        (cursor->end-and-scroll (om/get-node owner "text"))))
    om/IDidMount
    (did-mount [_]
      (cursor->end-and-scroll (om/get-node owner "text")))
    om/IRender
    (render [_]
      (let [text (data/cur-draft->text current-draft)]
        (html [:div {:on-click   #(utils.dom/set-cursor-to-end (om/get-node owner "text"))
                     :class-name "editor"
                     :style      styles/editor-reviewer}
               (when (:text-grain (data/settings data))
                 [:div {:class-name "grain"
                        :style      {:height 200
                                     :width  500}}])
               [:div {:class-name "gradient"
                      :style      (select-keys styles/editor-text [:width :height])} ""]
               [:div {:style            styles/editor-text
                      :ref              "text"
                      :content-editable true
                      :on-key-down      #(handle-shortcuts :editor %)
                      :on-key-press     #(send-action! :editor-typing)
                      ; todo om/update is too slow on fast typing. Maybe diff impl. will be faster.
                      :on-key-up        (fn [e]
                                          (let [cursor-pos (.. js/window getSelection -anchorOffset)
                                                new-text (.. e -target -textContent)]
                                            ; persist entire text in memory and send diff of change to backend
                                            #_(om/set-state! owner :text new-text))
                                          )
                      }
                text]])))))
