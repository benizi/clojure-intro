(ns presentron.ruby
  (:require [clojure.string :as string]
            [clojure.java.shell :as shell]
            [clojure.java.io :as io]))

(defn parser []
  (-> "haml-to-html.rb"
      io/resource
      io/reader
      slurp))

(def *parser* (parser))

(defn run-parser
  [stdin]
  (shell/sh "bundle" "exec" "ruby" "-e" (parser)
            :in stdin))

(defn css-styles
  []
  (-> (shell/sh
       "bundle" "exec"
       "ruby" "-rbundler/setup"
       "-e" "Bundler.require :default ; Pygments.styles.each { |s| puts Pygments.css(\".highlight.#{s}\", style:s) }")
      :out))

(def css (css-styles))

(defn safe
  "Escape evaluation characters"
  [ruby]
  (string/replace ruby "#{" "\\#{"))

(defn parse-haml
  [text]
  (let [{:keys [out err exit]} (run-parser (safe text))]
    (if (zero? exit)
      out
      (throw (Exception. (str "parse-haml failed:\n" err))))))
