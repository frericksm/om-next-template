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
     :id-key :db/id
     :normalize true
     :send #(remote/send-to-remotes! remotes %1 %2)}))
