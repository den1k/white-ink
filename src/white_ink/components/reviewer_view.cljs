(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]
            [white-ink.utils.search :as utils.search]
            [white-ink.components.search :as search])
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
         :text          (:text review-draft)
         :search-text   nil}))
    om/IWillMount
    (will-mount [_]
      (process-task :reviewer
                    ;; todo impl delete in search. still consider to render input field off-screen
                    :search (fn [char] (if (seq (om/get-state owner :search-text))
                                         (om/update-state! owner :search-text #(-> (concat % char)
                                                                                   clojure.string/join))
                                         (om/set-state! owner :search-text char)))))
    om/IRenderState
    (render-state [_ {:keys [text review-draft search-text]}]
      (let [text (if (seq search-text)
                   (search/results (utils.search/find text search-text))
                   text)]
        (html [:div {:style styles/reviewer-view}
               [:div
                {:style (if-not searching?
                          styles/reviewer-text
                          styles/reviewer-text-on-search)}
                text]
               (om/build notepad-reviewer review-draft)])))))
