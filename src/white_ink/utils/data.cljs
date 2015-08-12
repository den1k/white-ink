(ns white-ink.utils.data)

(defn current-draft [data]
  (-> data :drafts last))

(defn cur-draft [data]
  (-> data :current-draft))

(defn user [data]
  (-> data :user))

(defn settings [data]
  (-> data user :settings))

(defn metrics [data]
  (-> data user :metrics))

(defn sessions [draft]
  (-> draft :sessions))

(defn inserts [draft]
  (-> draft sessions :inserts))

(defn cur-session [draft]
  (-> draft cur-draft :current-session))

(def cur-sessions
  (comp sessions cur-draft))

(def cur-insert
  (comp :current-insert cur-session))

(def cur-inserts
  (comp :inserts cur-session))

(defn inserts->text
  "Concats the text from inserts into one string."
  ([inserts] (inserts->text inserts ""))
  ([inserts init-text]
   (reduce
     (fn [total-text {:keys [start-idx text removed?]}]
       (str (subs total-text (or removed? 0) start-idx)
            text
            (subs total-text start-idx)))
     init-text
     inserts)))

(defn session->text
  ([session] (session->text session ""))
  ([session init-text] (-> session
                           :inserts
                           (inserts->text init-text))))

(defn sessions->text
  "Takes a coll of sessions and concats the text from
  every session and every index respectively into one string. "
  [sessions]
  (let [all-inserts (map :inserts sessions)]
    (reduce
      (fn [all-text insert-coll]
        (inserts->text insert-coll all-text))
      ""
      all-inserts)))

(def cur-sessions->text
  (comp sessions->text cur-sessions))

(defn cur-draft->text [{:keys [current-session sessions] :as current-draft}]
  (let [cur-insert (:current-insert current-session)
        prev-text (->> (sessions->text sessions)
                       ;; only showing current insert and not rendering other inserts in session
                       #_(session->text current-session))]
    (-> prev-text
        (subs 0 (:start-idx cur-insert))
        (str (:text cur-insert)))))


(defn insert-add-offset-to-notes
  "Notes idxs are relative to the start of their insert.
  This adds the insert idx to every note idx."
  [insert]
  (let [offset (:start-idx insert)
        nts (:notes insert)]
    (map #(update % :draft-index + offset) nts)))

(defn merge-sort-notes-sessions [data]
  (->> data
       cur-sessions
       (mapcat :inserts)
       (into [] (mapcat insert-add-offset-to-notes))
       (sort-by :draft-index)))

(defn merge-notes-cur-session [draft]
  (->> draft
       :current-session
       ((juxt #(map :notes (-> % :inserts)) #(-> % :current-insert :notes)))
       flatten
       (remove nil?)))

;(def s (deref white-ink.state/app-state))
;(-> s
;    ;cur-insert
;    cur-inserts
;    )
;(merge-notes-cur-session (:current-draft s))
#_(-> s
    :current-draft
    :current-session
    :inserts)
;
;(cur-sessions->text s)
;
;(-> s
;    cur-draft
;    cur-insert-text)
;
;#_(-> s
;      cur-sessions
;      sessions->text)
;;(sessions->text (cur-sessions s))
;
;(cur-draft s)
;(cur-inserts s)
;(cur-sessions s)
;(keys (:current-draft s))
;(-> s cur-session
;    ;session->text
;    cur-insert-text
;    )
;
;(defn build-text [data]
;  (-> data
;      inserts
;      ))
;
;(->> s
;     cur-inserts
;     (map inserts->text)
;     )
;
;(def is [{:start-idx 0
;          :text      "The beginning"
;          :removed   nil}
;         {:start-idx 5
;          :text      "The middle"
;          :removed   nil}
;         {:start-idx 10
;          :text      "The end"
;          :removed   nil}])
