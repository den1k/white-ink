(ns white-ink.utils.shortcuts
  (:require [cljs.core.async :as async]
            [white-ink.chans :refer [action-chan]]))

(def ^:const ^:private key-map
  {220            :backslash
   191            :forwardslash
   #{:shift 32}   :shift-space
   #{:option 32}  :option-space
   #{:option 191} :option-forwardslash
   9              :tab
   221            :right-bracket
   219            :left-bracket
   37             :arrow-left
   38             :arrow-up
   39             :arrow-right
   40             :arrow-down
   13             :return})

(def ^:const ^:private allowed-keys
  "Only using this because the event needs to be handled in the handler.
  Therefore it is necessary to know which events do prevent and which to let pass."
  {:app            (select-keys key-map [220 #{:shift 32} #{:option 32} #{:option 191}])
   :editor         (select-keys key-map [9 37 38 39 40])
   :notepad-editor (select-keys key-map [40 13 9])
   :search         (select-keys key-map [221 219])})

(defn modifier-keys [event]
  (let [modi (zipmap
               [:meta :ctrl :option :shift]
               (map #(aget event %) ["metaKey" "ctrlKey" "altKey" "shiftKey"]))
        active-modi (set
                      (keep (fn [[k v]]
                              (when (true? v)
                                k))
                            modi))]
    (when (seq active-modi)
      (set active-modi))))

(defn get-key [source event]
  (let [keycode (.. event -keyCode)
        modi (modifier-keys event)
        key-or-combo (if modi
                       (conj modi keycode)
                       keycode)]
    (get-in allowed-keys
            [source key-or-combo])))

(defn handle-shortcuts
  ([source event]
   (handle-shortcuts source nil event))
  ([source opts event]
   (let [keycode (.-keyCode event)]
     (prn source "-> KEYCODE:" keycode)
     (when-let [key (get-key source event)]
       (let [message (if opts
                       [:key-down source key opts]
                       [:key-down source key])]
         (async/put! action-chan message))
       (.preventDefault event)))))
