(ns example.transit
  (:require [cognitect.transit :as transit]
            [om.transit :as om-transit])
  (:import [java.io ByteArrayOutputStream]))

(defn fetch-and-clear-byte-array [^ByteArrayOutputStream byte-array]
  (let [result (.toString byte-array)]
    (.reset byte-array)
    result))

(defn transit-encode [value]
  (let [transit-out (ByteArrayOutputStream. 4096)
        writer (om-transit/writer transit-out)]
    (do
      (transit/write writer value)
      (fetch-and-clear-byte-array transit-out))))

(defn wrap-transit
  [handler]
  (fn [request]
    ;; TODO - handle requests without bodies?
    (-> request
      (update :body #(transit/read (om-transit/reader %)))
      (handler)
      (update :body transit-encode))))
