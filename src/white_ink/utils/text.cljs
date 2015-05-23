(ns white-ink.utils.text)

(defn search [text query]
  (let [q (.toLowerCase query)                              ; case insensitive
        q-len (count q)
        search-text (.toLowerCase text)]
    (loop [prev-idx 0 out []]
      (let [i (.indexOf search-text q prev-idx)]
        (if-not (= -1 i)
          (let [end-idx (+ i q-len)]
            (recur end-idx
                   (conj out
                         {:text (subs text prev-idx i)}
                         {:res (subs text i end-idx)})))
          (conj out {:text (subs text prev-idx)}))))))
