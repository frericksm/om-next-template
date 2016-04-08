(ns example.state
  (:require [om.next :as om]))

(defonce app-state
  (atom {:todos/list [[:todos/by-id 1] [:todos/by-id 2]]
         :todos/by-id {1 {:todo/text "First Todo"}
                       2 {:todo/text "Second Todo"}}}))

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

(defmethod read :todos/list
  [{:keys [state query]} key _]
  (let [st @state]
    {:value (om/db->tree query (get st key) st)}))

;;;;;;;;;;;;
;; Mutate ;;
;;;;;;;;;;;;

(defmulti mutate om/dispatch)

(def parser (om/parser {:read read :mutate mutate}))
