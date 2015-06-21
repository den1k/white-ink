(ns white-ink.utils.state
  (:require [om.core :as om]
            [goog.string :as gstring]
            [clojure.string :as string]))

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
    (UUID. (string/join (concat
                                  (top-32-bits) "-"
                                  (repeatedly 4 f) "-4"
                                  (repeatedly 3 f) "-"
                                  (g) (repeatedly 3 f) "-"
                                  (repeatedly 12 f))))))

(defn save-note!
  "Saves a note and it's attributes. If there is no text in the node it will delete it.
  (Currently only deletes last note. TODO delete based on index"
  [{:keys [notes note text] :as note-map}]
  (if (gstring/isEmptyOrWhitespace text)
    (when notes
      (let [note-idx (last (om/path note))]
        ;; todo impl idx specific removal
        (om/transact! notes pop)
        #_(prn "nu notes" (clj->js (:notes (last (:drafts @(om/transact! notes pop))))))))
    (om/update! note (merge @note (dissoc note-map :note :notes)))))