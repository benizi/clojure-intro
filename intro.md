class: center, middle, title
title: Intro to Clojure

# Intro to Clojure

---

## About Me
- [Benjamin R. Haskell](http://benizi.com): Twitter [@benizi](https://twitter.com/benizi), GitHub [benizi](https://github.com/benizi) Web [benizi](http://benizi.com/)
- Day Job: [Forever&trade;](https://www.forever.com/) (mostly Ruby)
- Worked professionally with
  - Perl, .NET (C♯, VB), JavaScript, PHP, Ruby
- Strong interests in
  - Go, Clojure, Erlang, Haskell, and Python

- Like tools that require a lot of configuration/practice to "get":
  - Vim, Zsh, Linux, XMonad

---

## Clojure and Me
- Interested in Clojure for a couple of years
- No production experience
- I hate virtually all of my Clojure code, despite making it available
- Solved all [4clojure.com](https://www.4clojure.com) problems.

---

## Clojure and Me - Toy apps
- [yu2.be](http://yu2.be/)
  - really simple YouTube® link shortener
  - Running on Heroku
- [these slides](https://github.com/benizi/clojure-intro)
  - [Ring](https://github.com/ring-clojure/ring)/[Compojure](https://github.com/weavejester/compojure) application
  - that parses HAML/Markdown with Ruby
  - Munges deck.js templates with [enlive](https://github.com/cgrand/enlive)
  - Syntax highlighting with Pygments (in Ruby)

---

## Immutability
- Mutability causes bugs, action at a distance
    ```ruby
    # Ruby
    puts data.inspect # { vital_data: 1, other: 2 }

    def evil_function(param)
      param.delete(:vital_data)
    end

    evil_function(data)

    puts data.inspect # { other: 2 }
    ```
- "But isn't immutability inefficient?"
  - Everything is "pass by value"
  - Lots of data copying

---

## Hash-Array Mapped Tries

<img style="width: 40%" src="/clojure-intro/images/persistenthashmap1.png"/>
<img style="width: 40%" src="/clojure-intro/images/persistenthashmap-pathcopy.png"/>

---

## Hash-Array Mapped Tries
- Core data structures all Hash-Array Mapped Tries
  - Elegant: Structural sharing minimizes copying
- Clever: 5-bit indices squashed into 32-bit ints
- Efficient: O(log<sub>32</sub>(N)) operations (effectively O(1))
- Doesn't extend to Java objects
  - Mutability is a big concern with Java interop

Highly recommended reading: [Karl Krukow blog post](http://blog.higher-order.net/2010/08/16/assoc-and-clojures-persistenthashmap-part-ii.html)

---

## Clojure
- Follow along(!)
- [Himera](http://himera.herokuapp.com) - online ClojureScript REPL by [@fogus](https://twitter.com/fogus).
- [http://himera.herokuapp.com](http://himera.herokuapp.com)
<iframe src="http://himera.herokuapp.com/index.html"
  style="width: 100%; min-height: 4in"
  scrolling="no">
</iframe>

---

## Clojure Basics - Comments
- Comments
  ```clojure
  ; line-based
  ; everything after ";" ignored by reader

  ;; function-like
  (comment ...) ; => nil
  [1 (comment 2) 3] ; => [1 nil 3]
  ```

- REPL conventions
  ```clojure
  user=> input ; this is user input
  ; output     ; this is printed output
  ; => val     ; this is a returned value
  ```

---

## Other comment types
  ```clojure
  ;; Reader macro #_
  #_(...) ; form is completely elided
  [1 #_2 3] ; => [1 3]

  ;; #! shell compatibility
  #!/usr/bin/clojure      ; not usually a thing,
                          ; but would be ignored
  ```

---

## Clojure Basics - nil, bools
- nil = equivalent to Java null
  ```clojure
  nil
  ```
- Booleans are Java Booleans
  ```clojure
  true false
  ```
- Only nil and false are "falsey"

---

## Clojure Basics - numerics
- Clojure has several built-in numeric types
  ```clojure
  (map class [
      1      ; => java.lang.Long
      1.2    ; => java.lang.Double
      1/2    ; => clojure.lang.Ratio
      1N     ; => clojure.lang.BigInt
      1M     ; => java.math.BigDecimal
  ])
  ```

---

## Clojure Basics - Strings
  ```clojure
  (class "asdf") ; => java.lang.String    ;; Strings

  ;; strings are sequences of characters
  (map identity "asdf") ; => (\a \s \d \f)

  ;; A few special names for characters
  (first "\n") ; => \newline

  ;; Just Characters underneath
  (class \newline) ; => java.lang.Character

  (def item "yay")
  ;; str stringifies and joins
  (str "a" item "b") ; => "ayayb"
  ```

---

## Clojure Basics - Regexps
  ```clojure
  #"regular expression"
  (class #"regular expression")
  ; => java.util.regex.Pattern

  ; fewer backticks: #"\d" is like "\\d"

  (re-seq #"\w" "asdf")
  ; => ("a" "s" "d" "f")

  (re-find #"sd" "blah asdf blah")
  ; => "sd"
  (re-find #"sd" "blah blah blah")
  ; => nil
  ```

---

## Clojure Basics - Symbolics
  ```clojure
  'some-thing       ; => some-thing       ;; Symbols
  'whatever/thing   ; => whatever/thing (namespaced)
  'questionable?    ; => questionable?
  'warn-!-yay       ; => warn-!-yay

  (name :stringy)   ; => "stringy"        ;; Keywords
  ::resolved        ; => :user/resolve
  (in-ns 'whatever)
  ::resolved        ; => :whatever/resolve
  ```

---

## Clojure Basics - List-alikes
  ```clojure
  (list 1 2 3) ; => (1 2 3)        ;; Lists

  ; quote prevents evaluation
  '(1 2 3) ; => (1 2 3)

  [1 2 3]                          ;; Vectors
  (vec '(1 2 3))  ; => [1 2 3]
  (vector 1 2 3)  ; => [1 2 3]
  ([5 6 7] 0)     ; => 5
  ```

---

## Clojure Basics - Indexed seqs
  ```clojure
  {:a 1 :b 2}                                 ;; Maps
  (hash-map :a 1 :b 2)   ; => {:a 1, :b 2}
  (sorted-map :a 1 :b 2) ; => {:a 1, :b 2}
  ({:a 1 :b 2} :a)       ; => 1
  ({:a 1 :b 2} :c)       ; => nil

  #{:a :b :c}                                 ;; Sets
  (hash-set :a :b :c)   ; => #{:a :b :c}
  (set [:a :b :c])      ; => #{:a :b :c}
  (sorted-set :a :b :c) ; => #{:a :b :c}
  ```

---

## Clojure - Order of operations
  ```clojure
  ;; 1. Read - Convert string representation to data
  (read-string "(1 2 3)") ; => (1 2 3)

  ;; 2. Macro expansion - Recursively expand macros
  (macroexpand '(defn foo [a] (+ 1 a)))
  ; => (def foo (clojure.core/fn ([a] (+ 1 a))))

  ;; 3. Evaluation - Most datatypes self-evaluate
  (eval 1)        ; => 1
  (eval :keyword) ; => :keyword
  (eval [1 2 3])  ; => [1 2 3]
  ```
--
Being hand-wavy. Actually [...more complex](http://blog.fogus.me/2012/03/27/compiling-clojure-to-javascript-pt-3-the-himera-model/),
[especially for ClojureScript](http://blog.fogus.me/2012/04/25/the-clojurescript-compilation-pipeline/)

---

## Clojure evaluation - Lists
- Lists and symbols are special
  ```clojure
  (first-item "arg1" 'thing)
  ; => CompilerException ...
  ; Unable to resolve symbol: first-item
  ```
- First item is resolved to something...
--

  - ... more about namespaces later
---

## Clojure evaluation - ...function
- If first thing in a list resolves to a function:
  1. Evaluate the remaining items
  2. Pass them as arguments to the function
- Example...

---

## Function evaluation example

```clojure
(prn (str (+ 1 4) 2 3))
 'prn                  ; resolves to function
  (str (+ 1 4) 2 3)    ; evaluate
   'str                ; resolves to function
    (+ 1 4)            ; evaluate
     '+                ; resolves to function
      1                ; evaluate => 1
      4                ; evaluate => 4
    '+ 1 4             ; apply => 5
    2                  ; evaluate => 2
    3                  ; evaluate => 3
  'str 5 2 3           ; apply => "523"
'prn "523"             ; apply
;   side effect: prints "523"
nil                    ; result
```

---

## Evaluation - special forms
- If it's a special form, evaluate it specially
- Examples...

---

## Special forms - def
- .special[def] interns symbol in current namespace
  ```clojure
  (def symbol init?)
  ```
- Special because it's very low-level
- .special[defn] is a macro around .special[def] (not a special form)

---

## Special forms - if
- .special[if] evaluates conditional, then one of two branches
  ```clojure
  (if test       ; test evaluated first
    when-true    ; evaluated if true
    when-false)  ; evaluated if false
  ```
- Special because parts remain unevaluated

---

## Special forms - do
- .special[do] runs the forms it contains
- discards results except the last
    ```clojure
    (do
      (+ 10 20)       ; => 30, but discarded
      (println "Yay") ; => prints: Yay
      1234)           ; => evaluates to 1234
    ; => 1234
    ```
- Special because it's essentially a no-op

---

## Special forms - quote
- .special[quote] prevents evaluation of its arguments
    ```clojure
    (quote (rm-rf "/"))
    ; => (rm-rf "/")
    ```
- Special because args are unevaluated
- Equivalent to the reader macro: .special[']
  ```clojure
        (= (quote (a b c))
           '(a b c))
  ```

---

## Evaluation - ...special forms
- About 20 special forms
  ```clojure
  ;; definitions, bindings
  def let quote var fn
  ;; control-flow
  if do loop recur
  ;; dealing w/ Exceptions
  throw try catch
  ;; Java Interop forms
  . new set!
  ```
- Otherwise, those are all the evaluation rules

---

## Functions on hash maps

```clojure
(:a {:a 1 :b 2}) ; => 1   ; keywords are functions
(:a #{:a :b})    ; => :a

; longs are not
(0 [1 2 3])   ; => ClassCastException
; nor are strings
("a" {"a" 1}) ; => ClassCastException

(get {:a 1 :b 2} :a)          ; => 1
(get {:a 1 :b 2} :c)          ; => nil
(get {:a 1 :b 2} :c :default) ; => :default

(keys {:a 1 :b 2}) ; => (:a :b)
(vals {:a 1 :b 2}) ; => (1 2)

(assoc {:a 1} :b 2)     ; => {:a 1 :b 2}
(dissoc {:a 1 :b 2} :b) ; => {:a 1}
```

---

## Clojure Basics - Sequences
```clojure
;; (cons first rest)
;; cons[tructs] a new seq by
;; appending first to the rest
(cons 4 '(1 2 3))    ; => (4 1 2 3)
(cons 4 [1 2 3])     ; => (4 1 2 3)
(cons 4 #{1 2 3})    ; => (4 1 2 3)             ;
(cons [:b 2] {:a 1}) ; => ([:b 2] [:a 1])
```

---

## Clojure Basics - Sequences
```clojure
;; (conj coll item)
;; conj[oins] efficiently
(conj '(1 2 3) 4)    ; => (4 1 2 3)
(conj [1 2 3] 4)     ; => [1 2 3 4]
(conj #{1 2 3} 4)    ; => #{1 2 3 4}
(conj {:a 1} [:b 2]) ; => {:b 2, :a 1}

;; (usually same concrete type)
;; position depends on concrete type
```

---

## Clojure Basics - Sequences
```clojure
;; (into coll a b c ...)
;; conj[oins] the rest of its arguments
;; into the first
(into [] 1 2 3 4)       ; => [1 2 3 4]
(into #{} 1 2 3 4)      ; => #{1 3 2 4}
(into {} [:a 1] [:b 2]) ; => {:a 1, :b 2}
```

---

## Binding forms
```clojure
;; eval body with local bindings
(let [bindings] body)

;; Many binding forms:

name value         ; binds value to name

(let [a (+ 1 2 3)] a)
;     | \- value = evaluated (+ 1 2 3)
;     \- name = a
; => 6
```

---

## Sequential binding forms
```clojure
[a b ...] value    ; binds a to 1st element
                   ; binds b to 2nd element
                   ; ...

(let [[a b] '(x y)] {:thing-1 a :thing-2 b})
; => {:thing-1 x :thing-2 y}

;; unmatched names get nil
(let [[a b] '(x)] {:thing-1 a :thing-2 b})
; => {:thing-1 x :thing-2 nil}
```

---

## Map-like binding forms
```clojure
;; example value
(def req {:type :get, :url "/"})

;; pulls :key out
{name :key}
(let [{x :type} req] x)
; => :get

;; multiple keys at once,
{:keys [a b c]}

(let [{:keys [type url]} req]
  {:t type :u url})
; => {:t :get :u "/"}
```

---

## Map-like binding forms
```clojure
;; also assign whole to a name
{:keys [...] :as name}

(let [{:keys [url] :as r} req] (= r req))
; => true

;; non-keyword keys:
{:strs [url]} value   ;; string keys
{:syms [url]} value   ;; symbol keys
```

---

## Lazy sequences
```clojure
;; generates integers starting at 0
(range) ; => returns lazy sequence: (0 1 2 3 4 ...)
(range x) ; => returns 0 to (dec x)
(range x y) ; => returns x to (dec y)

;; take, drop, and nth can limit the laziness
(take 4 (range)) ; => (0 1 2 3)
(drop 5 (range)) ; => lazy sequence: (5 6 7 8 ...)
(drop 5 (take 8 (range))) ; => (5 6 7)

(nth (range) 20) ; => 20
(nth (range) 0) ; => 0
```

---

## Functional programming
```clojure
;; Apply a function with seq as arguments
(apply + [1 2 3 4]) ; => 10
; same as: (+ 1 2 3 4)

;; Apply a fn with items of seqs in turn
(map f collection)
; => '((f (first collection))
;      (f (second collection))
;      ...)

;; result is a sequence
(map identity [1 2 3])  ; => (1 2 3)
(map identity '(1 2 3)) ; => (1 2 3)
(map identity #{1 2 3}) ; => (1 2 3)
```

---

## Map (cont.)
```clojure
(map str [{:a 1} [1 2] #{:a :b}])
; => ("{:a 1}" "[1 2]" "{:a :b}")

;; multiple collections stop when the shortest ends
(map vector [:a :b :c] (range))
; => ((vector :a 0) (vector :b 1) (vector :c 2))
; => ([:a 0] [:b 1] [:c 2])
```

---

## Map is lazy - dorun
```clojure
user=> (def x (map prn [1 2 3]))
; => #'user/x  ;; no prints

user=> (realized? x)
; => false

user=> x
; (1
; 2
; 3
; nil nil nil) ;; prints interspersed w/ result

user=> (dorun (map prn [1 2 3]))
; 1
; 2
; 3
; => nil ;; dorun forces realization, ignores result
```

---

## Functional programming
```clojure
;; Fold over a list
(reduce f initial-value? collection)
(reduce str "" [\a \b \c \d])
; => like repeatedly doing: (f value next-value)
; => (str "" \a)    ; => "a"
; => (str "a" \b)   ; => "ab"
; => (str "ab" \c)  ; => "abc"
; => (str "abc" \d) ; => "abcd"
```

---

## Functional programming
```clojure
;; Composition
(comp f g)          ; => fn that applies g, then f
#<core$comp$fn__4154...@2cb2e792>
(comp str inc)      ; => fn that inc's, then str's
((comp str inc) 42) ; => "43"

;; Partial application
(partial f other-args...)
; => fn that applies f to args and other-args

(def higher (partial map (comp str inc)))
(higher [1 2 3 4]) ; => ("2" "3" "4" "5")

(def blue-label (partial create-label :bg "blue"))
(blue-label "Some text") ; => blue label
;; from PEP 0309
```

---

## Clojure threading macros
```clojure
;; passes x to form1, result to form2, etc.
;; -> = as first arg
(-> x form1 form2 ...)
(-> 1 (inc) (* 2) (/ 7)) ; => 4/7
(/ (* (inc 1) 2) 7)

;; passes x to form1, result to form2, etc.
;; ->> = as last arg
(->> x form1 form2 ...)
(->> [:a :b] (map str) (apply str)) ; => ":a:b"
(apply str (map str [:a :b]))
```

---

## Clojure Basics - defn
```clojure
;; defn is a macro, which expands to def of a (fn) form
(defn entry-printer
  "This is an example of binding"  ; doc-string
  [[key val :as entry]]            ; parameters
  (println entry "=" key "=>" val) ; body
  )

(entry-printer [:a 1])
; [:a 1] = :a => 1
; => nil

(dorun (map entry-printer {:a 1 :b 2}))
; [:a 1] = :a => 1
; [:b 2] = :b => 2
; => nil
```

---

## Namespaces - use, import
- Namespaces are maps of symbols to Vars
- Use = import all symbols from another ns
  ```clojure
  user=> (use 'clojure.repl) ;; discouraged
  user=> #'doc
  #'clojure.repl/doc
  ```
- Import = Java Class imports
  ```clojure
  user=> (import 'java.io.Reader)
  user=> Reader
  java.io.Reader
  ;; multiple:
  user=> (import '[java.io Reader StringReader])
  ```

---

## Namespaces - load
- Load = lower-level loading
- Not frequently used directly(?)
  ```clojure
  ;; classpath-relative
  user=> (load "/clojure.set")

  ;; relative to current namespace directory
  presentron.core=> (load "something")
  ; => could not load presentron/something__init.class
  ;    or             presentron/something.clj
  ```

---

## Namespaces - require
- Require = load and optionally alias another ns
  ```clojure
  user=> (require 'clojure.repl)
  user=> (clojure.repl/doc str)

  ; :as lets you use a shorter namespace alias
  user=> (require '[clojure.repl :as repl])
  user=> (repl/doc str)

  ; :refer imports specific symbols
  user=> (require '[clojure.repl :refer (doc)])
  user=> (doc str)

  ; :refer :all = use
  user=> (require '[clojure.repl :refer :all])
  user=> (doc str)
  ```

---

## Namespaces - ns macro
- Wrapper around the functions mentioned
- No need to quote things in this macro
  ```clojure
  (ns com.benizi.superlibrary
    (:require [clojure.java.io :as io]
              [clojure.string :as string])
    (:require [clojure.repl :refer (doc source)])
    (:use [clojure.repl])
    (:import [java.lang String])
    ;...
    )
  ```

---

layout: true

## Clojure Basics - Don't forget immutability
```clojure
(def a {:a 1})   ; => #'user/a
(assoc a :b 2)   ; => {:a 1 :b 2}
a                ; => {:a 1}
```

---

layout: false

## Clojure evaluation - my experience

Some things that have helped me (maybe) understand what's going on
- Don't think in "infix": think of (x y z) as "apply x to y and z"
    ```clojure
    (+ 1 2 3) ; "apply sum to 1, 2, and 3"
    (< 1 2)   ; "apply 'in-order' to 1 and 2     ; => true
    (< 1 2 3) ; "apply 'in-order' to 1, 2, and 3 ; => true
    ```
- Hard to remember early on when things need to be in a list
- If you want it to be evaluated, put it in a list
- If you don't want evaluation, quote it, or use a vector

---

layout: true

## Clojure Basics - Reference types - refs
- Provides a wrapper around a normal entity
- Coordinates access to that entity
- Access is only available within a transaction

---

layout: false

## Links

- [Pittsburgh.clj GitHub](https://github.com/pittsburghclj)
- [These slides](https://github.com/pittsburghclj)

---

## More resources
- Good Books
  - Excellent introduction, tour of language: [Clojure Programming](http://www.clojurebook.com/)
    (better than: [Programming Clojure](http://pragprog.com/book/shcloj2/programming-clojure))
  - More philosophical "what makes Clojure different/fun" [The Joy of Clojure](http://joyofclojure.com/)
- Blog posts
  - Clojure's PersistentHashMap posts by Karl Krukow:
    [part i](http://blog.higher-order.net/2009/09/08/understanding-clojures-persistenthashmap-deftwice.html),
    [part ii](http://blog.higher-order.net/2010/08/16/assoc-and-clojures-persistenthashmap-part-ii.html)
- Talks
  - Rich Hickey
    [Are We There Yet?](http://www.infoq.com/presentations/Are-We-There-Yet-Rich-Hickey),
    [Simple Made Easy](http://www.infoq.com/presentations/Simple-Made-Easy) *highly recommended*

---

## Even more resources
- Web sites for practicing
  - Learn through koans
    [clojure-koans](https://github.com/functional-koans/clojure-koans)
  - Practice problems [4clojure](http://www.4clojure.com)
      - user: [benizi](http://www.4clojure.com/user/benizi)
              ![benizi](/clojure-intro/images/4clojure.png)
      - current rank: (T-)1/27,671.
      - Quantity. Not quality.
  - Good for any language, but very math-heavy: [Project Euler](http://projecteuler.net/)
- Web sites for reference
  - Community documentation
    [ClojureDocs](http://clojuredocs.org)
  - Search over code (really useful for examples)
    [{:get 'clojure}](http://getclojure.org/)
    e.g. search for [conj](http://getclojure.org/search?q=conj&num=0)
- IRC
