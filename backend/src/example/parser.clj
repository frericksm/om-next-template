(ns example.parser
  (:require [datomic.api :as d]
            [example.db.util :as db-util]
            [om.next.server :as om]))

;;;;;;;;;;;
;; Reads ;;
;;;;;;;;;;;

(defmulti readf (fn [env k params] k))

(defn todos
  "Pull todos from database"
  ([db] (todos db nil))
  ([db query]
   (d/q '[:find [(pull ?eid query) ...]
          :in $ query
          :where [?eid :todo/text]]
     db query)))

(defmethod readf :todos/list
  [{:keys [conn query]} _ _]
  {:value (todos (d/db conn) query)})

;;;;;;;;;;;;;;;
;; Mutations ;;
;;;;;;;;;;;;;;;

(defmulti mutate (fn [env k params] k))

(def parser (om/parser {:read readf :mutate mutate}))
