(ns helga.plugins.echo
  (:use helga.registry)
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string]))

(defplugin
  (:cmd
   "Echoes everything back to you. Even silence."
   #{"echo"}
   (fn [{:keys [bot conversation message args] :as cmd-map}]
     (if (seq args)
       (str (format "You said: \"%s\"" (string/join " " args)))
       "You decided to remain silent."))))
