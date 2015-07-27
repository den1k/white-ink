(ns white-ink.styles.styles
  (:require [white-ink.styles.typography :as typo]
            [white-ink.styles.colors :as colors]
            [white-ink.utils.styles.units :as units]))

(def ^:const texts-and-notepad
  {:display        :flex
   :justifyContent :space-around
   :alignItems     :center})

(def editor-height
  (units/lines->total-height 8 typo/write-1))

(def reviewer-height
  (units/lines->total-height 18 typo/write-1))

(defn editor-view [searching?]
  (merge texts-and-notepad
         {:marginTop  (units/lines->total-height 1.5 typo/write-1)
          :height     editor-height
          :transition "opacity 0.5s ease"}
         (when searching?
           {:opacity 0.2})))

(def editor-reviewer
  {:display    "flex"
   :flexBasis  450
   :maxWidth   550
   :flexGrow   "3"
   :flexShrink "0"})

(def ^:const editor-text
  (merge typo/write-1
         {:overflow "auto"
          :outline  "none"
          ; z-index for grain
          :zIndex 0
          :height editor-height}))

(def ^:const reviewer-text
  (merge typo/write-1
         {:height   reviewer-height
          :overflow "auto"
          :color    colors/reviewable-text}))

(def ^:const reviewer-view
  (merge texts-and-notepad
         {:marginTop (units/lines->total-height 2 typo/write-1)}))

(def ^:const ^:private notepad
  {:display       "flex"
   :flexDirection "column"
   :alignItems    "flex-start"
   :flexBasis     150
   :maxWidth      220
   :flexGrow      "1"
   :flexShrink    "0"
   :paddingRight  9
   :overflowX     "hidden"
   :overflowY     "scroll"
   :direction     "rtl"})

(def notepad-editor
  (assoc notepad
    :height (units/full-lines-in-height editor-height
                                               typo/write-3
                                               0.7)))

(def notepad-reviewer
  (assoc notepad
    :height (units/full-lines-in-height reviewer-height
                                               typo/write-3
                                               0.7)))


(def ^:const note
  {:flexShrink    "0"
   :paddingLeft   9
   :maxWidth      "inherit"
   :position      "relative"
   :textAlign     "right"
   :listStyleType "none"
   :direction     "ltr"})

(def ^:const note-editor
  (merge note
         typo/write-3
         {:outline "none"}))

(def ^:const note-reviewer
  (-> note-editor
      (dissoc :outline)
      (assoc :cursor "pointer"
             :color colors/reviewable-text)))

(def ^:const search-result
  {:color "black"})
