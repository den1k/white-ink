(ns white-ink.components.menu
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
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
                                    :borderRadius "5px"
                                    :padding      "5px 10px"}}
                     key]])))

(defn sub-shortcut-wrapper [shortcut]
  [:div {:style {:marginLeft   50
                 :marginBottom 10}}
   shortcut])

(defn choose [items-map chosen]
  (into {} (map (fn [[k _]]
                  (if (= chosen k)
                    {k true}
                    {k false}))) items-map))

(defn select-menu-item [submenus selected]
  (om/transact! submenus #(choose % selected)))

(def menu-item-style
  {:outline    "none"
   :border     "1px solid whitesmoke"
   :background "initial"
   ;:borderRadius 2
   :cursor     "pointer"
   })

(defn submenu-title-button [[name key menu] owner]
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

(defn menu-view [{:keys [menu] :as data} owner]
  (reify
    om/IInitState
    (init-state [_]
      {})
    om/IRenderState
    (render-state [_ {:keys []}]
      (let [items (:items menu)
            {:keys [current-document?
                    documents?
                    keyboard-shortcuts?
                    settings?]} items]
        (prn (type menu))
        (html [:div
               {:style {:position      "absolute"
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
                ;[:button {:style menu-item-style} "Merge Session"]
                (om/build submenu-title-button ["Current Document" :current-document? items])
                (om/build submenu-title-button ["Documents" :documents? items])
                (om/build submenu-title-button ["Shortcuts" :keyboard-shortcuts? items])
                (om/build submenu-title-button ["Settings" :settings? items])
                ]
               (when current-document?
                 [:div
                  "Current Document options would be here."])
               (when keyboard-shortcuts?
                 [:div {:style {:width    "80%"
                                :fontSize "1.6rem"}}
                  (om/build shortcut ["New Note" "tab"])
                  (sub-shortcut-wrapper
                    (om/build shortcut ["Submit" "return"]))
                  (om/build shortcut ["Search" "\\"])
                  (sub-shortcut-wrapper
                    (om/build shortcut ["Next Result" "]"]))
                  (sub-shortcut-wrapper
                    (om/build shortcut ["Previous Result" "["]))
                  ])

               (when documents?
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
                                      :fontSize     "1.6rem"}}])

               (when settings?
                 [:div
                  "Your settings would come here."])

               ])))))
