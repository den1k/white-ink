(ns white-ink.utils.data)

(defn current-draft [data]
  (-> data :drafts last))

(defn review-drafts [data]
  (-> data :drafts butlast))

(defn user [data]
  (-> data :user))

(defn settings [data]
  (-> data user :settings))

(defn metrics [data]
  (-> data user :metrics))
