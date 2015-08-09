(ns white-ink.styles.typography)

;$text1-font-size: rem(12);
;$text2-font-size: rem(16);
;$text3-font-size: rem(18);
;
;$text1-line-height: 1.4;
;$text3-line-height: 1.5;
;
;@mixin typography-write1 {
;                          font-family: $helvetica;
;                          font-size: $text1-font-size;
;                          line-height: $text1-line-height;
;                          }

; https://bitbucket.org/den1k/whiteink-ember/src/647c4d7e897f74a7c2fcb3964debc2ca00a24942/app/styles/typography.scss?at=master

(def ^:const text-3
  {:fontSize   12
   :lineHeight 1.4})

(def ^:const text-1
  {:fontSize   18
   :lineHeight 1.5})

(def ^:const write-1
  (merge text-1
         {:fontFamily   "georgia"
          :letterSpacing "0.01rem"
          :fontWeight    400
          :fontStyle     "normal"}))

(def ^:const write-3
  (merge text-3
         {:fontFamily "helvetica"}))


