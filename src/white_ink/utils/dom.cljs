(ns white-ink.utils.dom
  (:require [goog.style :as gstyle]
            [goog.fx.dom :as gdom]))

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

(defn set-cursor-to-end [node]
  (let [node (or (.-lastElementChild node) node)]
    (set-cursor node (count (.-textContent node)))))

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
          y (cond
              me-offset? (+ divider-height y)
              (< y parent-scroll-top) (- y divider-height)
              :else y)]
      (.play (gdom/Scroll. parent #js [0 parent-scroll-top] #js [0 y] 100))
      #_(gstyle/scrollIntoContainerView me parent))))