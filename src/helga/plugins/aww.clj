(ns helga.plugins.aww
  (:use helga.registry)
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]
            [helga.hydra :as hydra]))

(defplugin
  (:cmd
   ""
   #{"aww"}
   (fn [{:keys [bot conversation message args] :as cmd-map}]
     (hydra/r bot))))
