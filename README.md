# bdd-vs-unit-testing

This article and the accompanying sample project compares traditional Unit testing and Behaviour Driven
Development (BDD) testing. We will attempt to show that these are complimentary approaches with each addressing a 
different goal and concern.

There seems to be some confusion in the world about what BDD is. It is often seen as a style or a different approach of 
writing unit or integration test. We intend to show that BDD has a completely different goal.  

The project also acts as a reference of how to separate and configure the two testing types in a Gradle project.

## What is Unit Testing?

> Unit testing, a.k.a. component or module testing, is a form of software testing by which isolated source code is
> tested to validate expected behavior.
>
> Unit testing describes tests that are run at the unit-level to contrast testing at the integration or system level.
>
> -- <cite>Wikipedia</cite>

Thus, the purpose of a unit test is to validate if a source code class, component or module behaves as intended by the
developer.

When a unit test fails, it means that the software does not behave as the developer intended, i.e. a bug.

Unit tests tend to be closely coupled to the code that it tests. For example, a unit test class **PersonServiceTest**
most likely tests a class called **PersonService** and its internal classes that form a part of the same component.
The **PersonServiceTest** would hold a reference to a **PersonService** and call methods directly on it. Thus, if the
**PersonService** is refactored to change the method signature, the **PersonServiceTest** would no longer compile and
would have to be refactored as well.

Here is a diagram to show how unit testing works:

![Unit Test](/Documentation/Unit%20Test.drawio.png)

## What is BDD?

> Behavior-driven development (BDD) involves naming software tests using domain language to describe the behavior of the
> code.
>
> BDD involves use of a domain-specific language (DSL) using natural-language constructs (e.g., English-like sentences)
> that can express the behavior and the expected outcomes.
>
> Proponents claim it encourages collaboration among developers, quality assurance experts, and customer representatives
> in a software project. It encourages teams to use conversation and concrete examples to formalize a shared
> understanding of how the application should behave. BDD is considered an effective practice especially when the
> problem space is complex.
>
> -- <cite>Wikipedia</cite>

Thus, BDD is a way of writing down the requirements of a system in a language accessible to non-technical and technical
people alike.

When a BDD test fails it means that the software does not yet behave as per the requirements, but it does not
necessarily indicate a bug. It more than likely means the requirement has not been implemented yet.

With BDD we want to decouple the BDD test from the code being tested as much as possible. This is done for technical
people to keep our options open to change the architecture and/or design as we learn more. It is also done for
non-technical people to not alienate them by bringing technical terms into a business discussion about requirements.

We want to write our BDD tests in a language that focuses on what the user wants to achieve and completely avoid
technical and design decisions. For example, a BDD test should be so technology-agnostic that it does not even hint at
whether the solution we will develop will be a Command Line Interface (CLI), REST API, SOAP Web Service, Messaging
interface, Website or Mobile App.

Instead of writing a test with language like:

```
Given the user clicks the login link
And the user enters the username "admin" and the password "pwd123"
When the user clicks the login button
Then the user should see the welcome page
```

Rather use language like:

```
Given the user wants to log in
And specifies their valid login credentials
When the user logs in
Then the user is successfully logged in
```

Notice that the first example almost certainly refers to a Web application and the second example is
technology-agnostic with regard to user interface technology and authentication credentials.

## How does BDD work?

In programming when we want to decouple two things, one way to do this is with a layer of abstraction. And more
specifically to create an interface for the abstraction with a client calling the interface and a service implementing
the technical concerns to obscure them from the client.

This is exactly what BDD does. Here is a diagram to show the design pattern for BDD tools like Cucumber etc.

![BDD](/Documentation/BDD.drawio.png)

But if we understand this pattern, it is actually possible to create BDD tests without any BDD tools like Cucumber if we
want to avoid the additional complexity and learning curve that they bring.

While I am not seeking to discourage the usage of really great tools like Cucumber, I think that understanding how to do
BDD without these tools will make us better at using the tools. This is similar to some people advocating for learning a
programming language like Java without the usage of an IDE before we introduce an IDE.

## Explaining this example project

Here are some important things to look at while looking at the code.

### Separate BDD tests from unit tests

Using gradle java plugin to separate BDD tests from the standard unit tests.

In the [build.gradle](./build.gradle) file we added the following configuration:

```
testing {
  suites {
    bddTest(JvmTestSuite) {
      dependencies {
        implementation project()
      }
      configurations {
        bddTestImplementation {
          extendsFrom testImplementation
          extendsFrom testRuntimeOnly
        }
      }
    }
  }
}
```

This configuration sets up a gradle test suite and a gradle task called **bddTest**.
The source code for these tests will be placed in **/src/bddTest/java** and the resources will be under
**/src/bddTest/resources**.

If you run the gradle **build** or **check** tasks, the BDD tests will not be run by default.

I am of the opinion that a failing BDD tests should not result in a failed CI/CD build or prevent merging a code branch,
as this will prevent us from writing BDD requirements before they are implemented. But if your situation is different,
you can add the following configuration to your [build.gradle](./build.gradle) file.

```
tasks.named('check') {
  dependsOn testing.suites.bddTest
}
```

### Viewing the BDD test result

The standard gradle JUnit5 test report does not provide details about the steps of the test, i.e. the Given, When and
Then statements.

To improve this we added the [Allure report](https://allurereport.org/) gradle plugin to
the [build.gradle](./build.gradle) file.

```
id 'io.qameta.allure' version '2.12.0'
```

This automatically adds the correct setup of the classes needed for Allure reports.

Notice the file [GreetingFeature.java](./src/bddTest/java/net/binarypaper/bdd_vs_unit_testing/bdd/GreetingFeature.java)
class is annotated with **@Story** to specify the User Story that will appear in the test report.

Notice the
file [GreetingServiceDriver.java](./src/bddTest/java/net/binarypaper/bdd_vs_unit_testing/bdd/GreetingServiceDriver.java)
class methods are annotated with **@Step** to specify the test steps i.e. Given, When and Then that will appear in the
test report.

To view the test report first run the gradle **bddTest** task to run the tests.
Then run the gradle **allureServe** task to view the test report.

Here is as example of the main Allure report screen that shows statistics about the test results:

![BDD](/Documentation/Allure%20report%20main.png)

Here is as example of the Allure report behaviours screen that shows the BDD specification of a specific test:

![BDD](/Documentation/Allure%20report%20behaviours.png)