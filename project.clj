(defproject presentron "0.1.0-SNAPSHOT"
  :description "Simple app to inject HAML slides into Deck.JS"
  :url "https://github.com/benizi/clojure-intro"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [enlive "1.1.1"]
                 [me.raynes/cegdown "0.1.0"]
                 [org.jruby/jruby-complete "1.6.3"]
                 [compojure "1.0.2"]
                 [ring/ring-jetty-adapter "1.0.1"]
                 [hiccup "1.0.0-RC2"]]
  :main presentron.core)
