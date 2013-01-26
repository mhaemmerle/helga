(ns helga.plugins.auth
  (:use helga.registry)
  (:require [clojure.tools.logging :as log]
            [clojure.string :as string])
  (:import [com.skype.api Conversation Message Participant
            Conversation$ParticipantFilter]))

(defn ^:private authorized?
  [user bot]
  (contains? (:authorized @bot) user))

(defn ^:private admin?
  [user bot]
  (contains? (:admins (:config @bot)) user))

(defn list-participants
  [conversation]
  (let [participants (.getParticipants conversation Conversation$ParticipantFilter/ALL)
        identities (reduce (fn [acc participant]
                             (conj acc (.getIdentity participant))) [] participants)]
    (string/join ", " identities)))

;; TODO give a hint, that a user is alread authorized
(defn authorize
  [bot conversation message args]
  (log/info "authorize" conversation message args)
  (if-let [user (first args)]
    (if (admin? (.getAuthor message) bot)
      (do
        (swap! bot update-in [:authorized] conj user)
        (str (format "Welcome aboard, %s." user)))
      "Yours is not the power to grant this.")
    "That's all nice and well, but I need a name!"))

(defn deauthorize
  [bot conversation message args]
  (log/info "revoke" args)
  (let [user (first args)]
    (if (and (not (nil? user))
             (contains? (:authorized @bot) user))
      (do
        (swap! bot update-in [:authorized] disj user)
        (str (format "Access rights of '%s' revoked succesfully." user)))
      (str (format "Either didn't find user '%s' or she wasn't authorized in the first place." user)))))

;; ask for authorization
;; generate key pair
;; reply with openid protected link that contains first key
;; store both keys
;; authorize on successful visit to the link

(defplugin
  (:cmd
   "Lists the identity of all conversation participants."
   #{"list"}
   (fn [{:keys [conversation] :as cmd-map}]
     (list-participants conversation)))
  (:cmd
   ""
   #{"authorized"}
   (fn [{:keys [bot conversation message args] :as cmd-map}]
     (if (authorized? (.getAuthor message) bot) "Yes." "No.")))
  (:cmd
   ""
   #{"authorize"}
   (fn [{:keys [bot conversation message args]}]
     (authorize bot conversation message args)))
  (:cmd
   ""
   #{"revoke"}
   (fn [{:keys [bot conversation message args]}]
     (deauthorize bot conversation message args)))
  (:init (fn [_]
           (log/info "init called")))
  (:cleanup (fn [_]
              (log/info "cleanup called"))))
