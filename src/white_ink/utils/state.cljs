(ns white-ink.utils.state
  (:require [om.core :as om]
            [white-ink.utils.text :as utils.text]
            [clojure.string :as string]
            [cljs.pprint :refer [pprint]]))

(defn make-squuid
  "(make-squuid)  =>  new-uuid
  Arguments and Values:
  new-squuid --- new type 4 (pseudo randomly generated) cljs.core/UUID instance.
  Description:
  Returns pseudo randomly generated, semi-sequential SQUUID.
  See http://docs.datomic.com/clojure/#datomic.api/squuid
  Returns a UUID where the most significant 32 bits are the current time since epoch in seconds.
  like: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx as per http://www.ietf.org/rfc/rfc4122.txt.
  Examples:
  (make-random-squuid)  =>  #uuid \"305e764d-b451-47ae-a90d-5db782ac1f2e\"
  (type (make-random-squuid)) => cljs.core/UUID"
  []
  (letfn [(top-32-bits [] (.toString (int (/ (.getTime (js/Date.)) 1000)) 16))
          (f [] (.toString (rand-int 16) 16))
          (g [] (.toString (bit-or 0x8 (bit-and 0x3 (rand-int 15))) 16))]
    (uuid (string/join (concat
                         (top-32-bits) "-"
                         (repeatedly 4 f) "-4"
                         (repeatedly 3 f) "-"
                         (g) (repeatedly 3 f) "-"
                         (repeatedly 12 f))))))

(defn save-note!
  "Saves a note and it's attributes. If there is no text in the node it will delete it.
  (Currently only deletes last note. TODO delete based on index"
  [{:keys [notes note text] :as note-map}]
  (if (utils.text/not-empty-or-whitespace text)
    (om/update! note (merge @note (dissoc note-map :note :notes)))
    ;; if empty content delete:
    (when notes
      (let [note-idx (last (om/path note))]
        ;; todo impl idx specific removal
        (om/transact! notes pop)
        #_(prn "nu notes" (clj->js (:notes (last (:drafts @(om/transact! notes pop))))))))))

(defn save-new-insert! [app-state full-text idx]
  (let [idx (or (utils.text/index-of-space full-text idx)
                ;; append if idx is in last word
                (count full-text))]
    (om/transact! app-state [:current-draft :current-session]
                  (fn [cur-session]
                    (let [{:keys [text] :as current-insert} (:current-insert cur-session)]
                      (cond-> cur-session
                              (utils.text/not-empty-or-whitespace text) (update :inserts conj current-insert)
                              true (assoc :current-insert {:start-idx idx
                                                           :text      ""
                                                           :removed?  nil
                                                           :notes     []})))))))

(defn update-cur-insert!
  "Rerenders on text-specific state-change drop chars on fast typing.
  This waits for a half second break before persisting.
  Beware, if current-insert changes before the wait period expired, this will overwrite state."
  [{:keys [current-draft] :as app-state} {:keys [text-content orig-text start-idx]}]
  (let [new-text (-> text-content
                     (subs start-idx)
                     utils.text/trim-right)
        ;; removed may have to be dec'd
        removed? (when (utils.text/empty-or-whitespace? new-text)
                   (- (count orig-text)
                      (count text-content)))
        cur-insert (-> current-draft :current-session :current-insert)]
    ;(prn "text content" text-content)
    ;(prn "new-text" new-text)
    ;(prn @cur-insert)
    (om/transact! cur-insert #(merge % {:text     new-text
                                        :removed? removed?}))))
(defn persist-scroll-offset [data el]
  (let [persist-fn (fn [el]
                     ;; no-op if nil. Could maybe checked earlier?
                     (when el
                       (om/update! data :review-scroll-top (.-scrollTop el))))]
    (persist-fn el)))

(defn merge-current-session [{:keys [current-draft] :as data}]
  (let [current-session (:current-session current-draft)
        {:keys [current-insert
                inserts]} current-session
        new-inserts (conj inserts current-insert)]
    (pprint
      @(om/transact! data
                     [:current-draft]
                     #(-> %
                          (assoc-in [:current-session :current-insert]
                                    {:start-idx nil         ; temp zero, probs len of review text
                                     :text      ""
                                     :removed?  nil
                                     :notes     []})
                          (assoc-in [:current-session :inserts] [])
                          (update :sessions conj new-inserts))))))