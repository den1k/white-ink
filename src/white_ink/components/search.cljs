(ns white-ink.components.search
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [goog.style :as gstyle]
            [goog.fx.dom :as gdom])
  #_(:import [goog.fx.dom.scroll])
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

(defn scroll-into-view [elem view-divider]
  (let [me elem
        me-top (.. me getBoundingClientRect -top)
        parent (.. me -parentNode)
        parent-rect (.. me -parentNode getBoundingClientRect)
        parent-height (.. parent-rect -height)
        divider-height (/ parent-height view-divider)
        bottom-divider-line (- (.-bottom parent-rect) divider-height)
        parent-scroll-top (.-scrollTop parent)
        ;visible? (< (.-top parent-rect) me-top (.-bottom parent-rect))
        me-offset? (> me-top bottom-divider-line)]
    (let [y (.-y (gstyle/getContainerOffsetToScrollInto me parent))
          #_y #_(cond
              me-offset? (+ divider-height y)
              (< y parent-scroll-top) (- y divider-height)
              :else y)]
      (prn "y y" y)
      (prn "prscrolltop" parent-scroll-top)
      #_(.play (gdom/Scroll. parent #js [0 parent-scroll-top] #js [0 y] 200))
      (gstyle/scrollIntoContainerView me parent))
    ))

(defn result [[text selected?] owner]
  (reify
    om/IDidUpdate
    (did-update [_ _ _]
      ;; todo experiment transitioning scroll within om, will-update did-update
      ;(set! (.-scrollTop (.-parentNode (om/get-node owner))) 300)
      #_(scroll-into-view (om/get-node owner) 3.3))
    om/IRender
    (render [_]
      (html [:span
             {:style (if selected?
                       (assoc styles/search-result
                         :background "orange")
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
