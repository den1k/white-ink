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
        (utils.dom/scroll-into-view (om/get-node owner) 7)))
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

(defn results [results]
  (for [[idx res] (map-indexed vector results)
        :let [[type text selected?] res]]
    (case type
      :res (om/build result [text selected?] {:react-key idx})
      :text (om/build (fn [_ owner]
                        (om/component
                          (html [:span
                                 text]))) nil {:react-key idx})
      :scroll-target (om/build scroll-target text {:react-key idx}))))
