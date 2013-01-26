(ns helga.plugins.ci
  (:use helga.registry)
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]))

;; helga ci {{team}} build
;; helga ci {{team}} status
;; helga ci rooms
;; helga ci set room {{team}} {{room}}

;; needs to define a hook for the remote caller

(defplugin
  (:cmd
   "Talk to your CI and make it do things."
   #{"ci"}
   (fn [{:keys [bot conversation message args] :as cmd-map}]
     (log/info "called with" args)
     "not implemented yet"))
  (:init (fn [_]
           (log/info "init called")))
  (:cleanup (fn [_]
              (log/info "cleanup called"))))
