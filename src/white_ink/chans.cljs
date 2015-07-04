(ns white-ink.chans
  (:require [cljs.core.async :as a]))

(def action-chan (a/chan))
(def task-chan (a/chan))
(def task-pub (a/pub task-chan first))

