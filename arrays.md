class: center, middle, title
title: Arrays

# Arrays

---

## Processing Related Values

Task: Send an email to each student

```
Dear {{name}},

You are awesome! Welcome to class.

Best,
Tech Elevator
```

Given a function that performs the actual send:

- `void sendEmail(String name)`

---

## Solution 1: variables for everyone

```java
String ben = "Ben Haskell";
String jack = "Jack Skellington";
String leela = "Turanga Leela";

sendEmail(ben);
sendEmail(jack);
sendEmail(leela);
```

---

## Solution 2: numbered variables

```java
String student1 = "Ben Haskell";
String student2 = "Jack Skellington";
String student3 = "Turanga Leela";

sendEmail(student1);
sendEmail(student2);
sendEmail(student3);
```

---

## Actual solution: Arrays

```java
String[] names = new String[3];
names[0] = "Ben Haskell";
names[1] = "Jack Skellington";
names[2] = "Turanga Leela";

for (String name : names) {
  sendEmail(name);
}
```

---

## Array basics

- An **array** holds an ordered list of **elements**
- Each element of an array has the **same type**
- Arrays have a **fixed length** that cannot be changed

???

- Other data structures allow you to change some of these constraints

---

## Array variables

- Declaring an array variable by appending `[]` to the name

```java
String names[];
int scores[];
double sum, readings[];
```

- Often initialized at the same time:

```java
String names[] = new String[3];
int scores[] = new int[1000];
double sum, readings[] = new double[10];
```

???

- can be interspersed with non-array

---

## Accessing array elements

- Arrays are indexed from `0`
- Put the index between square brackets after the variable name

```java
String[] names = new String[3];

// Set values of elements:

// Note: 0, 1, 2 (not 1, 2, 3)
names[0] = "Ben Haskell";
names[1] = "Jack Skellington";
names[2] = "Turanga Leela";

// Index doesn't have to be a constant
for (int i = 0; i < names.length; i++) {
  sendEmail(names[i]);
}
```

???

- also called "offset", since it's the distance from the start

---

## Array initializer

- Entire array can be initialized with special syntax

```java
String[] names = new String[3];
names[0] = "Ben Haskell";
names[1] = "Jack Skellington";
names[2] = "Turanga Leela";

// Equivalent:
String[] names = {"Ben Haskell", "Jack Skellington", "Turanga Leela"};
```

```java
int scores[] = new int[3];
scores[0] = 20;
scores[1] = 30;
scores[2] = 40;

// Equivalent:
int scores[] = {20, 30, 40};
```

---

## Arrays are reference types

- If you pass an array to a function, it can alter its contents

```java
void modifyArray(String[] array, int i, String newValue) {
  array[i] = newValue;
}

// ...
String[] letters = {"Alpha", "Bravo", "Charlie"};
modifyArray(letters, 2, "Chaplin");
System.out.println(letters[2]);
// => Chaplin
```
