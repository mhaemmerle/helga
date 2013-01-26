(ns helga.registry
  (:use [aleph http formats]
        [lamina core]
        [useful.fn :only [fix to-fix !]])
  (:require [clojure.tools.logging :as log]
            [helga.hydra :as hydra]))

;; taken from http://github.com/flatland/lazybot

(defn merge-with-conj [& args]
  (apply merge-with #(if (vector? %) (conj % %2) (conj [] % %2)) args))

(defn parse-fns
  [body]
  (apply merge-with-conj
         (for [[one & [two three four :as args]] body]
           {one
            (case one
              :cmd {:docs two
                    :triggers three
                    :fn four}
              :hook {two {:fn three}}
              :indexes (vec args)
              two)})))

(defn if-seq-error [fn-type possible-seq]
  (log/info "if-seq-error" fn-type possible-seq)
  (if (and (not (fn? possible-seq)) (seq possible-seq))
    (throw (Exception. (str "Only one " fn-type " function allowed.")))
    possible-seq))

(def make-vector (to-fix (! vector?) vector))

(defmacro defplugin [& body]
  (let [{:keys [cmd hook cleanup init routes]} (parse-fns body)
        scmd (if (map? cmd) [cmd] cmd)]
    (log/info "defplugin" cmd hook cleanup init routes)
    `(let [pns# *ns*
           p-name# (keyword (last (.split (str pns#) "\\.")))]
       (defn ~'load-this-plugin [bot#]
         (when ~init ((if-seq-error "init" ~init) bot#))
         (swap! bot# assoc-in [:plugins p-name#]
                {:commands ~scmd
                 :hooks (into {}
                              (for [[k# v#] (apply merge-with-conj
                                                   (make-vector ~hook))]
                                [k# (make-vector v#)]))
                 :cleanup (if-seq-error "cleanup" ~cleanup)
                 :routes ~routes})))))

(defn load-plugin
  [bot plugin]
  (log/info "load plugin #1")
  (let [ns (symbol (str "helga.plugins." plugin))]
    (require ns :reload)
    (log/info "load plugin #2")
    ((resolve (symbol (str ns "/load-this-plugin"))) bot)))

(defn find-command
  [bot cmd]
  (some #(when (= cmd (:triggers %)) %) (mapcat :commands (vals (:plugins @bot)))))

(defn pull-hooks
  [bot hook-key]
  (map :fn
       (hook-key
        (apply merge-with concat
               (map :hooks
                    (vals (:plugins @bot)))))))

(defn handle
  [bot conversation message cmd-fn args]
  (log/info "execute" cmd-fn)
  (try
    (cmd-fn {:bot bot :conversation conversation :message message :args args})
    (catch Exception exception
      (.getMessage exception))))
