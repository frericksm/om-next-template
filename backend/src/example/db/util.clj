(ns example.db.util
  (:require [datomic.api :as d]
            ;; [example.user :as user]
            [clojure.java.io :as io])
  (:import datomic.Util))

(defn db-seeded?
  [conn]
  (let [db (d/db conn)
        item-count (d/q '[:find (count ?eid) .
                          :where [?eid :todo/text]] db)]
    (and item-count
      (> item-count 0))))

(defn get-schema []
  (first (Util/readAll (io/reader (io/resource "data/schema.edn")))))

(defn get-seeds []
  (first (Util/readAll (io/reader (io/resource "data/seeds.edn")))))

(defn add-schema [conn]
  @(d/transact conn (get-schema)))

(defn add-seeds
  "Add initial data"
  [conn]
  (when (not (db-seeded? conn))
    @(d/transact conn (get-seeds))
    #_(user/create-first-user conn)))

(defn initialize-db
  "Initialize a Datomic db and return the db connection"
  [uri]
  (d/create-database uri)
  (doto (d/connect uri)
    (add-schema)
    (add-seeds)))

(defn resolve-tempid [tx-result temp-id]
  (d/resolve-tempid (:db-after tx-result) (:tempids tx-result) temp-id))
