(ns white-ink.utils.utils)

(defn case-fn
  "Like `case` but checks if the last of each clause is a fn, in which case it applies exp on it."
  [exp & clauses]
  (let [else? (if (odd? (count clauses))
                (last clauses))
        clauses (if else?
                  (butlast clauses)
                  clauses)
        cs-map (apply hash-map clauses)]
    (if-let [ret (get cs-map exp)]
      (if (fn? ret)
        (apply ret exp)
        ret))
    else?))
