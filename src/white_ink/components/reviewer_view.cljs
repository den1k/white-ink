(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]
            [white-ink.utils.search :as utils.search]
            [white-ink.components.search :as search]
            [white-ink.utils.dom :as utils.dom]
            [white-ink.utils.misc :as utils.misc]
            [white-ink.utils.components.reviewer-view :refer [insert-note-hooks]])
  (:require-macros [white-ink.macros :refer [process-task
                                             send-action!]]))

(defn review-draft-view [{:keys [searching?] :as data} owner]
  (reify
    om/IInitState
    (init-state [_]
      (let [draft-text (data/cur-sessions->text data)
            notes (data/merge-sort-notes data)]
        {:draft-text  draft-text
         :render-text (insert-note-hooks [[:text draft-text]] notes)
         :result-idx  nil
         :notes       notes}))
    om/IWillMount
    (will-mount [_]
      (process-task :reviewer
                    :search #(let [text (om/get-state owner :draft-text)
                                   query (utils.search/constrain-query %)]
                              (when-let [results (utils.search/find text query)]
                                (om/update-state! owner
                                                  (fn [state] (-> state
                                                                  ;; new results, reset result idx
                                                                  (assoc :result-idx nil)
                                                                  ;; insert notes after results
                                                                  ;; since the number of results will in most cases
                                                                  ;; exceed the number of notes.
                                                                  (assoc :render-text (insert-note-hooks results (om/get-state owner :notes))))))))
                    :search-dir (fn [dir]
                                  (let [{:keys [render-text result-idx]} (om/get-state owner)
                                        next-idx (and result-idx (utils.search/next-res-idx dir result-idx render-text))]
                                    (when next-idx
                                      (om/set-state! owner :result-idx next-idx))))
                    :scroll-to (fn [note]
                                 (let [target (utils.misc/note-draft-idx-hook-id note)]
                                   (utils.dom/scroll-into-view (.getElementById js/document target) 7 500)))))
    om/IDidMount
    (did-mount [_]
      (let [node (om/get-node owner "review-draft")]
        (utils.dom/animate-scroll node (:review-scroll-top data) 2000)))
    om/IDidUpdate
    (did-update [_ _ _]
      (let [parent (om/get-node owner "review-draft")
            result-idx (om/get-state owner :result-idx)]
        (cond
          (and searching? (nil? result-idx)) (when-let [vis-idx (utils.dom/first-visible-or-closest-idx parent)]
                                               (om/set-state! owner :result-idx vis-idx)))))
    om/IRenderState
    (render-state [_ {:keys [draft-text render-text result-idx scroll-target-idx notes]}]
      (let [render-text (cond-> render-text
                                ;; order of these matters because the result idx is determined after initial render
                                ;true (insert-notes notes)
                                (and searching? result-idx) (update result-idx conj :selected)
                                )]
        (html
          [:div {:style styles/editor-reviewer}
           [:div
            {:ref           "review-draft"
             :style         styles/reviewer-text
             ;; potential perf bottleneck, especially during scrolling b/c no selection event in html
             :on-wheel      (fn [e]
                              (send-action! :scrolling :reviewer)
                              #_(let [{:keys [start end]} (utils.dom/get-selection)]
                                (when (or (= 0 start end) (not= start end))
                                  (send-action! :selection-change :reviewer [start end])))
                              (.stopPropagation e))
             :on-mouse-down #(do (send-action! :selection-change :reviewer [0 0])
                                 (utils.dom/set-selection (om/get-node owner "review-draft") 0 0))
             :on-mouse-over #(let [{:keys [start end]} (utils.dom/get-selection)]
                              (when (or (= 0 start end) (not= start end))
                                (send-action! :selection-change :reviewer [start end]))
                              (.stopPropagation %))
             :on-mouse-up   #(let [{:keys [start end type]} (utils.dom/get-selection)]
                              (case type
                                :caret (send-action! :start-insert draft-text start)
                                :range (send-action! :selection-change :reviewer [start end]))
                              (.preventDefault %))
             }
            (search/results render-text)]])))))



(defn reviewer-view [{:keys [speed->opacity] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "reviewer-view")
    om/IRender
    (render [_]
      (html [:div {:style (assoc styles/reviewer-view :opacity speed->opacity)}
             (om/build review-draft-view data)
             (om/build notepad-reviewer (data/merge-sort-notes data))]))))
