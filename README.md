# Acme Traverser

This project allows you to traverse directories and perform various actions on files.

## Running the Program

To run the program, use the following command:

```bash
java -cp acmeTraverser.jar fileTraverser.app.App [directory] [actions]
```
## Example
```bash
java -cp acmeTraverser.jar fileTraverser.app.App /Users/holly/Documents/FileTraverser/FileTraverser/src writeDates countLines calculateAverageFileSize
```

## Creating new actions

1. create a new function within src/main/java/fileTraverser/app/App.java
2. compile
```bash
javac -d target/classes src/main/java/fileTraverser/app/*.java
```
3. create the jar file
```bash
jar cvf acmeTraverser.jar -C target/classes .
```



