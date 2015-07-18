(ns white-ink.utils.search
  (:require [white-ink.utils.text :as utils.text]
            [white-ink.utils.utils :as utils])
  (:refer-clojure :exclude [find]))

(defn find [text query]
  (if (utils.text/empty-or-whitespace? query)
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
              ;; return nil if no results
              (if (< 1 (count out))
                out
                nil))))))))

(defn constrain-query [q]
  (if (> 2 (count q))
    ""
    q))

(def count-until-search-res
  (partial utils/count-until-pred :res))

(defn next-res-idx [dir cur-idx search-results]
  {:pre [(number? cur-idx)]}
  (let [max-idx (dec (count search-results))
        steps (case dir
                :forward 2
                :backward -2)
        next-idx (+ cur-idx steps)]
    (cond
      ; going backwards and no more results
      (neg? next-idx) (->> search-results
                           reverse
                           count-until-search-res
                           (- max-idx)) #_(next-res-idx dir (count search-results) search-results)
      ; passed in cur idx lies outside of search-results
      (> next-idx max-idx) (-> search-results
                               count-until-search-res)
      :else next-idx)))
