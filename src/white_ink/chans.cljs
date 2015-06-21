(ns white-ink.chans
  (:require [cljs.core.async :as a]))

(def action-chan (a/chan))
(def event-chan (a/chan))
(def events-pub (a/pub event-chan first))

