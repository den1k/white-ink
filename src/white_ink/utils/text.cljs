(ns white-ink.utils.text)

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
