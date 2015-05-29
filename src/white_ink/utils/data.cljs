(ns white-ink.utils.data)

(defn current-draft [data]
  (-> data :drafts last))

(defn review-drafts [data]
  (-> data :drafts butlast))
