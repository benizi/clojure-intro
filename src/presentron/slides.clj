(ns presentron.slides
  (:require [presentron.ruby :as ruby :refer (parse-haml)]
            [net.cgrand.enlive-html :as html]
            [me.raynes.cegdown :as md]))

(def deck-js
  (html/html-resource (.toURI (java.io.File. "deck.js/boilerplate.html"))))

(defn string->nodes
  "Converts an HTML string into HTML nodes"
  [string]
  (-> string
      java.io.StringReader.
      html/html-resource))

(defn with-style
  "Subs in the better stylesheet"
  [html]
  (html/transform html
                  [[:link (html/attr= :rel "stylesheet")]]
                  (fn [node]
                    (update-in node [:attrs :href] clojure.string/replace #"web-2\.0" "swiss"))))

(defn slide-from-content
  [content]
  ["\n" {:tag :section :attrs {:class "slide"} :content content}])

(defn with-slides
  "Subs in the slide content"
  [nodes slide-html]
  (let [slides (html/select slide-html [:body :> :*])
        title (html/select slide-html [:title])
        title (when-let [title (seq title)]
                (-> title first :content))
        style (html/select slide-html [:head :style])]
    (-> nodes
        (html/transform [:section] nil)
        (html/transform [:body] (html/prepend slides))
        (html/transform [:title] (if title (html/content title) identity))
        (html/transform [:head] (if style (html/append style) identity))
        ;; TODO - add marking for language
        ;; (html/transform [(html/attr? :kind)] (html/content "code yay"))
        )))

(defn without-comments
  "Removes slides that have class .comment"
  [nodes]
  (-> nodes
      (html/transform [:.slide.comment] nil)))

(defn with-markdown
  "Render Markdown content"
  [nodes]
  (-> nodes
      (html/transform [:markdown]
                      (fn [{:keys [content] :as el}]
                        (assoc el :content (string->nodes (md/to-html (apply str content))))))
      (html/transform [:markdown :p] html/unwrap)
      (html/transform [:markdown] html/unwrap)))

(defn with-highlighting-style
  "Adds in the CSS for syntax highlighting"
  [nodes]
  (-> nodes
      (html/transform [:head] (html/append {:tag :style :content ruby/css}))))

(defn render
  [nodes]
  (->> nodes
       html/emit*
       (apply str)))

(defn presentation
  [filename]
  (let [slides (-> filename slurp parse-haml string->nodes)]
    (-> deck-js
        with-style
        with-highlighting-style
        (with-slides slides)
        without-comments
        with-markdown
        render)))
