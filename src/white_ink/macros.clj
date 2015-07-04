(ns white-ink.macros)


(defmacro send-action! [& args]
  `(do
     (let [args# [~@args]]
       (cljs.core.async/put! (om.core/get-shared ~'owner :actions)
                             ;; cannot `apply` coll to macro, therefore it checks
                             ;; whether args (a coll) contains a coll by checking if first is still a coll when spliced.
                             ;; if args contains only one coll splicing is equivalent to calling first on args
                             (if (-> args# first coll?)
                               (first args#)
                               args#)))))

#_(defmacro process-event [for-source & clauses]
  `(let [event-chan# (cljs.core.async/sub (om.core/get-shared ~'owner :events-pub) ~for-source (cljs.core.async/chan))]
     (cljs.core.async.macros/go-loop []
                                     (when-let [event# (last (cljs.core.async/<! event-chan#))]
                                       (print "EVENT" event#)
                                       (white-ink.utils.utils/case-fn event# ~@clauses)

                                       (recur)))))

#_(clojure.pprint/pprint (macroexpand `(process-event :editor :first-clause :callback)))

(defmacro process-task [for-source & clauses]
  `(let [task-chan# (cljs.core.async/sub (om.core/get-shared ~'owner :tasks-pub) ~for-source (cljs.core.async/chan))]
     (cljs.core.async.macros/go-loop []
                                     (when-let [task# (next (cljs.core.async/<! task-chan#))]
                                       (let [task# (if (= 1 (count task#))
                                                     (first task#)
                                                     task#)]
                                         (prn "TASK" task#)
                                         (white-ink.utils.utils/case-first-fn task# ~@clauses))

                                       (recur)))))
