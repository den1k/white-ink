(ns white-ink.components.editor
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :as async]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.text :as utils.text]
            [white-ink.utils.data :as data]
            [white-ink.utils.async :refer [debounce-chan]]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [cljs.pprint :refer [pprint]])
  (:require-macros [cljs.core.async.macros :as async]
                   [white-ink.macros :refer [process-task
                                             send-action!]]))

(defn editor [{:keys [current-draft] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IInitState
    (init-state [_]
      {:debounce-action (debounce-chan 500 (om/get-shared owner :actions))})
    om/IWillMount
    (will-mount [_]
      (process-task :editor
                    :focus #(utils.dom/set-cursor-to-end (om/get-node owner "text"))))
    om/IDidUpdate
    (did-update [_ {:keys [current-draft]} _]
      ;(when (not= current-draft (:current-draft data)))
      (utils.dom/cursor->end-and-scroll (om/get-node owner "text")))
    om/IDidMount
    (did-mount [_]
      (utils.dom/cursor->end-and-scroll (om/get-node owner "text")))
    om/IRenderState
    (render-state [_ {:keys [debounce-action sessions-inserts]}]
      (let [cur-insert (data/cur-insert data)
            orig-text (data/editor-text sessions-inserts cur-insert)
            space? (= \space (last orig-text))
            text (if space? (str (utils.text/trim-right orig-text) "&nbsp;") orig-text)
            start-idx (-> current-draft :current-session :current-insert :start-idx)
            start-idx (if space? (inc start-idx) start-idx)
            ]
        ;(prn "TCT" text)
        (html [:div {:on-click   (fn [e]
                                   (send-action! :editor :focus)
                                   (.preventDefault e))
                     :class-name "editor"
                     :style      styles/editor-reviewer}
               (when (:text-grain (data/settings data))
                 [:div {:class-name "grain"
                        :style      {:height 200
                                     :width  500}}])
               [:div {:class-name "gradient"
                      :style      (select-keys styles/editor-text [:width :height])} ""]
               [:div {:style                   styles/editor-text
                      :ref                     "text"
                      :content-editable        true
                      :on-key-down             #(handle-shortcuts :editor %)
                      :on-key-press            #(do (send-action! :editor-typing)
                                                    (.stopPropagation %))
                      :on-key-up               (fn [e]
                                                 (let [text-content (.. e -target -textContent)
                                                       text-info {:orig-text    orig-text
                                                                  :text-content text-content
                                                                  :start-idx    start-idx}]
                                                   (async/put! debounce-action [:update-insert text-info]))
                                                 (.preventDefault e))
                      :dangerouslySetInnerHTML {:__html text}
                      }
                #_text]])))))

