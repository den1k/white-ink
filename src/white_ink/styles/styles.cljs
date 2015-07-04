(ns white-ink.styles.styles)

(def ^:const texts-and-notepad
  {:display        :flex
   :justifyContent :space-around})

(def ^:const texts
  {:width 500})

(def ^:const editor-view
  (merge texts-and-notepad {}))

(def ^:const editor-text
  (merge texts {:height   200
                :overflow "auto"
                :outline  "none"}))

(def ^:const reviewer-text
  (merge texts {:height   400
                :overflow "auto"}))

(def ^:const reviewer-view
  (merge texts-and-notepad {:marginTop 10}))

(def ^:const notepad
  {})

(def ^:const note-editor
  {:outline "none"})

(def ^:const note-reviewer
  (dissoc note-editor :outline))

(def ^:const reviewer-text-on-search
  (assoc reviewer-text :color "silver"))

(def ^:const search-result
  {:color "black"})
