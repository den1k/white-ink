(ns ^:figwheel-always white-ink.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.state :refer [app-state]]
            [white-ink.utils.text :as text]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :as async]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(defn current-draft [data]
  (-> data :drafts last))

(defn review-drafts [data]
  (-> data :drafts butlast))

(defn notepad-editor [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-editor"]
           [:ul (for [note notes]
                  [:li
                   {:content-editable true
                    :key              (:id note)}
                   (:text note)])]])))

(defn notepad-reviewer [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-reviewer"]
           [:ul (for [note notes]
                  [:li {:key (:id note)}
                   (:text note)])]])))

(def styles-texts-notepad
  "I want to be moved to a style namespace"
  {:display        :flex
   :justifyContent :space-around})

(def styles-texts
  {:width 500})

(defn search-result-view [text owner]
  (om/component
    (html [:span
           {:style {:background "lightgrey"}}
           text])))

(defn search->html [results]
  (for [res results
        :let [search-res (:res res)]]
    (if search-res
      (om/build search-result-view search-res)
      (:text res))))

;var setSelection = function(elem, start, end) {
;                                               var range = document.createRange();
;                                                   range.setStart(elem.firstChild, start);
;                                                   range.setEnd(elem.firstChild, end);
;                                                   var sel = window.getSelection();
;                                               sel.removeAllRanges();
;                                               sel.addRange(range);
;                                               };

(defn set-selection [node start end]
  (let [range (doto
                (.createRange js/document)
                (.setStart (.-firstChild node) start)
                (.setEnd (.-firstChild node) end))
        sel (doto
              (.getSelection js/window)
              .removeAllRanges
              (.addRange range))]))

(defn set-cursor [node idx]
  (set-selection node idx idx))

(defn editor [current-draft owner]
  (reify
    om/IDisplayName
    (display-name [_] "editor")
    om/IInitState
    (init-state [_]
      {:text          (:text current-draft)
       :search-text   nil
       :cursor-pos    nil
       :should-update true})
    om/IWillMount
    (will-mount [_]
      (let [tx-sub (async/sub (:tx-chan (om/get-shared owner)) :all #_(not= % :editor) (async/chan))]
        (async/go-loop []
                       (when-let [[tag tx] (async/<! tx-sub)]
                         (when-not (= :editor tag)
                           (om/set-state! owner :text (:new-value tx)))
                         (recur)))))
    om/IDidUpdate
    (did-update [_ _ {:keys [text]}]
      (when (not= text (om/get-state owner :text))
        (set-cursor (om/get-node owner "text") (om/get-state owner :cursor-pos))))
    om/IRenderState
    (render-state [_ {:keys [text search-text]}]
      ;; don;t search on every render
      (let [text (if (not-empty search-text)
                   (search->html (text/search text search-text))
                   text)]
        (html [:div {:style styles-texts}
               [:input {:type        "text"
                        :placeholder "Search"
                        :value       search-text
                        :on-change   #(om/set-state! owner :search-text (.. % -target -value))}]
               [:div {:style            (assoc styles-texts :height 200
                                                            :overflow "auto")
                      :ref              "text"
                      :content-editable true
                      :on-key-up        (fn [e]
                                          (let [cursor-pos (.. js/window getSelection -anchorOffset)]
                                            (om/set-state! owner :cursor-pos cursor-pos)
                                            ;(om/set-state! owner :should-update false)
                                            ; persist entire text in memory and send diff of change to backend
                                            (om/update! current-draft :text (.. e -target -textContent) :editor)))
                      }
                text]])))))

(defn editor-view [data owner]
  (om/component
    (let [current-draft (-> data current-draft)]
      (html [:div
             [:button {:on-click #(om/update! current-draft :text "horses in nevada" :not-editor)} "update text externally"]
             [:div {:style styles-texts-notepad}
              (om/build editor current-draft)
              (om/build notepad-editor current-draft)]]))))

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
      (html [:div {:style (assoc styles-texts-notepad :marginTop 10)}
             [:div {:style styles-texts}
              (:text review-draft)]
             (om/build notepad-reviewer review-draft)]))))


(defn app [data owner]
  (om/component
    (dom/div nil
             (om/build editor-view data)
             (om/build reviewer-view data))))

(let [transactions (async/chan)
      transactions-pub (async/pub transactions (fn [_] :all))]
  (om/root
    (fn [data owner]
      (reify om/IRender
        (render [_]
          (om/build app data))))
    app-state
    {:target    (. js/document (getElementById "app"))
     :tx-listen (fn [tx] (async/put! transactions [(:tag tx) tx]))
     :shared    {:tx-chan transactions-pub}}))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


