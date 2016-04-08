(ns example.reconciler
  (:require [om.next :as om]
            [example.env :as env]
            [example.state :as state]
            [example.remote :as remote]))

(def remotes
  {:remote {:url env/BACKEND_URL}})

(defonce reconciler
  (om/reconciler
    {:state state/app-state
     :parser state/parser
     :normalize true
     :send #(remote/send-to-remotes! remotes %1 %2)}))
