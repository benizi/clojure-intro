(ns presentron.core
  (:use [compojure.core :only (defroutes GET)])
  (:require [presentron.slides :as slides]
            [hiccup.core :as h]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.adapter.jetty :refer (run-jetty)]
            [ring.util.response :as response]))

(defn as-html
  "Marks a response as HTML"
  [response]
  (response/content-type response "text/html;charset=utf-8"))

(defn slide-response
  "Returns a response for the slides in a given HAML file"
  [filename]
  (try
    (as-html (response/response (slides/presentation filename)))
    ;;(catch Exception e (response/response (.getMessage e)))
    (catch Exception e :next)))

(defroutes presentron
  (GET "/" [] (slide-response "intro.haml"))
  (GET ["/:deck" :deck #"[A-Za-z0-9.\-]+"] [deck] (slide-response (str deck ".haml")))
  (route/files "/core" {:root "deck.js/core"})
  (route/files "/extensions" {:root "deck.js/extensions"})
  (route/files "/themes" {:root "deck.js/themes"})
  (GET "/modernizr.custom.js" [] (response/file-response "deck.js/modernizr.custom.js"))
  (GET "/jquery-1.7.2.min.js" [] (response/file-response "deck.js/jquery-1.7.2.min.js"))
  (route/not-found "Route not found"))

(defn -main [& [port]]
  (run-jetty (handler/api presentron) {:port (Integer. (or port (System/getenv "PORT") 5000))}))
