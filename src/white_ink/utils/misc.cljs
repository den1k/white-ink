(ns white-ink.utils.misc
  (:require [white-ink.utils.utils :as utils]))

(defn containing-idxs-for-text-idx [stop-idx coll]
  "Returns the path as [idx-of-containing, :text, idx-of-val],
  forming the equivalent of stop-idx in a coll of colls."
  (utils/idx-of-container-and-containing-item 1 stop-idx coll))

(defn in-range? [[start end] x]
  (> end x start))

(defn keys->dasherized-str [prefix & keys]
  (fn [m]
    (clojure.string/join "-"
                         (concat [prefix]
                                 (map m keys)))))

(defn insert-form [results form idx]
  (let [[res-idx idx-text idx-char] (containing-idxs-for-text-idx idx results)
        res (get results res-idx)
        res-text (get res idx-text)
        [text-1 text-2] ((juxt #(subs % 0 idx-char) #(subs % idx-char)) res-text)]
    (vec (concat (subvec results 0 res-idx)
                 [(assoc res idx-text text-1)
                  form
                  (assoc res idx-text text-2)]
                 (subvec results (inc res-idx))))))

;; for NOTES
(def note-draft-idx-hook-id
  (keys->dasherized-str "note-draft-idx-hook" :abs-idx #_:id))

(defn note-draft-idx-form [note]
  [:note-draft-idx-hook ""
   (note-draft-idx-hook-id note)])