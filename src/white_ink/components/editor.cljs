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


(defn editor [{:keys [current-draft] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IInitState
    (init-state [_]
      {:text-persist-chan (async/chan (async/sliding-buffer 1))})
    om/IWillMount
    (will-mount [_]
      (let [persist-chan (om/get-state owner :text-persist-chan)
            cur-insert (-> current-draft :current-session :current-insert)]
        (async/go-loop [persist (async/timeout 5000)]
                       (async/<! persist)
                       (when-let [v (async/<! persist-chan)]
                         (om/update! cur-insert :text v)
                         (prn "recurring")
                         (recur (async/timeout 5000)))
                       ))
      (process-task :editor
                    :focus #(utils.dom/set-cursor-to-end (om/get-node owner "text"))))
    om/IDidUpdate
    (did-update [_ {:keys [current-draft]} _]
      (when (not= current-draft (:current-draft data))
        (utils.dom/cursor->end-and-scroll (om/get-node owner "text"))))
    om/IDidMount
    (did-mount [_]
      (utils.dom/cursor->end-and-scroll (om/get-node owner "text")))
    om/IRenderState
    (render-state [_ {:keys [text-persist-chan]}]
      (let [text (data/cur-draft->text current-draft)
            cur-insert (-> current-draft :current-session :current-insert)
            start-idx (-> current-draft :current-session :current-insert :start-idx)]
        (prn cur-insert)
        (html [:div {:on-click   #(send-action! :editor :focus)
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
                      :on-key-press     #(do (send-action! :editor-typing)
                                             (.stopPropagation %))
                      ; todo om/update is too slow on fast typing. Maybe diff impl. will be faster.
                      :on-key-up        (fn [e]
                                          (let [new-text (.. e -target -textContent)]
                                            ;; still laggy persistence, and drops part of string during rerender
                                            (async/put! text-persist-chan (subs new-text start-idx))
                                            ;(om/update! cur-insert :text (subs new-text start-idx))
                                            ; persist entire text in memory and send diff of change to backend
                                            #_(om/set-state! owner :text new-text))
                                          )
                      }
                text]])))))
