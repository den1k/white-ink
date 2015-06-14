(ns white-ink.components.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.state :refer [app-state]]
            [cljs.core.async :as async]
            [white-ink.components.editor-view :refer [editor-view]]
            [white-ink.components.reviewer-view :refer [reviewer-view]]
            [white-ink.utils.actions :refer [start-actions-handler]])
  (:require-macros [cljs.core.async.macros :as async]))

(defn app [data owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (do
        (start-actions-handler (om/get-shared owner) data)))
    om/IRender
    (render [_]
      (dom/div nil
               (om/build editor-view data)
               (om/build reviewer-view data)))))
