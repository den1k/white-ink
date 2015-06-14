(ns ^:figwheel-always white-ink.core
  (:require [om.core :as om :include-macros true]
            [cljs.core.async :as async]
            [white-ink.state :refer [app-state]]
            [white-ink.components.app :refer [app]])
  (:require-macros [cljs.core.async.macros :as async]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; okay with this being a global. pretty much same as om/shared
(def actions-chan (async/chan))

(let [transactions (async/chan)
      transactions-pub (async/pub transactions :tag)
      events (async/chan)
      events-pub (async/pub events first)]
  (om/root
    (fn [data owner]
      (reify om/IRender
        (render [_]
          (om/build app data))))
    app-state
    {:target    (. js/document (getElementById "app"))

     :tx-listen (fn [tx] (async/put! transactions tx))

     :shared    {:tx-chan transactions-pub
                 :actions actions-chan
                 :events events
                 :events-pub events-pub
                 }}))



(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )




