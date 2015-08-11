(ns white-ink.components.notepad
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.dom :as utils.dom]
            [cljs.core.async :refer [<! sub chan tap mult put!]]
            [white-ink.utils.state :refer [make-squuid]]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.misc :as utils.misc])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]
                   [white-ink.macros :refer [process-task
                                             send-action!]]))

(defn editor-note [[note notes] owner]
  (reify
    om/IInitState
    (init-state [_]
      {:length-limit 100})
    om/IRenderState
    (render-state [_ {:keys [length-limit]}]
      (html
        [:li
         {:content-editable true
          :class-name       "editable note"
          :key              (:id note)
          :style            styles/note-editor
          :on-blur          #(do
                              (send-action! :save-note {:note  note
                                                        :notes notes
                                                        :text  (.. % -target -innerText)})
                              (.preventDefault %))
          :on-key-press     #(let [el (.-target %)
                                   cnt-txt (count (.. % -target -innerText))]
                              (when (> cnt-txt length-limit)
                                (.preventDefault %)))
          :on-key-down      #(handle-shortcuts :notepad-editor %)}
         (:text note)]))))

(defn notepad-editor [{:keys [current-session] :as draft} owner]
  (reify
    om/IInitState
    (init-state [_]
      {:focus-last-note false})
    om/IWillMount
    (will-mount [_]
      (process-task :notepad-editor
                    :new-note (fn []
                                ;; todo make save note as it is submitted, and not empty before
                                (om/transact! current-session
                                              [:current-insert :notes]
                                              #(conj % {:text        " "
                                                        :draft-index (count (-> @current-session :current-insert :text))
                                                        :id          (make-squuid)}))
                                (om/set-state! owner :focus-last-note true))))
    om/IDidUpdate
    (did-update [_ _ _]
      (when (om/get-state owner :focus-last-note)
        (doto (.-lastChild (om/get-node owner "notes"))
          (utils.dom/set-cursor-to-end)
          (utils.dom/scroll-into-view 500)
          (aset "innerHTML" ""))
        (om/set-state! owner :focus-last-note false)))
    om/IRenderState
    (render-state [_ _]
      (let [notes (-> current-session :current-insert :notes)]
        (html
          [:ul {:ref        "notes"
                :class-name "note-pad"
                :style      styles/notepad-editor}
           (for [note notes]
             (om/build editor-note [note notes]))])))))

(defn reviewer-note [note owner]
  (reify
    om/IInitState
    (init-state [_]
      {:visible?      false
       :mult-viz-chan nil})
    om/IWillMount
    (will-mount [_]
      (let [check-viz-chan (chan)]
        (tap (om/get-state owner :mult-viz-chan) check-viz-chan)
        (go-loop []
                 (when (<! check-viz-chan)
                   (om/set-state! owner :visible?
                                  (utils.dom/visible?
                                    (.getElementById js/document (utils.misc/note-draft-idx-hook-id note))))
                   (recur)))))
    om/IRenderState
    (render-state [_ {:keys [visible?]}]
      (html
        [:li {:key        (:id note)
              :class-name "reviewable note"
              :style      (if visible?
                            (assoc styles/note-reviewer :color "#323232")
                            styles/note-reviewer)
              :on-click   #(do (send-action! :reviewer :scroll-to note)
                               (.preventDefault %))}
         (:text note)]))))

(defn notepad-reviewer [notes owner]
  (reify
    om/IInitState
    (init-state [_]
      (let [check-all-viz-chan (chan)]
        {:check-all-viz-chan check-all-viz-chan
         :mult-viz           (mult check-all-viz-chan)}))
    om/IWillMount
    (will-mount [_]
      (process-task :notepad-reviewer
                    :check-notes-viz #(put! (om/get-state owner :check-all-viz-chan) true)
                    :selection-change #(om/set-state! owner :selection-range %)))
    om/IRenderState
    (render-state [_ {:keys [mult-viz]}]
      (html
        [:ul {:class-name "note-pad"
              :style      styles/notepad-reviewer}
         (for [note notes]
           (om/build reviewer-note note {:init-state {:mult-viz-chan mult-viz}}))]))))
