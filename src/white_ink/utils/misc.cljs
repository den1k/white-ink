(ns white-ink.utils.misc
  (:require [white-ink.utils.utils :as utils]))

(defn containing-idxs-for-text-idx [stop-idx coll]
  "Returns the path as [idx-of-containing, :text, idx-of-val],
  forming the equivalent of stop-idx in a coll of colls."
  (utils/idx-of-container-and-containing-item 1 stop-idx coll))

(defn in-range? [[start end] x]
  (> end x start))