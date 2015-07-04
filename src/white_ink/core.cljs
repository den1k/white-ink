(ns ^:figwheel-always white-ink.core
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :as async]
            [white-ink.state :refer [app-state]]
            [white-ink.components.app :refer [app]]
    ;; importing utils because it is used by a macro and would otherwise throw error
            [white-ink.utils.utils :as utils]
            [white-ink.chans :refer [action-chan task-chan task-pub]])
  (:require-macros [cljs.core.async.macros :as async]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

(let [transactions (async/chan)
      transactions-pub (async/pub transactions :tag)]
  (om/root
    (fn [data owner]
      (reify om/IRender
        (render [_]
          (om/build app data))))
    app-state
    {:target    (. js/document (getElementById "app"))

     :tx-listen (fn [tx] (async/put! transactions tx))

     :shared    {:tx-chan transactions-pub
                 :actions action-chan
                 :tasks task-chan
                 :tasks-pub task-pub
                 }}))



(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  :on-jsload nil
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )




