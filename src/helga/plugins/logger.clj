(ns helga.plugins.logger
  (:use helga.registry)
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]))

(defn log-message
  [{:keys [bot conversation message] :as m-bot}]
  (log/info "log-message" conversation message)
  )

;; command could be: helga logger get (date)

(defn logger
  [bot args]
  (log/info "logger"))

(defn init
  [what]
  (log/info "init" what))

(defn cleanup
  [what]
  (log/info "cleanup" what))

(defplugin
  (:hook :on-message #'log-message)
  (:cmd
   "the logger"
   #{"logger"}
   (fn [{:keys [bot conversation message args] :as cmd-map}]
     (log/info "logger" args)
     (logger bot args)))
  (:init (fn [_]
           (log/info "init called")))
  (:cleanup (fn [_]
              (log/info "cleanup called"))))

;; :routes
