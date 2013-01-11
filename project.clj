(defproject skypeclj "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.skype/skypekit "1.0"]
                 [org.slf4j/slf4j-api "1.6.6"]
                 [org.slf4j/slf4j-log4j12 "1.6.6"]
                 [org.clojure/tools.logging "0.2.3"]
                 [org.clojure/tools.cli "0.2.2"]
                 [aleph "0.3.0-SNAPSHOT"]
                 [ring "1.1.6"]
                 [compojure "1.1.3"]
                 [camel-snake-kebab "0.1.0-SNAPSHOT"]
                 [me.raynes/conch "0.5.0"]
                 [clj-time "0.4.4"]
                 [org.clojure/tools.nrepl "0.2.0-RC2"]
                 [com.cemerick/friend "0.1.2"]]
  :dev-dependencies [[lein-checkouts "1.0.0"]]
  :plugins [[lein-marginalia "0.7.1"]
            [lein-cljsbuild "0.2.10"]]
  :cljsbuild {:builds [{:source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :jvm-opts ["-server"]
  :main skypeclj.core)
