(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]
            [white-ink.utils.search :as utils.search]
            [white-ink.components.search :as search]
            [white-ink.utils.dom :as utils.dom]
            [white-ink.utils.misc :as utils.misc])
  (:require-macros [white-ink.macros :refer [process-task]]))

(defn split-result [results scroll-target-idx]
  (let [[res-idx idx-text idx-char] (utils.misc/containing-idxs-for-text-idx scroll-target-idx results)
        res (get results res-idx)
        res-text (get res idx-text)
        [text-1 text-2] ((juxt #(subs % 0 idx-char) #(subs % idx-char)) res-text)]
    ;; todo cases where item selected (can't have two selected)
    (vec (concat (subvec results 0 res-idx)
                 [(assoc res idx-text text-1)
                  [:scroll-target scroll-target-idx]
                  (assoc res idx-text text-2)]
                 (subvec results (inc res-idx))))))

(defn review-draft-view [{:keys [searching?] :as data} owner]
  (reify
    om/IInitState
    (init-state [_]
      (let [draft (data/current-draft data)]
        {:review-draft      draft
         :render-text       [[:text (:text draft)]]
         :result-idx        nil
         :scroll-target-idx nil}))
    om/IDidUpdate
    (did-update [_ _ _]
      (let [parent (om/get-node owner "review-draft")
            {:keys [result-idx]} (om/get-state owner)]
        (cond
          (and searching? (nil? result-idx)) (when-let [vis-idx (utils.dom/first-visible-or-closest-idx parent)]
                                               (om/set-state! owner :result-idx vis-idx)))))
    om/IWillMount
    (will-mount [_]
      (process-task :reviewer
                    :search #(let [text (om/get-state owner [:review-draft :text])
                                   query (utils.search/constrain-query %)]
                              (when-let [results (utils.search/find text query)]
                                ;; find first visible result on i-did-update and set idx to it
                                (om/update-state! owner
                                                  (fn [state] (-> state
                                                                  ;; reset a bunch of state
                                                                  (assoc :result-idx nil)
                                                                  (assoc :scroll-target-idx nil)
                                                                  (assoc :render-text results))))))
                    :search-dir (fn [dir]
                                  (let [{:keys [render-text result-idx]} (om/get-state owner)
                                        next-idx (and result-idx (utils.search/next-res-idx dir result-idx render-text))]
                                    (when next-idx
                                      (om/set-state! owner :result-idx next-idx))))
                    :scroll-to (fn [target-idx]
                                 (om/set-state! owner :scroll-target-idx target-idx)
                                 #_(om/update-state! owner :render-text #(split-result % target-idx)))))
    om/IRenderState
    (render-state [_ {:keys [render-text result-idx scroll-target-idx]}]
      (let [render-text (cond-> render-text
                                ;; todo find solution for offset idx when length increased due to scroll idx
                                (and searching? result-idx) (update result-idx conj :selected)

                                scroll-target-idx (split-result scroll-target-idx)
                                )]
        (html
          [:div {:style styles/editor-reviewer}
           [:div
            {:ref   "review-draft"
             :style styles/reviewer-text}
            (search/results render-text)]])))))



(defn reviewer-view [{:keys [speed->opacity] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "reviewer-view")
    om/IInitState
    (init-state [_]
      (let [draft (data/current-draft data)]
        {:review-draft draft}))
    om/IRenderState
    (render-state [_ {:keys [review-draft]}]
      (html [:div {:style (assoc styles/reviewer-view :opacity speed->opacity)}
             (om/build review-draft-view data)
             (om/build notepad-reviewer review-draft)]))))
