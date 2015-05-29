(ns white-ink.styles.styles)

(def texts-notepad
  {:display        :flex
   :justifyContent :space-around})

(def texts
  {:width 500})

(def editor-view
  (merge texts-notepad {}))

(def editor-text
  (merge texts {:height   200
                :overflow "auto"}))

(def reviewer-text
  (merge texts {:height   400
                :overflow "auto"}))

(def reviewer-view
  (merge texts-notepad {:marginTop 10}))