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

(defn cur-session [data]
  (-> data cur-draft :current-session))

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
       (str (subs total-text 0 (- start-idx removed?))
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
  (let [sessions-inserts (map :inserts sessions)]
    (reduce
      (fn [all-text insert-coll]
        (inserts->text insert-coll all-text))
      ""
      sessions-inserts)))

(def cur-sessions->text
  (comp sessions->text cur-sessions))

(defn flatten-sessions-into-inserts [sessions]
  (vec (mapcat (comp reverse (partial sort-by :idx)) sessions)))

(defn ->sessions-inserts [data]
  ((comp flatten-sessions-into-inserts cur-sessions) data))

(defn editor-text [sessions-inserts cur-insert]
  (let [prev-text (->> (inserts->text sessions-inserts)
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

(defn get-abs-char-idx [sessions insert-idx rel-char-idx]
  (let [abs-char-idx (+ (get-in sessions [insert-idx :start-idx])
                        rel-char-idx)]

    (reduce
      (fn [cur-abs-idx {:keys [start-idx text]}]
        (if (<= start-idx cur-abs-idx)
          (+ cur-abs-idx (count text))
          cur-abs-idx))
      abs-char-idx
      (next (subvec sessions insert-idx)))))

(defn add-abs-idxs-to-notes [sessions-inserts]
  (for [[i insert] (map-indexed vector sessions-inserts)]
    ;; note offsets for removed not yet implemented
    (update insert :notes
            (fn [notes]
              (map #(assoc % :abs-idx (get-abs-char-idx sessions-inserts i (:rel-idx %))) notes)))))

(defn cat-sort-notes [sessions-inserts]
  (->> sessions-inserts
       (mapcat :notes)
       (sort-by :abs-idx)))

(defn merge-notes-cur-session [draft]
  (->> draft
       :current-session
       ((juxt #(map :notes (-> % :inserts)) #(-> % :current-insert :notes)))
       flatten
       (remove nil?)))

;; todo for proper note draft-idx offsets, need to recursively apply every session,
;; figure out the total text length, then apply the next one. Do not concat all inserts as if
;; they're one session.
;(def s (deref white-ink.state/app-state))
;(-> s
;cur-insert
;cur-inserts
;)

;;todo sort by idx, then additoon backwats
#_(->> s
       :current-draft
       :sessions
       (map :inserts)
       ;; get idx of every insert partitioned by session
       (map #(map (juxt :idx (comp count :text)) %))

       ;prn
       #_(map
           #(reduce
             (fn [out idx]
               (conj out (+ (last out) idx)))
             [0] %)))
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
;(def is [{:idx 0
;          :text      "The beginning"
;          :removed   nil}
;         {:idx 5
;          :text      "The middle"
;          :removed   nil}
;         {:idx 10
;          :text      "The end"
;          :removed   nil}])

