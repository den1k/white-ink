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
             {:style (if selected?
                       (assoc styles/search-result
                         :background "skyblue")
                       styles/search-result)}
             text]))))


(defn results [results]
  (for [[idx res] (map-indexed vector results)
        :let [search-res (:res res)
              selected? (:selected? res)]]
    (if search-res
      (om/build result [search-res selected?] {:react-key idx})
      (om/build (fn [_ owner]
                  (om/component
                    (html [:span
                           (:text res)]))) nil {:react-key idx}))))
