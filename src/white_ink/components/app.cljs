(ns white-ink.components.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.state :refer [app-state]]
            [cljs.core.async :as async]
            [white-ink.components.editor-view :refer [editor-view]]
            [white-ink.components.reviewer-view :refer [reviewer-view]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn app [data owner]
  (om/component
    (dom/div nil
             (om/build editor-view data)
             (om/build reviewer-view data))))

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
     :shared    {:tx-chan transactions-pub}}))

