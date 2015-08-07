(ns white-ink.utils.search
  (:require [white-ink.utils.text :as utils.text]
            [white-ink.utils.utils :as utils])
  (:refer-clojure :exclude [find]))

(defn find [text query]
  (if (utils.text/empty-or-whitespace? query)
    [[:text text]]
    (let [q (.toLowerCase query)                            ; case insensitive
          q-len (count q)
          t-len (count text)
          search-text (.toLowerCase text)]
      (loop [prev-idx 0 out []]
        (let [i (.indexOf search-text q prev-idx)]
          (if-not (= -1 i)
            (let [end-idx (+ i q-len)]
              (recur end-idx
                     (if-not (= prev-idx i)
                       (conj out
                             [:text (subs text prev-idx i)]
                             [:res (subs text i end-idx)])
                       (conj out [:res (subs text i end-idx)]))))
            ;; last text segment
            (if-not (= prev-idx t-len)
              (conj out [:text (subs text prev-idx)])
              ;; return nil if no results
              (if (< 1 (count out))
                out
                nil))))))))

;; todo maybe delete this
(defn find-idxs [text query]
  (if (utils.text/empty-or-whitespace? query)
    [[:text text]]
    (let [q (.toLowerCase query)                            ; case insensitive
          q-len (count q)
          t-len (count text)
          search-text (.toLowerCase text)]
      (loop [prev-idx 0 out []]
        (let [i (.indexOf search-text q prev-idx)]
          (if-not (= -1 i)
            (let [end-idx (+ i q-len)]
              (recur end-idx
                     (if-not (= prev-idx i)
                       (conj out
                             [:text prev-idx]
                             [:res i])
                       (conj out [:res i]))))
            ;; last text segment
            (if-not (= prev-idx t-len)
              (conj out [:text prev-idx])
              ;; return nil if no results
              (if (< 1 (count out))
                out
                nil))))))))

;(find-idxs "hello buddy" "bud")

(defn constrain-query [q]
  (if (> 2 (count q))
    ""
    q))

(defn search-res? [item]
  (= :res (first item)))

(def count-until-search-res
  (partial utils/count-until-pred search-res?))

(defn next-res-idx [dir cur-idx search-results]
  {:pre [(number? cur-idx)]}
  (let [max-idx (dec (count search-results))
        res (case dir
              ; going backwards and no more results
              :backward (->> (subvec search-results 0 cur-idx)
                             reverse
                             count-until-search-res)
              ; passed in cur idx lies outside of search-results
              :forward (-> (subvec search-results (inc cur-idx))
                           count-until-search-res))
        next-idx (do (prn "res1" res)
                     (if res
                       ((case dir
                          :forward +
                          :backward -) cur-idx res 1)
                         (case dir
                           :forward (next-res-idx :forward 0 search-results)
                           :backward (next-res-idx :backward max-idx search-results))))]
    (prn "nextidx" next-idx)
    next-idx))
