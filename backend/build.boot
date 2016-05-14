(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/env "0.3.0"]

                  [org.danielsz/system "0.3.0-SNAPSHOT"]
                  [aleph "0.4.1"]
                  [com.datomic/datomic-free "0.9.5359"]

                  [buddy "0.13.0"]

                  ;; middleware
                  [ring-cors "0.1.7"]

                  [org.omcljs/om "1.0.0-alpha34"]])

(boot.core/load-data-readers!)

(require
  '[system.boot :refer [system run]]
  '[example.system :refer [prod-system]])

(deftask dev
  "Run a restartable system in the repl"
  []
  (comp
    (watch :verbose true)
    (system :sys #'prod-system :auto true :files ["server.clj"
                                                  "errors.clj"
                                                  "cors.clj"])
    (speak)
    (repl :server true)))
