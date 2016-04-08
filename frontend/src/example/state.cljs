(ns example.state
  (:require [om.next :as om]))

(defonce app-state
  (atom {:app/msg "App state message"}))

;;;;;;;;;;
;; Read ;;
;;;;;;;;;;

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state]} k params]
  (let [st @state]
    (if (contains? st k)
      {:value (get st k)}
      {:remote true})))

;;;;;;;;;;;;
;; Mutate ;;
;;;;;;;;;;;;

(defmulti mutate om/dispatch)

(def parser (om/parser {:read read :mutate mutate}))
