require "rubygems"
require "bundler/setup"
Bundler.require :default
require "active_support/core_ext/string"

CodeRay::Scanners.list.each do |scanner|
  mod_name = "#{scanner}".classify
  # warn "Installing Haml::Filters::#{mod_name}Code"
  eval <<-SCANNER
    module Haml::Filters::#{mod_name}Code
      include Haml::Filters::Base
      def render(text)
        "<!-- #{mod_name}Code -->" +
        begin
          div = CodeRay.scan(text, :#{scanner}).div
          div.sub(/(?<="CodeRay")(?=>)/, %Q{kind="#{mod_name}"})
        rescue => e
          "<pre>" + e.message + "</pre>"
        end
      end
    end
  SCANNER
end

Linguist::Language.all.each do |lang|
  name = lang.default_alias_name
  name = case name
         when "c++" ; then "cpp"
         when "c#" ; then "c-sharp"
         when /html\+/ ; then name.gsub('+', '-')
         when /[^a-z\-]/i ; then warn "Will fail: #{name}"
         else name
         end

  unless name
    warn "Unhandled language name: #{lang.default_alias_name}"
    next
  end

  mod_name = name.underscore.classify
  eval <<-SCANNER
    module Haml::Filters::Lang#{mod_name}
      include Haml::Filters::Base
      def render(text)
        "<!-- Lang#{mod_name} -->" +
        begin
          lang = Linguist::Language.find_by_alias("#{name}")
          div = lang.colorize(text, :options => { :cssclass => "#{name}" })
          div.sub(/(?<=class="highlight)/, %Q{ emacs" kind="#{mod_name}"})
        rescue => e
          "<pre>" + e.message + "</pre>"
        end
      end
    end
  SCANNER
end

Pygments.lexers.each do |_,lexer|
  name = lexer[:aliases].first
  next unless name =~ /^[a-z]+$/
  mod_name = name.underscore.classify
  eval <<-SCANNER
    module Haml::Filters::#{mod_name}Lexer
      include Haml::Filters::Base
      def render(text)
        "<!-- #{mod_name}Lexer -->" +
        begin
          Pygments.highlight(text, :lexer => "#{name}", :options => { :cssclass => "highlight emacs" })
        rescue => e
          "<pre>" + e.message + "</pre>"
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

engine = Haml::Engine.new(ARGF.read)
puts engine.render.gsub("&#x000A;", "\n")
