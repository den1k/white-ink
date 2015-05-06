(ns ^:figwheel-always white-ink.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn- notes-gen [n]
  (for [n (range 1 n)]
    {:text (str "note " n)}))

(def mock-notes (notes-gen 14))
;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:user   nil
                          :drafts [{:text  "previous - first draft"
                                    :notes mock-notes}
                                   {:text  "current - second draft"
                                    :notes mock-notes}]}))

(defn current-draft [data]
  (-> data :drafts last))

(defn review-drafts [data]
  (-> data :drafts butlast))

(defn notepad-editor [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-editor"]
           [:ul (for [note notes]
                  [:li {:content-editable true}
                   (:text note)])]])))

(defn notepad-reviewer [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-reviewer"]
           [:ul (for [note notes]
                  [:li (:text note)])]])))

(defn editor [{:keys [text] :as current-draft} owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IDidMount
    (did-mount [_]
      (let [node (om/get-node owner)]
        (set! (. node -innerHTML) text)))
    om/IRender
    (render [_]
      (html [:textarea {:on-key-down #(print "persist text/diff")}]))))

(def styles-texts-notepad
  "I want to be moved to a style namespace"
  {:display :flex
   :justify-content :space-around})

(defn editor-view [data owner]
  (om/component
    (let [current-draft (-> data current-draft)]
      (html [:div {:style styles-texts-notepad}
             (om/build editor current-draft)
             (om/build notepad-editor current-draft)]))))

(defn reviewer-view [data owner]
  (reify
    om/IDisplayName
    (display-name [_] "reviewer")
    om/IInitState
    (init-state [_]
      (let [review-drafts (review-drafts data)]
        {:review-drafts review-drafts
         :review-draft  (last review-drafts)}))
    om/IRenderState
    (render-state [_ {:keys [review-draft]}]
      (html [:div {:style styles-texts-notepad}
             (:text review-draft)
             (om/build notepad-reviewer review-draft)]))))


(defn app [data owner]
  (om/component
    (dom/div nil
             (om/build editor-view data)
             (om/build reviewer-view data))))

(om/root
  (fn [data owner]
    (reify om/IRender
      (render [_]
        (om/build app data))))
  app-state
  {:target (. js/document (getElementById "app"))})


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


