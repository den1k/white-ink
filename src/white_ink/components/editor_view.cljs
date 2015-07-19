(ns white-ink.components.editor-view
  (:require [om.core :as om]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.data :as data]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.components.editor :refer [editor]]
            [white-ink.components.notepad :refer [notepad-editor]]))

(defn editor-view [data owner]
  (om/component
    (let [current-draft (-> data data/current-draft)]
      (html
        [:div {:style styles/editor-view}
         (om/build editor data {:state current-draft})
         (om/build notepad-editor current-draft)]))))
