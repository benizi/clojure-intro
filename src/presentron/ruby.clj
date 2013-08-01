(ns presentron.ruby
  (:require [clojure.string :as string])
  (:import [java.io File]
           [org.jruby.embed ScriptingContainer]))

(defn- in-ruby
  [string & {:keys [vars]}]
  (let [c (ScriptingContainer.)]
    (do
      (dorun
       (map (fn [[k v]] (.put c (name k) v))
            (or vars {})))
      (.runScriptlet c string))))

(defn parse-haml
  [text]
  (in-ruby "
require \"rubygems\"
require \"haml\"
require \"coderay\"
require \"active_support/core_ext/string\"

CodeRay::Scanners.list.each do |scanner|
  mod_name = \"#{scanner}\".classify
  # warn \"Installing Haml::Filters::#{mod_name}Code\"
  eval <<-SCANNER
    module Haml::Filters::#{mod_name}Code
      include Haml::Filters::Base
      def render(text)
        begin
          CodeRay.scan(text, :#{scanner}).div.sub(/(?<=\"CodeRay\")(?=>)/, \"kind=\\\\\"#{mod_name}\\\\\"\")
        rescue => e
          \"<pre>\" + e.message + \"</pre>\"
        end
      end
    end
  SCANNER
end

module Haml::Filters::Marked
  include Haml::Filters::Base
  def render(text)
    %Q{<markdown>#{text}</markdown>}
  end
end

engine = Haml::Engine.new(html)
engine.render.gsub(\"&#x000A;\", \"\\n\")
" :vars {"html" (string/replace text "#{" "\\#{")}))
