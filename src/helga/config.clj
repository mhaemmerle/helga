(ns helga.config
  (:require [clojure.java.io :as io])
  (:import [java.io PushbackReader]))

(defn load-config (binding [*read-eval* false]
                    (with-open [r (io/reader "resources/config.clj")]
                      (read (PushbackReader. r)))))
