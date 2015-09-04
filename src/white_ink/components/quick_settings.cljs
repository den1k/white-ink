(ns white-ink.components.quick-settings
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.utils.state :as utils.state]

    #_[white-ink.utils.shortcuts :refer [handle-shortcuts]])
  (:require-macros [cljs.core.async.macros :as async]
                   [white-ink.macros :refer [send-action!]]))

(defn shortcut [[name key description?] owner]
  (om/component
    (html
      [:div {:style {:display        "flex"
                     :justifyContent "space-between"
                     :marginBottom   5}}
       [:span name] [:span {:style {:border       "1px solid whitesmoke"
                                    :background   "white"
                                    :borderRadius "5px"
                                    :padding      "5px 10px"}}
                     key]])))

(defn sub-shortcut-wrapper [& shortcuts]
  [:div {:style {:marginLeft   30
                 :marginBottom 10}}
   shortcuts])

(defn choose [items-map chosen]
  (into {} (map (fn [[k _]]
                  (if (= chosen k)
                    {k true}
                    {k false}))) items-map))

(defn select-menu-item [submenus selected]
  (om/transact! submenus #(choose % selected)))

;; todo move to styles
(def menu-item-style
  {:outline    "none"
   :border     "1px solid whitesmoke"
   :background "initial"
   ;:fontSize   "1.1rem"
   ;:borderRadius 2
   :cursor     "pointer"
   })

(defn quick-settings-title-button [[name key menu] owner]
  (reify
    om/IRender
    (render [_]
      (html
        [:button {:onClick #(select-menu-item menu key)
                  :style   (if (get menu key)
                             (merge menu-item-style
                                    {:background "rgb(140, 140, 148)"
                                     :color      "white"})
                             menu-item-style)}
         name]))))

(defn build-titles [{:keys [quick-settings drafts]}]
  (let [items (:items quick-settings)
        titles (remove nil? [["Current Document" :current-document? items]
                             (when (seq drafts)
                               ["Documents" :documents? items])
                             ["Shortcuts" :keyboard-shortcuts? items]
                             ["Settings" :settings? items]])]
    (om/build-all quick-settings-title-button titles)))

(defn quick-settings-button [label f]
  [:button {:className "quick-settings-button"
            :onClick   #(f)
            :style     {:border       "1px solid whitesmoke"
                        :outline      "none"
                        :background   "white"
                        :boxShadow    "0 0.3px 0.1px 0.1px black"
                        :borderRadius "5px"
                        :padding      "5px 10px"
                        :cursor       "pointer"}}
   label])

(defn current-document [{:keys [user quick-settings current-draft] :as data}]
  (let [{:keys [title start-date]} current-draft
        author (str (:first-name user) \space (:last-name user))]
    [:div {:style {:display        "flex"
                   :flex-direction "column"
                   :align-items    "center"}}
     [:div {:style {:display        "flex"
                    :justifyContent "space-between"
                    :alignItems     "center"}}
      (quick-settings-button "Merge Session" (fn []
                                               (utils.state/merge-current-session data)
                                               (om/update! quick-settings :show? false)))
      [:p {:style {:width "50%"}}
       "Merge all current writing progress into your working-draft."]]
     [:hr {:style {:width  "100%"
                   :margin "10px 0"
                   ;:border "1px solid green"
                   }}]
     [:div {:style {:display       "flex"
                    :flexDirection "column"
                    :width         "70%"}}
      [:div {:style {:alignSelf "center"
                     :fontSize  "1.2rem"
                     :margin    "20px 0"}}
       title]
      [:div {:style {:display        "flex"
                     :justifyContent "space-between"}}
       [:span "Written by"] [:span author]]
      [:div {:style {:display        "flex"
                     :justifyContent "space-between"}}
       [:span "Started on"] [:span start-date]]]]))

(defn keyboard-shortcuts []
  [:div {:style {:width "80%"
                 ;:fontSize "1.6rem"
                 }}
   (om/build shortcut ["New Note" "tab"])
   (sub-shortcut-wrapper
     (om/build shortcut ["Submit" "return"]))
   (om/build shortcut ["Search" "\\"])
   (sub-shortcut-wrapper
     (om/build shortcut ["Next Result" "]"])
     (om/build shortcut ["Previous Result" "["]))
   ])

(defn documents [{:keys [drafts]}]
  [:div
   ;; alfred / spotlight-like search interface with filtered document list
   [:input {:type      "text"
            :autoFocus true
            :style     {:background   "skyblue"
                        :outline      "none"
                        :border       "none"
                        :borderRadius "5px"
                        :fontFamily   "\"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;"
                        :height       50
                        :width        500
                        :textAlign    "baseline"
                        :fontSize     "1.6rem"}}]
   (for [draft drafts
         :let [title (:title draft)]]
     [:div title])])

(defn quick-settings-view [{:keys [quick-settings] :as data} owner]
  (reify
    om/IInitState
    (init-state [_]
      {})
    om/IRenderState
    (render-state [_ {:keys []}]
      (let [items (:items quick-settings)
            {:keys [current-document?
                    documents?
                    keyboard-shortcuts?
                    settings?]} items]
        (html [:div
               {:className "quick-settings"
                :style     {:position      "absolute"
                            :margin        "auto"
                            :top           0
                            :bottom        "50%"
                            :left          0
                            :right         0
                            :z-index       2
                            ;:background     "tomato"
                            :display       "flex"
                            :flexDirection "column"
                            :alignItems    "center"
                            :border        "none"
                            :borderRadius  "5px"
                            :height        300
                            :width         600
                            :fontFamily    "\"HelveticaNeue-Light\", \"Helvetica Neue Light\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif;"}}
               ;; search shows ups conditionally when documents are shown
               ;; also, list of documents renders below search and is filtered throughout
               [:div {:style {:marginBottom 20}}
                (build-titles data)]

               (when current-document?
                 (current-document data))

               (when keyboard-shortcuts?
                 (keyboard-shortcuts))

               (when documents?
                 (documents data))

               (when settings?
                 [:div
                  "Your settings would come here."])

               ])))))
