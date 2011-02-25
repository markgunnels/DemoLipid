(defproject informatic_tools "1.0.0-SNAPSHOT"
  :description "FIXME: write"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [dk.ative/docjure "1.5.0-SNAPSHOT"]
                 [incanter "1.2.3"]
                 [clojureql "1.1.0-SNAPSHOT"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [compojure "0.6.0"]
                 [hiccup "0.3.4"]
                 [sandbar "0.3.3"]
                 [com.ashafa/clutch "0.2.4"]
                 [org.danlarkin/clojure-json "1.2-SNAPSHOT"]]
  :dev-dependencies [
                     [swank-clojure "1.3.0-SNAPSHOT"]
                     [lein-ring "0.3.2"]
                     ]
  :ring {:handler informatic_tools.core/app})
