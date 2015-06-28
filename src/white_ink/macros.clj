(ns white-ink.macros)

(defmacro process-event [for-source & clauses]
  `(let [event-chan# (cljs.core.async/sub (om.core/get-shared ~'owner :events-pub) ~for-source (cljs.core.async/chan))]
     (cljs.core.async.macros/go-loop []
                                     (when-let [event# (last (cljs.core.async/<! event-chan#))]
                                       (print "EVENT" event#)
                                       (white-ink.utils.utils/case-fn event# ~@clauses)

                                       (recur)))))

#_(clojure.pprint/pprint (macroexpand `(process-event :editor :first-clause :callback)))
