(ns example.components.base
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :refer-macros [html]]))

(defui ^:once Base
  static om/IQuery
  (query [this]
    [:app/msg])

  Object
  (render [this]
    (let [props (om/props this)]
      (html
        [:div.container
         [:div (:app/msg props)]]))))
