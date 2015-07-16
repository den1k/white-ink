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

(defn case-first-fn
  "Like `case` but if the expression is a seq, will use the first item to dispatch
  and apply next items to matching fn in clauses."
  [exp & clauses]
  {:pre [(even? (count clauses))]}
  (let [cs-map (apply hash-map clauses)
        args? (coll? exp)
        f? (and args? (get cs-map (first exp)))
        match? (get cs-map exp)]
    (cond
      f? (apply f? (rest exp))
      match? (match?)
      :else (.error js/console "Unknown case:" (clj->js exp)))))

(defn count-until-pred [pred coll]
  (reduce (fn [c item]
            (if (pred item)
              (reduced c)
              (inc c))) 0 coll))
