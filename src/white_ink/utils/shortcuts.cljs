(ns white-ink.utils.shortcuts
  (:require [cljs.core.async :as async]
            [white-ink.chans :refer [action-chan]]))

(def ^:const ^:private key-map
  {220 :backslash
   9   :tab
   37  :arrow-left
   38  :arrow-up
   39  :arrow-right
   40  :arrow-down
   13  :return})

(def ^:const ^:private allowed-keys
  "Only using this because the event needs to be handled in the handler.
  Therefore it is necessary to know which events do prevent and which to let pass."
  {:app (select-keys key-map [220])
   :editor         (select-keys key-map [9 220 37 38 39 40])
   :notepad-editor (select-keys key-map [40 13])})

(defn get-key [source keycode]
  (-> allowed-keys
      (get source)
      (get keycode)))

(defn handle-shortcuts
  ([source event]
   (handle-shortcuts source nil event))
  ([source opts event]
   (let [keycode (.-keyCode event)]
     (prn "KEYCODE:" keycode)
     (when-let [key (get-key source keycode)]
       (let [message (if opts
                       [:key-down source key opts]
                       [:key-down source key])]
         (async/put! action-chan message))
       (.preventDefault event)))))
