(defproject mfw "0.1.0"
  :description "Chat bot that queries image galleries"
  :url "https://smug.city"
  :license {:name "ISC License"
            :url "https://opensource.org/licenses/ISC"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.5.1"]
                 [ring/ring-core "1.5.1"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.5.2"]
                 [clj-http "3.4.1"]
                 [cheshire "5.5.0"]]
  :plugins [[lein-ring "0.11.0"]]
  :ring {:handler mfw.core/app}
  :main ^:skip-aot mfw.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
