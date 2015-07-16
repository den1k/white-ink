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
  ;(prn "dir" dir)
  ;(prn "cur-idx" cur-idx)
  ;(prn "count" (count search-results))
  (let [steps (case dir
                :forward (->> (inc cur-idx)
                              (subvec search-results)
                              count-until-search-res
                              inc)
                :backward (-> (subvec search-results 0 cur-idx)
                              reverse
                              count-until-search-res
                              inc
                              -))
        next-idx (+ cur-idx steps)]
    ;(prn "next idx" next-idx)
    (cond
      ; going backwards and no more results
      (neg? next-idx) (next-res-idx dir (count search-results) search-results)
      ; passed in cur idx lies outside of search-results
      (> next-idx (dec (count search-results))) (next-res-idx dir 0 search-results)
      :else next-idx)))