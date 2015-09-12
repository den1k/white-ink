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
  (let [res (reduce (fn [c item]
                      (if (pred item)
                        (reduced c)
                        (inc c))) 0 coll)]
    (if (= res (count coll))
      nil
      res)))


(defn idx-of-container-and-containing-item
  "Given a coll of colls, sums the length of each item with key of k
  until stop-idx.
  Returns the path as [idx-in-containing, k, idx-of-val],
  forming the equivalent of stop-idx in a coll of colls."
  [k stop-idx coll]
  (reduce
    (fn [[p-idx _ p-count] container]
      (let [val-length (count (get container k))
            total (+ p-count val-length)
            next-idx (inc p-idx)]
        (if (> total stop-idx)
          (reduced [next-idx k (- val-length (- total stop-idx))])
          [next-idx k total])))
    [-1 0]
    coll))

(defn maybe-f [f]
  (fn [x]
    (when x
      (f x))))

(defn maybe [f x]
  (when x
    (f x)))
