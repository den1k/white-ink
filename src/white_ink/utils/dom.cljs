(ns white-ink.utils.dom)

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