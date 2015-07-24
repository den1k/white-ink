(ns white-ink.styles.styles
  (:require [white-ink.styles.typography :as typo]
            [white-ink.styles.colors :as colors]
            [white-ink.utils.styles :as utils.styles]))

(def ^:const texts-and-notepad
  {:display        :flex
   :justifyContent :space-around})

(def ^:const texts
  {:width 500})

(defn editor-view [searching?]
  (merge texts-and-notepad
         {:transition "opacity 0.5s ease"}
         (when searching?
           {:opacity 0.2})))

(def ^:const editor-text
  (merge typo/write-1
         texts
         {:height   (utils.styles/lines->line-height 8 typo/write-1)
          :overflow "auto"
          :outline  "none"}))

(def ^:const reviewer-text
  (merge typo/write-1
         texts
         {:height   (utils.styles/lines->line-height 18 typo/write-1)
          :overflow "auto"
          :color    colors/reviewable-text}))

(def ^:const reviewer-view
  (merge texts-and-notepad
         {:marginTop (utils.styles/lines->line-height 2 typo/write-1)}))

(def ^:const notepad
  {})

(def ^:const note-editor
  (merge typo/write-3
         {:outline "none"}))

(def ^:const note-reviewer
  (-> note-editor
      (dissoc :outline)
      (assoc :cursor "pointer"
             :color colors/reviewable-text)))

(def ^:const search-result
  {:color "black"})
