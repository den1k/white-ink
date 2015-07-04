(ns white-ink.components.search
  (:require [om.core :as om]
            [sablono.core :as html :refer-macros [html]]
            [white-ink.styles.styles :as styles]))

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
