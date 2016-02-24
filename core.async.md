class: center, middle, title
title: Short Intro to core.async

# Short Intro to [core.async](https://github.com/clojure/core.async)

---

## Sources
- David Nolen - Clojure Library Core.async for Asynchronous Programming
  - [YouTube talk](https://www.youtube.com/watch?v=AhxcGGeh5ho)
  - [GitHub code used in samples](https://github.com/swannodette/hs-async)
- Timothy Baldridge - Clojure/conj 2013 talk on core.async
  - [YouTube video](https://www.youtube.com/watch?v=enwIIGzhahw)
  - [GitHub code](https://github.com/halgari/clojure-conj-2013-core.async-examples)
- Live coding in [Emacs-live](http://overtone.github.io/emacs-live/)

---

## About Me
- [Benjamin R. Haskell](http://benizi.com/):
    Twitter [@benizi](https://twitter.com/benizi),
    GitHub [benizi](https://github.com/benizi)
- Day Job: [Forever&trade;](http://www.4moms.com/) (mostly Ruby)
--

- [#1 (tied) on 4clojure](http://www.4clojure.com/users) (quantity, not quality)

![4clojure stats](/clojure-intro/images/4clojure.png)
---

## About core.async
- Implementation of Communicating Sequential Processes (CSP)
- Provides a Channel abstraction
- Similar to how Go(lang) implemented Channels
- Works well in both Clojure and ClojureScript

---

## Leiningen coordinates
```clojure
;; in project.clj
(defproject my.project "0.1.0-SNAPSHOT"
  ;; ...
  :dependencies [ ;; ...
  [org.clojure/core.async "0.1.267.0-0d7780-alpha"] ;; ...
```

```clojure
;; in your code that uses core.async
(ns my.project.core
  (:require [core.async :as async
             ;; commonly:
             :refer (go put! take!)]))
```

---

## Channels
- You can create them
    - without a buffer
        ```clojure
        (def a-channel (chan))
        ```
    - with a buffer of size `n`
        ```clojure
        (def a-channel (chan n))
        ```
      [chan](http://clojure.github.io/core.async/index.html#clojure.core.async/chan)

---

## Channels - close
- You can close them
    ```clojure
      (close! a-channel)
    ```
    [close!](http://clojure.github.io/core.async/index.html#clojure.core.async/close!)

---

## Channels - put
- You can put things onto them
    - parking - only in go block
        ```clojure
        (>! a-channel 100)
        ```
    - blocking
        ```clojure
        (>!! a-channel 100)
        ```
    - non-blocking
        ```clojure
        (put! a-channel 100)
        ```
      [&gt;!](http://clojure.github.io/core.async/index.html#clojure.core.async/%3E!) |
      [&gt;!!](http://clojure.github.io/core.async/index.html#clojure.core.async/%3E!!) |
      [put!](http://clojure.github.io/core.async/index.html#clojure.core.async/put!)

---

## Channels - take
- You can take things off of them
    - parking - only in go block
        ```clojure
        (<! a-channel)
        ```
    - blocking
        ```clojure
        (<!! a-channel)
        ```
    - non-blocking
        ```clojure
        (take! a-channel)
        ```
      [&lt;!](http://clojure.github.io/core.async/index.html#clojure.core.async/%3D!) |
      [&lt;!!](http://clojure.github.io/core.async/index.html#clojure.core.async/%3D!!) |
      [take!](http://clojure.github.io/core.async/index.html#clojure.core.async/take!)

---

## Go macro
<iframe src="http://clojure.github.io/core.async/index.html#clojure.core.async/go"
  scrolling="no" style="width: 80%; height: 10em; overflow: hidden"></iframe>
--
<br>
Rewrites your code using crazy inversion-of-control macros, creating a state
machine scaffolding for turning blocking takes/puts into parked, individual
lightweight threads

---

## Go macro
- .strike[Rewrites your code using crazy inversion-of-control macros, creating
  a state machine scaffolding for turning blocking takes/puts into parked,
individual lightweight threads]
- Allows you to write blocking code as if it weren't
    ```clojure
    (let [c (chan)]
      (go
        (println (<! c)))
      (go
        (>! c :some-value)))
    ;; => prints :some-value
    ;; despite the take appearing before the put
    ```

---

## alts! and alts!!
- Perform the next available operation on a set of channels
- Go(lang) select, similar to Linux select call
    ```clojure
    (def a (chan))
    (def b (chan))
    (put! a 42)
    (alts!! [a b])
    ```
- alts! - in a go block
- alts!! - outside

[alts!](http://clojure.github.io/core.async/index.html#clojure.core.async/alts!) |
[alts!!](http://clojure.github.io/core.async/index.html#clojure.core.async/alts!!)

---

## Types of channels
### timeout
- Times out after n milliseconds
- Useful with alts!/alts!!
  ```clojure
  (let [c (chan)
        t (timeout 100)]
    (let [[v ch] (alts!! [c t])]
      (prn {:ch ch :v v})))
  ```

---

## Types of channels
### dropping buffer
- When full, puts can complete, but won't have any effect
    ```clojure
    (async/dropping-buffer n)
    ```

[dropping-buffer](http://clojure.github.io/core.async/index.html#clojure.core.async/dropping-buffer)

---

## Types of channels
### sliding buffer
- When full, puts can complete, but first item still in the channel is dropped
    ```clojure
    (async/sliding-buffer n)
    ```

[sliding-buffer](http://clojure.github.io/core.async/index.html#clojure.core.async/sliding-buffer)
