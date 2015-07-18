(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]
            [white-ink.utils.search :as utils.search]
            [white-ink.components.search :as search]
            [white-ink.utils.dom :as utils.dom])
  (:require-macros [white-ink.macros :refer [process-task]]))

(defn reviewer-view [{:keys [searching?] :as data} owner]
  (reify
    om/IDisplayName
    (display-name [_] "reviewer")
    om/IInitState
    (init-state [_]
      (let [review-drafts (data/review-drafts data)
            review-draft (last review-drafts)]
        {:review-drafts review-drafts
         :review-draft  review-draft
         :render-text   [review-draft]
         :result-idx    0}))
    om/IDidUpdate
    (did-update [_ _ _]
      (let [parent (om/get-node owner "review-draft")
            result-idx (om/get-state owner :result-idx)]
        (if-not result-idx
          ;; can use om/update and set if not nil
          (om/set-state! owner :result-idx
                         (utils.dom/first-visible-or-closest-idx parent)))
        ))
    om/IWillMount
    (will-mount [_]
      (process-task :reviewer
                    :search #(let [text (om/get-state owner [:review-draft :text])
                                   query (utils.search/constrain-query %)]
                              (when-let [results (utils.search/find text query)]
                                ;; find first visible result on i-did-update and set idx to it
                                (om/set-state! owner :result-idx nil)
                                (om/set-state! owner :render-text results)))
                    :search-dir #(let [cur-idx (om/get-state owner :result-idx)
                                       search-text (om/get-state owner :render-text)
                                       next-idx (utils.search/next-res-idx % cur-idx search-text)]
                                  (om/set-state! owner :result-idx next-idx))))
    om/IRenderState
    (render-state [_ {:keys [render-text review-draft result-idx]}]
      (let [render-text (if (and searching? result-idx)
                          (update render-text result-idx assoc :selected? true)
                          render-text)]
        ;; todo enable selected search results and go back and forth between them
        ;(prn (map keys render-text))
        (html [:div {:style styles/reviewer-view}
               [:div
                {:ref "review-draft"
                 :style (if-not searching?
                          styles/reviewer-text
                          styles/reviewer-text-on-search)}
                (search/results render-text)]
               (om/build notepad-reviewer review-draft)])))))
