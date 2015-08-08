(ns white-ink.utils.dom
  (:require [goog.style :as gstyle]
            [goog.fx.dom :as gdom]
            [white-ink.utils.utils :refer [count-until-pred]]
            [bardo.ease :refer [ease]]))

(extend-type js/HTMLCollection
  ISeqable
  (-seq [array] (array-seq array 0)))

(defn get-selection
  "Returns index of caret in text. If include-parent is true,
  will also count all siblings until the node containing the caret."
  ([] (get-selection false))
  ([include-parent?]
    (let [sel (.. js/document getSelection)
          [start end] (sort [(.-baseOffset sel) (.-extentOffset sel)])]
      (if-not include-parent?
        {:start start
         :end   end
         :type  (keyword (clojure.string/lower-case (.-type sel)))}
        (let [sel-node (.. sel -anchorNode -parentElement)
              siblings (.. sel-node -parentNode -children)
              sibl-pre-node (take-while #(not= sel-node %) siblings)
              txt-count-until (reduce (fn [n node] (+ n (.. node -textContent -length))) 0 sibl-pre-node)]
          {:start (+ txt-count-until start)
           :end   (+ txt-count-until end)
           :type  (keyword (clojure.string/lower-case (.-type sel)))})))))

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

(defn visible?
  "Returns a boolean if the element is visible within it's parent container
  or the passed in parent. Potential bottleneck b/c getBoundingClientRect
  is called each time."
  ([elem]
   (visible? elem (.. elem -parentNode)))
  ([elem parent]
   (let [elem-top (.. elem getBoundingClientRect -top)
         parent-rect (.. parent getBoundingClientRect)]
     (< (dec (.-top parent-rect)) elem-top (.-bottom parent-rect)))))

(defn animate-scroll [node offset dur]
  (.play
    (gdom/Scroll. node
                  #js [0 0]
                  #js [0 offset]
                  dur
                  (ease :cubic-in-out))))

(defn scroll-into-view
  "Scrolls an element into the visible bounds of it's parent.
  View divider will scroll it into view even if it is already visible, a value of 5
  will scroll it up if it's in the lower 5th of the parent.
  Dur is the duration of the animated scroll."
  ([elem] (scroll-into-view elem 1 100))
  ([elem dur] (scroll-into-view elem 1 dur))
  ([elem view-divider dur]
   (let [me elem
         me-top (.. me getBoundingClientRect -top)
         parent (.. me -parentNode)
         parent-rect (.. parent getBoundingClientRect)
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
       (.play
         (gdom/Scroll. parent
                       #js [0 parent-scroll-top]
                       #js [0 y]
                       dur
                       (ease :cubic-in-out)))))))

(defn scroll-to-bottom [elem]
  (aset elem "scrollTop" 10e6))

(defn cursor->end-and-scroll [node]
  (doto node
    scroll-to-bottom
    set-cursor-to-end))

(defn css-class? [name elem]
  (= name (.-className elem)))

;; todo move to a dom-selectors namespace or something.
(def search-res-node?
  (partial css-class? "search-res"))

(defn first-visible-idx
  ([elems] (first-visible-idx identity elems))
  ([pred elems]
   (count-until-pred (every-pred visible? pred) elems)))

(defn next-closest-idx
  ([parent] (next-closest-idx identity parent))
  ([pred parent]
   (let [distance (+ (.-offsetTop parent) (.-scrollTop parent))
         children (.-children parent)
         stop (fn [child] (> (.-offsetTop child) distance))]
     (count-until-pred (every-pred stop pred) children))))

(defn first-visible-or-closest-idx
  ([parent] (first-visible-or-closest-idx identity parent))
  ([pred parent]
   (let [children (.-children parent)]
     (or (first-visible-idx search-res-node? children)
         (next-closest-idx search-res-node? parent)))))