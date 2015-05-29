(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]))

(defn reviewer-view [data owner]
  (reify
    om/IDisplayName
    (display-name [_] "reviewer")
    om/IInitState
    (init-state [_]
      (let [review-drafts (data/review-drafts data)]
        {:review-drafts review-drafts
         :review-draft  (last review-drafts)}))
    om/IRenderState
    (render-state [_ {:keys [review-draft]}]
      (.log js/console (clj->js review-draft))
      (html [:div {:style styles/reviewer-view}
             [:div
              {:style styles/reviewer-text}
              (:text review-draft)]
             (om/build notepad-reviewer review-draft)]))))
