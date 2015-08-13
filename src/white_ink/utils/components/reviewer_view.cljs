(ns white-ink.utils.components.reviewer-view
  (:require [white-ink.utils.misc :refer [insert-form
                                          keys->dasherized-str
                                          note-draft-idx-form]]))

;(def note-draft-idx-hook-id
;  (keys->dasherized-str "note-draft-idx-hook" :draft-index :id))
;
;(defn note-draft-idx-form [note]
;  [:note-draft-idx-hook ""
;   (note-draft-idx-hook-id note)])

(defn insert-note-hooks [results notes]
  (let [draft-idxs (map :draft-index notes)
        note-form (map note-draft-idx-form notes)]
    (reduce (fn [res [css idx]]
              (insert-form res css idx))
            results
            (partition 2 (interleave note-form draft-idxs)))))

#_(insert-note-hooks [[:text "12345678"] [:res "rR"] [:text "999999"]] [{:draft-index 2}
                                                                        {:draft-index 4}
                                                                        {:draft-index 11}
                                                                        {:draft-index 20}])