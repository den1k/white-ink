(ns white-ink.components.editor
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.components.search :as search]
            [white-ink.utils.search :as utils.search]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :as async]
            [white-ink.utils.text :as text]
            [white-ink.styles.styles :as styles])
  (:require-macros [cljs.core.async.macros :as async]))

(defn editor [current-draft owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IInitState
    (init-state [_]
      {:text          (:text current-draft)
       :search-text   nil
       :cursor-pos    nil
       :should-update true})
    om/IWillMount
    (will-mount [_]
      (let [tx-sub (async/sub (:tx-chan (om/get-shared owner)) :editor #_(not= % :editor) (async/chan))]
        (async/go-loop []
                       (when-let [tx (async/<! tx-sub)]
                         (om/set-state! owner :text (:new-value tx))
                         (recur)))))
    om/IDidUpdate
    (did-update [_ _ {:keys [text]}]
      ;; update only if the text was externally reset
      (when (not= text (om/get-state owner :text))
        (utils.dom/set-cursor (om/get-node owner "text") (om/get-state owner :cursor-pos))))
    om/IRenderState
    (render-state [_ {:keys [text search-text]}]
      ;; TODO don't search on every render
      (let [text (if (not-empty search-text)
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
                      :on-key-up        (fn [e]
                                          (let [cursor-pos (.. js/window getSelection -anchorOffset)
                                                new-text (.. e -target -textContent)]
                                            (om/set-state! owner :cursor-pos cursor-pos)
                                            ;; breaks cursor on fast typing
                                            #_(om/set-state! owner :text new-text)
                                            ;(om/set-state! owner :should-update false)
                                            ; persist entire text in memory and send diff of change to backend
                                            (om/update! current-draft :text new-text :editor)))
                      }
                text]])))))
