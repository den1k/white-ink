(ns white-ink.utils.styles.units)

(defn lines->total-height
  "Given styles and an amount n, returns the total height of n lines."
  [n {:keys [fontSize lineHeight]}]
  ; browser rounds line-height to integer val
  (let [one-line (int (* fontSize lineHeight))]
    (* n one-line)))

(defn full-lines-in-height
  "Takes a maximum height, and optional ratio and a style.
  Returns the the maximum total height of full lines within max-height."
  ([max-height style]
   (full-lines-in-height max-height style 1))
  ([max-height {:keys [fontSize lineHeight]} ratio]
   (let [max-height (* ratio max-height)
         one-line (int (* fontSize lineHeight))]
     (- max-height (mod max-height one-line)))))

