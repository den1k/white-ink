(ns white-ink.components.search
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [white-ink.utils.dom :as utils.dom])
  (:require-macros [white-ink.macros :refer [send-action!]]))

(defn input [state owner]
  (reify
    om/IWillUnmount
    (will-unmount [_]
      (send-action! :reviewer :search ""))
    om/IRender
    (render [_]
      (html [:input {:on-change   #(->> (.. % -target -value)
                                        (send-action! :reviewer :search))
                     ;temp commnt
                     ;:on-blur    #(send-action! :search-off)
                     :on-key-down #(handle-shortcuts :search %)
                     :auto-focus  true
                     :style       {:background "tomato"
                                   ;; render off-screen
                                   :position   "absolute"
                                   :left       -9999}}]))))

(defn result [[text selected?] owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      (when selected?
        (utils.dom/scroll-into-view (om/get-node owner) 7 100)))
    om/IRender
    (render [_]
      (html [:span
             ;; todo make classnames consts, move to own ns
             {:class-name "search-res"
              :style      (if selected?
                            (assoc styles/search-result
                              :background "skyblue"
                              :borderRadius 2)
                            styles/search-result)}
             text]))))

(defn scroll-target [_ owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      (utils.dom/scroll-into-view (om/get-node owner) 7 500))
    om/IRender
    (render [_]
      (html [:span#scroll-target ""]))))

;; this is a render fn
(defn results [results]
  (for [[idx res] (map-indexed vector results)
        :let [[type content opt] res]]
    (case type
      :res (om/build result [content opt] {:react-key idx})
      :text (om/build (fn [_ owner]
                        (om/component
                          (html [:span
                                 content]))) nil {:react-key idx})
      :note-draft-idx-hook (om/build (fn [_ owner]
                                  (om/component
                                    (html [:span {:id opt
                                                  :style {:background "tomato"}}]))) nil))))
