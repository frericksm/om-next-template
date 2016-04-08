(ns example.state
  (:require [om.next :as om]
            [example.util :as util]))

(defonce app-state
  (atom {:todos/list [[:todos/by-id 1] [:todos/by-id 2]]
         :todos/by-id {1 {:db/id 1 :todo/text "First Todo"}
                       2 {:db/id 2 :todo/text "Second Todo"}}}))

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

(defn next-id [state]
  (->> state
    (:todos/list)
    (map second)
    (apply max)
    (inc)))

(defn create-todo [state]
  (let [id (next-id state)
        ref [:todos/by-id id]]
    (-> state
      (assoc-in ref {:db/id id :todo/text (str "Todo " id)})
      (update :todos/list conj ref))))

(defmethod mutate 'todo/create
  [{:keys [state]} _ _]
  {:action (fn [] (swap! state create-todo))})

(defn delete-todo [state id]
  (let [ref [:todos/by-id id]]
    (-> state
      (util/dissoc-in ref)
      (update :todos/list #(util/remove-from-vector % ref)))))

(defmethod mutate 'todo/delete
  [{:keys [state]} _ {:keys [db/id]}]
  {:action (fn []
             (swap! state delete-todo id))})

(def parser (om/parser {:read read :mutate mutate}))
