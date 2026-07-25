# Calculator

A lightweight, modular Java Calculator project providing arithmetic and scientific operations, expression parsing, history, and a simple programmatic API. This repository is organized to separate core logic, CLI/GUI entry points, and tests so the calculator can be used as a library or as a standalone application.

## Table of Contents

- [Features](#features)
- [Repository structure](#repository-structure)
- [Requirements](#requirements)
- [Build & Run](#build--run)
- [Usage examples](#usage-examples)
  - [Command-line](#command-line)
  - [Programmatic API](#programmatic-api)
- [Testing](#testing)
- [Design & Implementation notes](#design--implementation-notes)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- Basic arithmetic: add, subtract, multiply, divide
- Scientific functions: sin, cos, tan, log, ln, pow, sqrt
- Expression parsing with operator precedence and parentheses
- Memory and history (store/retrieve previous operations)
- High-precision calculations (BigDecimal support)
- Pluggable operations: easy to add new function modules
- CLI runner and programmatic Java API for embedding in other apps
- Unit tests for core operations and parser

## Repository structure

Note: update the tree below if your package or folder names differ.

- README.md
- LICENSE
- build.gradle / pom.xml                # Gradle or Maven build file (one of these)
- src/
  - main/
    - java/
      - com/moshood/calculator/
        - App.java                      # CLI / main application entry (optional)
        - Calculator.java               # Core calculator facade (API)
        - Parser.java                   # Expression parser / evaluator
        - operations/
          - Operation.java              # Operation interface
          - BasicOperations.java        # +, -, *, /
          - ScientificOperations.java   # sin, cos, pow, sqrt, ...
        - history/
          - HistoryManager.java         # store/list previous expressions/results
        - util/
          - BigDecimalUtils.java        # helpers for precision & rounding
    - resources/                       # optional resources (localization, configs)
  - test/
    - java/
      - com/moshood/calculator/
        - CalculatorTest.java
        - ParserTest.java
- docs/                                 # optional docs and sample expressions
- examples/
  - cli-example.sh                       # shell script showing CLI usage

## Requirements

- Java 11+ (works with Java 8 if no modules are used; BigDecimal behavior tested on 11+)
- Maven or Gradle (depending on which build tool is included)
- (Optional) IDE: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

## Build & Run

With Maven:
- Build: mvn clean package
- Run (CLI jar): java -jar target/calculator-1.0.jar
- Run tests: mvn test

With Gradle:
- Build: ./gradlew clean build
- Run (CLI jar): java -jar build/libs/calculator-1.0.jar
- Run tests: ./gradlew test

If your project produces an artifact named differently, replace the jar name above.

## Usage examples

### Command-line
If App.java provides a CLI, the following are example invocations:

- Interactive mode:
  java -jar calculator-1.0.jar

- One-shot evaluation:
  java -jar calculator-1.0.jar "2 + 3 * (4 - 1)"
  Output:
  11

- Evaluate a file of expressions:
  java -jar calculator-1.0.jar -f expressions.txt

### Programmatic API
Example of embedding the Calculator into another Java program:

```java
import com.moshood.calculator.Calculator;

public class Example {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        String expr = "sin(0.5) + 2^3 / 4";
        // evaluate returns BigDecimal or double depending on your implementation
        var result = calc.evaluate(expr);
        System.out.println(expr + " = " + result);
    }
}
```

API surface (typical):
- Calculator.evaluate(String expression) : BigDecimal
- Calculator.evaluate(String expression, MathContext mc) : BigDecimal
- Calculator.addOperation(Operation op) : void
- HistoryManager.getHistory() : List<HistoryEntry>

Adjust method names and packages to match your implementation.

## Testing

- Unit tests are in src/test/java. Tests should cover:
  - Parser correctness and operator precedence
  - Edge cases (division by zero, invalid input)
  - Scientific functions and precision
  - History and memory operations
- Run via your build tool: `mvn test` or `./gradlew test`.

## Design & implementation notes

- Parser: typically uses either a shunting-yard algorithm to convert infix expressions into RPN or a recursive descent parser to evaluate expressions directly.
- Precision: use BigDecimal to avoid floating-point rounding issues; expose configuration for scale and rounding mode.
- Extensibility: operations implement an Operation interface. New operations can be registered with Calculator at startup.
- Thread-safety: the core evaluator should be stateless or synchronize access to mutable state (history, memory).
- Error handling: parser and evaluator throw clear exceptions (ParseException, EvaluationException) with human-friendly messages.

## Contributing

1. Fork the repository
2. Create a feature branch: git checkout -b feature/your-feature
3. Implement your changes and add tests
4. Run tests locally
5. Open a pull request describing the change

Please follow standard Java code style and include unit tests for new behavior.

## License

This repository is unlicensed by default. Add a LICENSE file (for example MIT, Apache-2.0) to allow others to use it. Example: `LICENSE` (MIT)

## Contact

Created by MoshoodSO. For questions and contributions, open an issue or pull request on GitHub:
https://github.com/MoshoodSO/Calculator
``` 

What I did: prepared the README.md content and enclosed it in a file block so you can copy/paste cleanly.  
Next step for you: paste this into README.md at the repo root, then commit and push. If you want, I can also show the exact git commands to create a branch and open a PR. Which would you prefer?
