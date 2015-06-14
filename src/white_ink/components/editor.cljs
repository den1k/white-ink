(ns white-ink.components.editor
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.components.search :as search]
            [white-ink.utils.search :as utils.search]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :as async]
            [white-ink.utils.text :as text]
            [white-ink.styles.styles :as styles]
            [dommy.core :as dommy]
            [white-ink.utils.shortcuts :refer [handle-shortcut]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn editor [current-draft owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IInitState
    (init-state [_]
      {:text          (:text current-draft)
       :search-text   nil
       :should-update true})
    om/IWillMount
    (will-mount [_]
      (let [tx-sub (async/sub (:tx-chan (om/get-shared owner)) :editor (async/chan))]
        (async/go-loop []
                       (when-let [tx (async/<! tx-sub)]
                         (om/set-state! owner :text (:new-value tx))
                         (recur)))))
    om/IDidMount
    (did-mount [_]
      (let [text (om/get-state owner "text")
            node (om/get-node owner "text")]
        (dommy/listen! node :focus #(utils.dom/set-cursor-to-end node))))
    om/IDidUpdate
    (did-update [_ _ {:keys [text]}]
      ;; update only if the text was externally reset
      (let [new-text (om/get-state owner :text)]
        (when (not= text new-text)
          (utils.dom/set-cursor (om/get-node owner "text") (count new-text)))))
    om/IRenderState
    (render-state [_ {:keys [text search-text]}]
      (let [text (if (seq search-text)
                   (search/results (utils.search/find text search-text))
                   text)]
        (html [:div
               [:input {:type        "text"
                        :placeholder "Search"
                        :value       search-text
                        :on-change   #(om/set-state! owner :search-text (.. % -target -value))}]
               [:div {:style            styles/editor-text
                      :ref              "text"
                      :content-editable true
                      :on-click         #(utils.dom/set-cursor-to-end (om/get-node owner "text"))
                      :on-key-down      (partial handle-shortcut owner :editor)
                      ; todo om/update is too slow on fast typing. Maybe diff impl. will be faster.
                      :on-key-up        (fn [e]
                                          (let [cursor-pos (.. js/window getSelection -anchorOffset)
                                                new-text (.. e -target -textContent)]
                                            ; persist entire text in memory and send diff of change to backend
                                            (om/update! current-draft :text new-text :editor)))
                      }
                text]])))))
