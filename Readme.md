# Smartest

## Compiling

To compile smartest, you need a Java Development Kit (JDK), Maven and clone the repository:

    cd smartest
    mvn clean package

## Execution

    cd target
    java -jar smartest.jar

### Flag

    -h, --help | Show this help message and exit.
    -v, --version | Print version information and exit.

### Options

    commit | Run tests then record changes to the repository.
    list-tests | List the tests that cover the changes between commits.
    test | Run tests on the selected scope.


