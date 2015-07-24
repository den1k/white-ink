(ns white-ink.utils.styles)

(defn lines->line-height
  "Given styles converts n lines to a lineHeight value."
  [n {:keys [fontSize lineHeight]}]
  (* fontSize lineHeight n))
