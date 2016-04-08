(set-env!
  :source-paths #{"src"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [adzerk/boot-cljs "1.7.170-3" :scope "test"]

                  ;; REPL
                  [adzerk/boot-cljs-repl "0.3.0" :scope "test"]
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]

                  [adzerk/boot-reload "0.4.7" :scope "test"]
                  [pandeiro/boot-http "0.7.0" :scope "test"]
                  [afrey/ring-html5-handler "1.0.0" :scope "test"]

                  [devcards "0.2.1-5" :exclusions [cljsjs/react] :scope "test"]

                  [org.clojure/clojurescript "1.8.40"]

                  ;; React
                  [cljsjs/react "15.0.0-rc.2-0"]
                  [cljsjs/react-dom "15.0.0-rc.2-0"]
                  [sablono "0.6.3"]
                  [org.omcljs/om "1.0.0-alpha32"]

                  [adzerk/env "0.2.0"]
                  [com.domkm/silk "0.1.1"]

                  [com.cognitect/transit-cljs "0.8.232"]

                  ;; Assets
                  [boot-fingerprint "0.1.1-SNAPSHOT"]

                  ;; Styles
                  [org.webjars.bower/bootstrap "4.0.0-alpha.2"]
                  [deraen/boot-sass "0.2.1" :scope "test"]])

(require
  '[adzerk.boot-cljs            :refer [cljs]]
  '[adzerk.boot-cljs-repl       :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload          :refer [reload]]
  '[pandeiro.boot-http          :refer [serve]]
  '[deraen.boot-sass            :refer [sass]]
  '[pointslope.boot-fingerprint :refer [fingerprint]])

(deftask build []
  (comp
    (speak)
    (cljs)
    (sass)
    (fingerprint)
    (target)))

(deftask run []
  (comp
    (serve :handler 'afrey.ring-html5-handler/handler :reload true :port 3001)
    (watch)
    (cljs-repl)
    (reload)
    (build)))

(deftask development []
  (merge-env! :source-paths #{"dev"})
  (task-options!
    cljs {:ids ["main"]}
    fingerprint {:skip true}
    reload {:on-jsload 'example.core/reload!})
  identity)

(deftask dev
  "Run application in development mode"
  []
  (comp
    (development)
    (run)))
