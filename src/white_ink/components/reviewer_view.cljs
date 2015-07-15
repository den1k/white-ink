(ns white-ink.components.reviewer-view
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.data :as data]
            [white-ink.styles.styles :as styles]
            [white-ink.components.notepad :refer [notepad-reviewer]]
            [white-ink.utils.search :as utils.search]
            [white-ink.components.search :as search])
  (:require-macros [white-ink.macros :refer [process-task]]))

(defn count-until-pred [pred coll]
  (reduce (fn [c item]
            (if (pred item)
              (reduced c)
              (inc c))) 0 coll))

(def count-until-search-res
  (partial count-until-pred :res))

(defn next-search-idx [dir cur-idx search-results]
  ;(prn "dir" dir)
  ;(prn "cur-idx" cur-idx)
  ;(prn "dir" dir)
  ; todo broken on last index forward
  (let [steps (case dir
                :forward (-> (subvec search-results (inc cur-idx))
                             count-until-search-res
                             inc)
                :backward (-> (subvec search-results 0 cur-idx)
                              reverse
                              count-until-search-res
                              inc
                              -))
        next-idx (+ cur-idx steps)]
    (cond
      ; going backwards and no more results
      (neg? next-idx) (next-search-idx dir (count search-results) search-results)
      ; passed in cur idx lies outside of search-results
      (> next-idx (count search-results)) (next-search-idx dir 0 search-results)
      :else next-idx)
    ))

;(next-search-idx :backward 2 [:a :b {:res :bla} :d {:res :bla}])

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
         :result-idx    1
         :search-text   ""}))
    om/IWillMount
    (will-mount [_]
      (process-task :reviewer
                    :search #(let [text (om/get-state owner [:review-draft :text])]
                              #_(om/set-state! owner :search-text (utils.search/constrain-query %))
                              ;(prn (utils.search/find text (utils.search/constrain-query %)))
                              (om/set-state! owner :render-text (utils.search/find text (utils.search/constrain-query %))))
                    :search-dir #(let [cur-idx (om/get-state owner :result-idx)
                                       search-text (om/get-state owner :render-text)
                                       next-idx (next-search-idx % cur-idx search-text)]
                                  (om/set-state! owner :result-idx next-idx)
                                  (prn "next" next-idx)
                                  #_(om/update-state! owner :result-idx (fn [cur-idx]
                                                                        )))))
    om/IRenderState
    (render-state [_ {:keys [render-text review-draft result-idx]}]
      (let [render-text (update render-text result-idx assoc :selected? true)]
        ;; todo enable selected search results and go back and forth between them
        (prn (map keys render-text))
        (html [:div {:style styles/reviewer-view}
               [:div
                {:style (if-not searching?
                          styles/reviewer-text
                          styles/reviewer-text-on-search)}
                (search/results render-text)]
               (om/build notepad-reviewer review-draft)])))))
