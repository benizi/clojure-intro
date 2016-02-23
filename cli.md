class: center, middle, title
title: CLI-fu

# Commandline-fu

---

## Execution model
- Programs
  - Input
      - One file
      - Two arrays of strings
  - Output
      - Two files
      - An integer

---

## Files
- In system calls, files are just integers
- STDIN (file descriptor 0)
- STDOUT (file descriptor 1)
- STDERR (file descriptor 2)

```c
// writes to stdout
write(1, "Hello, world!\n", 14);
```

```ruby
# writes to stdout
puts "Hello, world!"
```

---

## Two arrays of strings - #1: ARGV
- ARGV (vector of arguments)
- Name of program is the first argument

```c
int main(int argc, char **argv) {
  int i;
  for (i=0; i<argc; i++) {
    printf("[%d]=%s\n", i, argv[i]);
  }
}
```

```ruby
puts "[0]=#{$0}"
ARGV.each_with_index do |a,i|
  puts "[#{i}]=#{a}"
end
```

---

## Two arrays of strings - #2: ENV
- Treated as set of key-value pairs
- List of strings split on first "=" character

```c
int main(int argc, char **argv, char **env) { // not portable
  int i;
  for (i=0; env[i]; i++) {
    printf("ENV[%d]: %s\n", i, env[i]);
  }
}
```

---

## Output an integer
- "Exit status", "Error code"
- 0 = everything OK
- not 0 (often 1) = something went wrong

```c
int main(void) {
  return 1;
}
```

```bash
$ true ; echo $?
# => 0
$ false ; echo $?
# => 1
```

---

## How shells work
- Find the command(s) to run
- Wire up standard files
- Execute the pipeline
- Do control flow based on exit codes

```bash
$ /bin/echo pizza | grep z
# /bin/echo = fully qualified, no need to search
# grep = check each \`:`-separated entry in \`$PATH`
# open pipe = set of two files (UNIX sockets)
# run /bin/echo with fd1 = write end of pipe
# run /bin/grep with fd0 = read end of pipe
```

---

## Portable shell has very few constructs
```bash
if # condition
then # command
elif # condition
then # command
else # command
fi
```

```bash
case ... in
  pattern)
    # command
    ;;
esac
```

```bash
while # condition
do # command
done
```

```bash
for x in ...
do # command
done
```

---

## Surprise! "condition" = command
```bash
if # command
then # command
elif # command
then # command
else # command
fi
```

```bash
case ... in
  pattern)
    # command
    ;;
esac
```

```bash
while # command
do # command
done
```

```bash
for x in ...
do # command
done
```

---

## Double surprise! "[" is a command
```bash
$ which [
# => /bin/[

$ if [ -z "$var" ]
# if $var is empty:
# runs ["[", "-z", "", "]"]
```

- Use `[[ ... ]]` if your shell supports it
- POSIX allows `[[` and `]]` to have different semantics
- `bash` and `zsh` both do this
- More resilient (syntax-enhanced)

```bash
$ var="directory with spaces"
$ [ -d $var ]
# dash: 2: [: directory: unexpected operator
# bash: [: too many arguments
## works fine in zsh... where arg splitting is different
```

---

## Shell logic and variables
- Can set variables
- ENV variables are inherited
- `export` creates new ENV vars
- `&&` and `||` (`&&` binds tightly)

---

## Quoting
- Backslash = escape character
- Single quotes retain everything
- Double quotes retain everything except:
  - `$` => parameter expansion
  - ` => command substitution
  - Backslash only escapes:
      ```
      $ ` " \ <newline>
      ```
- Always need to be quoted:
    ```
    | & ; < > ( ) $ ` \ " ' <space> <tab> <newline>
    ```
- Sometimes need to be quoted:
    ```
     * ? [ # ~ = %
    ```

- HEREDOCs

---

## Commands
- `awk`
- `sed`
- `xargs`
- `jq`
