(ns example.components.base
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]))

(defui ^:once Todo
  static om/IQuery
  (query [this]
    [:todo/text])

  Object
  (render [this]
    (let [props (om/props this)]
      (html
        [:li (:todo/text props)]))))

(def todo (om/factory Todo))

(defui ^:once Base
  static om/IQuery
  (query [this]
    [:app/msg
     {:todos/list (om/get-query Todo)}])

  Object
  (render [this]
    (let [props (om/props this)]
      (html
        [:div.container
         (for [t (:todos/list props)]
           (todo t))]))))
