(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/env "0.2.0"]

                  [org.danielsz/system "0.2.0"]
                  ;; Replace with immutant and figure out immutant logging
                  [http-kit "2.1.19"]
                  [com.datomic/datomic-free "0.9.5206"]

                  [buddy "0.7.2"]

                  ;; middleware
                  [ring-cors "0.1.7"]
                  [ring-transit-middleware "0.1.2"]

                  [org.omcljs/om "1.0.0-alpha32"]])

(boot.core/load-data-readers!)

(require
  '[system.boot :refer [system run]]
  '[example.system :refer [prod-system]])

(deftask dev
  "Run a restartable system in the repl"
  []
  (comp
    (watch :verbose true)
    (system :sys #'prod-system :auto-start true :hot-reload true :files ["server.clj"])
    (speak)
    (repl :server true)))
