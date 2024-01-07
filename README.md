## Current progress

* Currently only the JVM is supported, but adding other platforms should be easy
* filling out the events and state handling parts

# Knockout Simplicity: KMIRC

KMIRC is short for Kotlin Multiplatform IRC. Coincidentally, they are my initials too - Kyle McBurnett.

The primary goal of this project is educational, specifically for gaining KMP experience. It's also a personal challenge
to strictly adhere to the Dependency Inversion Principle.

However, the long term objective is to build this into a product that people can find useful.

## Understanding the Dependency Inversion Principle

The Dependency Inversion Principle dictates that:

- High-level modules should not depend on low-level modules. Instead, both should depend on abstractions.
- Abstractions should not depend on details. On the contrary, details should depend on abstractions.

## Implementing the Rules

Here are the rules for this project:

1. Abstractions are not permitted to reference any implementations.
2. Implementations of an abstraction are not allowed to reference other implementations.

## Practical Approach

C language offers an excellent abstraction mechanism - header files. They allow one to use `struct DoWork;` in the
header file with the most complex implementation in the source file. I intend to mimic this approach using Gradle
modules.

Each module will consist of two submodules:

- A header module where interfaces are declared and the logic-free value classes are present.
- An implementation module.

I acknowledge that there might be at least one exception to this rule. For instance, the irc entities module is purely
filled with domain-specific data classes and enums, devoid of logic. This single level module conforms to the header
module rule.

As for the test code, it shall remain highly coupled to its implementation for the time being as KMP doesn't offer test
interfaces. Also, each module is likely to have only one implementation, meaning an abstract set of tests wouldn't be
beneficial.