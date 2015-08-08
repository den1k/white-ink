(ns white-ink.components.app
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.components.editor-view :refer [editor-view]]
            [white-ink.components.reviewer-view :refer [reviewer-view]]
            [white-ink.utils.actions :refer [start-actions-handler]]
            [white-ink.components.search :as search]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]])
  (:require-macros [cljs.core.async.macros :as async]
                   [white-ink.macros :refer [send-action!]]))

(defn app [{:keys [searching? speed->opacity] :as data} owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (start-actions-handler (om/get-shared owner) data))
    om/IRender
    (render [_]
      (html [:div
             {:on-key-down   #(handle-shortcuts :app %)
              :on-mouse-move #(when (> 1 speed->opacity)
                               (send-action! :app-mouse)
                               (.preventDefault %))}
             (when searching?
               (om/build search/input data))
             (om/build editor-view data)
             (om/build reviewer-view data)
             ]))))
