(ns white-ink.components.search
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.styles.styles :as styles]
            [white-ink.utils.text :as utils.text])
  (:require-macros [white-ink.macros :refer [send-action!]]))

(defn input [state owner]
  (om/component
    (html [:input {:on-change  #(->> (.. % -target -value)
                                     (send-action! :reviewer :search))
                   :auto-focus true
                   :style      {:background "tomato"
                                :position   "absolute"
                                :left       -9999}}])))

(defn result [text owner]
  (om/component
    (html [:span
           {:style styles/search-result}
           text])))

(defn results [results]
  (for [res results
        :let [search-res (:res res)]]
    (if search-res
      (om/build result search-res)
      (:text res))))
