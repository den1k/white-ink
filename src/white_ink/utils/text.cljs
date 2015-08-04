(ns white-ink.utils.text
  (:require [goog.string :as gstring]))

(defn search [text query]
  (if-not (every? seq [text query])
    [{:text text}]
    (let [q (.toLowerCase query)                            ; case insensitive
          q-len (count q)
          t-len (count text)
          search-text (.toLowerCase text)]
      (loop [prev-idx 0 out []]
        (let [i (.indexOf search-text q prev-idx)]
          (if-not (= -1 i)
            (let [end-idx (+ i q-len)]
              (recur end-idx
                     (let [to-out (if-not (= prev-idx i)
                                    [{:text (subs text prev-idx i)}
                                     {:res (subs text i end-idx)}]
                                    [{:res (subs text i end-idx)}])]
                       (into out to-out))))
            (if-not (= prev-idx t-len)
              (conj out {:text (subs text prev-idx)})
              out)))))))

(def empty-or-whitespace?
  gstring/isEmptyOrWhitespace)

(defn not-empty-or-whitespace [string]
  (when-not (empty-or-whitespace? string)
    string))

(def trim
  "Trims white spaces to the left and right of a string."
  (comp gstring/trim gstring/collapseBreakingSpaces))

(defn index-of-char
  "Returns idx of char in text. "
  [char]
  (fn do-it
    ([text] (do-it text 0))
    ([text start]
     (let [text (subs text start)
           ret (reduce #(if-not (= char %2)
                         (inc %)
                         (reduced %)) 0 text)]
       (if (= ret (count text))
         nil
         (+ start ret))))))

(def index-of-space
  (index-of-char \space))
