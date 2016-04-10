(ns example.components.base
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]
            [cljs.pprint :as pprint]
            [example.state :as state]))

(defui ^:once Todo
  static om/IQuery
  (query [this]
    [:db/id :todo/text])

  static om/Ident
  (ident [this props]
    [:todo/by-id (:db/id props)])

  Object
  (render [this]
    (let [props (om/props this)
          delete (-> this om/get-computed :todo/delete-fn)]
      (html
        [:li (:todo/text props)
         [:button.btn.btn-sm.btn-danger
          {:on-click #(delete (:db/id props))}
          "x"]]))))

(def todo (om/factory Todo))

(defn delete-todo [c id]
  (om/transact! c `[(todo/delete {:db/id ~id})]))

(defui ^:once Base
  static om/IQuery
  (query [this]
    [{:todo/list (om/get-query Todo)}])

  Object
  (render [this]
    (let [props (om/props this)]
      (html
        [:div.container
         [:button.btn {:on-click #(om/transact! this `[(todo/create {:db/id ~(om/tempid)})])}
          "New Todo"]

         (let [delete-fn (partial delete-todo this)]
           (for [t (:todo/list props)]
             (todo (-> t
                     (om/computed {:todo/delete-fn delete-fn})))))

         #_[:div
            (pr-str @state/app-state)]]))))
