(ns white-ink.components.notepad
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]))

(defn notepad-editor [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-editor"]
           [:ul (for [note notes]
                  [:li
                   {:content-editable true
                    :key              (:id note)}
                   (:text note)])]])))

(defn notepad-reviewer [{:keys [notes] :as draft} owner]
  (om/component
    (html [:div
           [:h6 "notepad-reviewer"]
           [:ul (for [note notes]
                  [:li {:key (:id note)}
                   (:text note)])]])))
