(ns example.reconciler
  (:require [om.next :as om]
            [example.state :as state]))

(defonce reconciler
  (om/reconciler
    {:state state/app-state
     :parser state/parser
     :normalize true}))
