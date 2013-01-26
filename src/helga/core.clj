(ns helga.core
  (:gen-class)
  (:use clojure.tools.cli)
  (:require [clojure.tools.logging :as log]
            [helga.bot :as bot]))

(defn at-exit
  [runnable]
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable runnable)))

(defn stop
  []
  (bot/stop))

(defn start
  []
  (bot/start)
  (at-exit stop))

(defn -main
  [& args]
  (start))
