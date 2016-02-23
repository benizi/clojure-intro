class: center, middle, title
title: Intro to Functional Programming with Clojure

# Intro to Functional<br>Programming with Clojure

---

class: center, middle, title

# Intro to Clojure<br>(&amp; Functional Programming)

---

## About Me
- [Benjamin R. Haskell](http://benizi.com):
    Twitter [@benizi](https://twitter.com/benizi),
    GitHub [benizi](https://github.com/benizi)
    Web [benizi](http://benizi.com/)
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
- Built some toy apps:
    - [yu2.be](http://yu2.be/)
        - really simple YouTube® link shortener
        - Running on Heroku
    - [these slides](https://github.com/benizi/clojure-intro)
        - [Ring](https://github.com/ring-clojure/ring)/[Compojure](https://github.com/weavejester/compojure) application                
        - that parses HAML/Markdown with Ruby
        - Munges deck.js templates with [enlive](https://github.com/cgrand/enlive)
        - Syntax highlighting with Pygments (in Ruby)

---

## FP and Me
- FP = Functional Programming
- Haskell
    - Learned Me a Haskell at [learnyouahaskell.com](http://learnyouahaskell.com/)
    - Highly recommended language to learn (influential)
    - Learned it to configure XMonad (and because of my name)
    - Once wrote meaningful monadic code
- Erlang
    - Learned Me Some Erlang at [learnyousomeerlang.com](http://learnyousomeerlang.com/)
    - Built minimal apps, hacked a bit on Riak-CS
- Etc.
    - Scala, F♯ - intro tutorials or a bit more

---

## What is FP?
- No one really agrees
- Often: functions as first-class objects
    - Functions as values you can create/pass
    - Functions that take/return functions => (higher-order functions)
- Simplest (what I'm running with / my bias):
    - Focus on functions, not classes

---

## Why FP?
- Referential Transparency
    - What you see is what you get
- Functional Purity
    - Mathematical provability, optimizable
- Elegant code
    - Functional = shorter
- Not Object-Oriented
    - Modeling is hard or gets in the way
- Actions over lists
    - The most common monad

---

## Referential Transparency
- Calling the same function with the same arguments will always produce the
  same results

```ruby
## Referentially opaque
$global_counter = 0
def get_counter
  $global_counter += 1
end
```

```ruby
## Referentially transparent
def next_value(counter)
  counter + 1
end
```

---

## Referential Transparency
- Calling the same function with the same arguments will always produce the same results
- Makes code easier to reason about
    - Only need to know inputs to understand the output
- Prevents action at a distance
    - Not relying on hidden state

---

## Functional Purity
- Referential Transparency + No side effects

```ruby
## Impure function
def print_table(table)
  table.each { |row| puts "ROW: #{row}" }
end
```

```ruby
## Pure function
def printable_rows(table)
  table.map { |row| "ROW: #{row}" }
end
```

---

## Functional Purity
- Referential Transparency + No side effects
- Allows optimizations
    - Can memoize anything
- Makes testing/simulation easier
    - No worries about affecting the environment

---

## Elegant Code
- Not 100% subjective
- Functional code tends to have less boilerplate
- Haskell quicksort

```haskell
qsort :: Ord a => [a] -> [a]
qsort []     = []
qsort (p:xs) = (qsort less) ++ [p] ++ (qsort more)
    where
        less = filter (< p) xs
        more = filter (>= p)
```

---

## Not Object-Oriented
- Very subjective
- Not _forced to be_ Object-Oriented
- Primary unit of OOP design tends to be class hierarchies
    - Object = what the things in the program are
- Primary unit of FP design is the function
    - Function = what the program does

---

## Actions over lists

  > It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures
  > <br>\- Alan Perlis

- Libraries of functions tend to be better-composable
- More things than you'd think involve actions over lists
- (aside) List = the simplest-to-comprehend monad

---

## Things to Keep in Mind
- It's a spectrum. Different FP languages have different emphases
    - Haskell - strong emphasis on functional purity
    - Scala - embraces Object Oriented Programming
- You don't have to use an FP language to practice FP techniques
    - JavaScript - [Functional JavaScript](http://www.functionaljavascript.com/)
    - Ruby - Enumerable
    - Python - list comprehensions
- Many FP features mutually reinforce each other
    - Immutability => Referential Transparency

---

## Clojure in bullet points
- Heavy emphasis on immutability
- Built-in concurrency constructs
    - Hard: Locks, mutexes, semaphores
    - Better: Software Transactional Memory
- A Modern Lisp
    - Homoiconicity - Code is data
    - Modern = Syntactic sugar for non-list data types
    - REPL: Read, Eval, Print, Loop
- On the JVM (Clojure) or Node.JS/browser (ClojureScript)
    - Very easy to consume libraries
    - Embraces the host platform

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

## Immutability in Clojure
- Core data structures all Hash-Array Mapped Tries
    - Elegant: Structural sharing minimizes copying
    - Clever: 5-bit indices squashed into 32-bit ints
    - Efficient: O(log<sub>32</sub>(N)) operations (effectively O(1))
- Doesn't extend to Java objects
    - Mutability is a big concern with Java interop

---

## Hash-Array Mapped Tries

<img style="width: 40%" src="/images/persistenthashmap1.png"/>
<img style="width: 40%" src="/images/persistenthashmap-pathcopy.png"/>

---

## Hash-Array Mapped Tries

Highly recommended reading: [Karl Krukow blog post](http://blog.higher-order.net/2010/08/16/assoc-and-clojures-persistenthashmap-part-ii.html)

---

## Clojure
- Rest of talk is introduction to Clojure coding
- [Himera](http://himera.herokuapp.com) - online ClojureScript REPL by
  [@fogus](https://twitter.com/fogus).
  Follow along(!).
- [http://himera.herokuapp.com](http://himera.herokuapp.com)
<iframe src="http://himera.herokuapp.com/index.html"
  style="width: 100%; min-height: 4in" scrolling="no">
</iframe>

---

## Clojure Basics - Comments
```clojure
; line-based
; everything after semicolon ignored by reader

;; function-like
(comment ...) ; => nil
[1 (comment 2) 3] ; => [1 nil 3]

; most useful for top-level forms
(comment defn somefunction ...)

;; Reader macro #_
#_(...) ; form is completely elided
[1 #_2 3] ; => [1 3]

;; #! shell compatibility
#!/usr/bin/clojure      ; not usually a thing,
                        ; but would be ignored
```

---

## Clojure Basics - Data types
```clojure
nil      ; equivalent to Java null      ;; nil

true                                    ;; Booleans
false    ; only nil and false
         ; are "falsey"

(map class [                            ;; Numbers
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
(class #"regular expressions")
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
(name :stringy)   ; => "stringy"        ;; Keywords
::resolved        ; => :user/resolve
(in-ns 'whatever)
::resolved        ; => :whatever/resolve

'some-thing       ; => some-thing       ;; Symbols
'whatever/thing   ; => whatever/thing
'questionable?    ; => questionable?
'warn-!-yay       ; => warn-!-yay
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

I'm being hand-wavy. The actual situation is [...more complex](http://blog.fogus.me/2012/03/27/compiling-clojure-to-javascript-pt-3-the-himera-model/),
[especially for ClojureScript](http://blog.fogus.me/2012/04/25/the-clojurescript-compilation-pipeline/)

---

## Clojure evaluation - Lists
- Lists (and symbols) are special
    ```clojure
    (eval (read-string "(first-item)"))
    ; => CompilerException ...
    ; Unable to resolve symbol: first-item
    ```
- First item is resolved to something...

---

## Clojure evaluation - ...function
- If it's a function:
  1. Evaluate the remaining items
  2. Pass them as arguments to the function

```clojure
(x (y 1) (z "a"))
; evaluate (y 1)
;   evaluate 1 => 1
;   call y with 1 => y-result
; evaluate (z "a")
;   evaluate "a" => 1
;   call z with "a" => z-result
; call x with y-result and z-result => x-result
```

---

layout: true

```clojure
;; Vars
#'name ; => (var name)

;; Deref
@value ; => (deref value)

;; Metadata
#^{:doc "string"} value ; => (with-meta value {:doc "string"})
```

---

layout: true

## Confusing - Namespaces
- Collection of Vars
- Always a "current" namespace
```clojure
(ns com.benizi.project.core
  (:refer [clojure.core.async :as async]) ;; alias
  (:use [clojure.core.async]) ;; discouraged
  (:import [java.net.URL]) ;; import Java class
  (:import [java.util.concurrent
                BlockingQueue
                LinkedBlockingQueue])) ;; multiple
```

---

layout: false

## Evaluation - ...special forms
- If it's a special form, evaluate it specially
  - .special[def] interns symbol in the current namespace
    ```clojure
    (def symbol init?)
    ```
    Special because it's very low-level
  - .special[if] evaluates a test and then one of the two branches
    ```clojure
    (if test       ; test is evaluated first
      when-true    ; not evaluated if test is false
      when-false)  ; not evaluated if test is true
    ```
  Special because parts of the form need to remain unevaluated

---

## Evaluation - ...special forms
- .special[do] runs the forms it contains, discarding the results except the last
    ```clojure
    (do
      (println "Yay") ; => prints: Yay
      1234) ; => evaluates to 1234
    ```
- .special[quote] prevents evaluation of its arguments
    ```clojure
    (quote (rm-rf "/"))
    ; => (rm-rf "/")
    ```
  Special because its arguments are unevaluated
- Equivalent to the reader macro: .special[']
    ```clojure
    (= (quote (a b c))
       '(a b c))
    ```

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

## Clojure Let, binding forms
```clojure
;; Evaluate body with local bindings as expressed by [forms]
(let [forms] body)

;; Many binding forms:

name value         ; binds value to name
(let [a (+ 1 2 3)]
  a) ; => 6

[a b ...] value    ; binds a to 1st element
                   ; binds b to 2nd element
                   ; ...
(let [[a b] '(x y)]
  (prn {:a a :b b})) ; => prints: {:a x, :b y}
```

---

## Binding forms (cont.)
```clojure
;; Map-like binding forms

(def req {:request-type :get, :url "/"})

{name :key}        ; pulls :key out of a map-like
(let [{x :request-type} req]
  x) ; => :get

{:keys [a b c]}    ; pull multiple keys at once,
                   ; where key is same as name
(let [{:keys [request-type url]} req]
  (str "TYPE: " request-type " for " url))
; => "TYPE: :get for /"

{:keys [...] :as name}  ; also assign whole to name
(let [{:keys [url] :as r} req]
  (= r req)) ; => true
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

<!--format: true-->

<!-- ## Concurrency-->
<!--- Divided along two axes-->
<!--    - Coordinated vs Uncoordinated-->
<!--    - Synchronous vs Asynchronous-->
<!--- Four built-in reference types-->
<!--    - Coordinated synchronous: refs-->
<!--    - Uncoordinated synchronous: atoms-->
<!--    - Uncoordinated asynchronous: agents-->
<!--    - (vars solve a different problem)-->

<!------->

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

(map identity [1 2 3]) ; => (1 2 3)          ;; result is list
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

## Functional programming
```clojure
;; Fold over a list
(reduce f initial-value? collection)
(reduce + 0 [1 2 3 4])
; => like repeatedly doing: (f value next-value)
; => (+ 0 1) => 1
; => (+ 1 2) => 3
; => (+ 3 3) => 6
; => (+ 6 4) => 10
```

---

## Functional programming
```clojure
;; Composition
(comp f g) ; => function that applies g, then f to the result
(comp inc -) ; => function that negates, then increments
((comp inc -) 42) ; => -41

;; Partial application
(partial f other-args...) ; => function that applies f to args and other-args

(def map-inc (partial map inc))
(map-inc [1 2 3 4]) ; => (2 3 4 5)

(def five-adder (partial + 5))
(five-adder 10 20) ; => 35
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

layout: true

## Clojure Basics - Defining functions
```clojure
;; defn is a macro, which expands to def of a (fn) form
(defn entry-printer
  "This is an example of binding" ; doc-string
  [[key val]]
  (println key " => " val))

(entry-printer [:a 1])
; => prints:
; :a => 1

(dorun (map entry-printer {:a 1 :b 2}))
; => prints:
; :a => 1
; :b => 2
; => nil
```

---

layout: true

## Clojure Namespaces
- Map of symbols to vars
- Named like Java packages (reverse domain names)
- Example: `com.benizi.superlibrary`

```clojure
;; ns macro
(ns some.namespace
  (:require [clojure.repl :as repl])             ;; loads and aliases
  (:require [clojure.repl])                      ;; just loads
  (:use [clojure.repl]) ; discouraged            ;; imports everything
  (:require [clojure.repl :refer (doc source)])  ;; import specific vars
  (:import [java.lang String])                   ;; String is available
  ...)
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
-
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
    - Excellent introduction, tour of language:
      [Clojure Programming](http://www.clojurebook.com/)
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
    - Learn through koans [clojure-koans](https://github.com/functional-koans/clojure-koans)
    - Practice problems [4clojure](http://www.4clojure.com)
        - user: [benizi](http://www.4clojure.com/user/benizi)
        - current rank: 48/25,702.
        - Quantity. Not quality.
    - Good for any language, but very math-heavy: [Project Euler](http://projecteuler.net/)
- Web sites for reference
    - Community documentation [ClojureDocs](http://clojuredocs.org)
    - Search over code (really useful for examples)
      [{:get 'clojure}](http://getclojure.org/)
      e.g. search for [conj](http://getclojure.org/search?q=conj&num=0)
- IRC
    - \#clojure on freenode
