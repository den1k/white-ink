(ns white-ink.components.app
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.components.editor-view :refer [editor-view]]
            [white-ink.components.reviewer-view :refer [reviewer-view]]
            [white-ink.components.search :as search]
            [white-ink.utils.actions :refer [start-actions-handler]]
            [white-ink.utils.shortcuts :refer [handle-shortcuts]]
            [white-ink.utils.data :as data]
            [white-ink.components.quick-settings :refer [quick-settings-view]])
  (:require-macros [cljs.core.async.macros :as async]
                   [white-ink.macros :refer [send-action!]]))

(defn app [{:keys [searching? speed->opacity quick-settings] :as data} owner]
  (reify
    om/IWillMount
    (will-mount [_]
      (start-actions-handler (om/get-shared owner) data))
    om/IRender
    (render [_]
      (let [sessions-inserts (data/->sessions-inserts data)]
        (html [:div
               {:on-key-down   #(handle-shortcuts :app %)
                :on-mouse-move #(when (> 1 speed->opacity)
                                 (send-action! :app-mouse)
                                 (.preventDefault %))}
               (when searching?
                 (om/build search/input data))
               (when (:show? quick-settings)
                 (om/build quick-settings-view data))
               [:div {:style (when (:show? quick-settings)
                               {:-webkit-filter "blur(3px)"
                                :opacity        0.1
                                :background     "white"
                                :position       "absolute"
                                :height         "100%"
                                :width          "100%"
                                })}
                (om/build editor-view data {:state {:sessions-inserts sessions-inserts}})
                (om/build reviewer-view data {:state {:sessions-inserts sessions-inserts}})]
               ])))))
