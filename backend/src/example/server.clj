(ns example.server
  (:require [ring.util.response :as response]
            [ring.middleware.cors :refer [wrap-cors]]
            ;; maybe not?
            [reloaded.repl :refer [system]]
            [example.transit :refer [wrap-transit]]
            [example.parser :as parser]))

(defn conn
  "Get Datomic connection from the system map"
  []
  (get-in system [:db :conn]))

#_(defn pull-out-tempids
    "Gross. I have to get the tempids up a level, but it should be easier."
    [response]
    (->> response
      (map (fn [entry]
             (if (symbol? (first entry))
               (let [[action value] entry
                     tempids (get-in value [:result :tempids] {})
                     new-value (-> value
                                 (assoc :tempids tempids)
                                 (util/dissoc-in [:result :tempids]))]
                 [action new-value])
               entry)))
      (into {})))

(defn handle-query [req]
  (let [query (:body req)
        ;; TODO identity
        resp (parser/parser {:conn (conn) :identity (:identity req)} query)]
    (-> resp
      #_(pull-out-tempids)
      (response/response))))

(def app
  (-> handle-query
    (wrap-transit)
    (wrap-cors
      :access-control-allow-origin [#".*"]
      :access-control-allow-methods [:post])))
