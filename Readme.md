# Smartest

## Compiling

To compile smartest, you need a Java Development Kit (JDK), Maven and clone the repository:

    cd smartest
    mvn clean package

## Execution

    cd target
    java -jar smartest.jar

### Flags

    | flag  | Description |
    | ------------- | ------------- |
    | -h, --help  | Show this help message and exit. |
    | -v, --version  | Print version information and exit. |
    | --config-path=<configPath> | Set path to config.smt file |

### Options

    | option  | Description |
    | ------------- | ------------- |
    | list-tests | List the tests that cover the changes between commits. |
    | test | Run tests on the selected scope. |
    | commit  | Run tests then record changes to the repository. |

#### List Tests

Usage: `smartest list-tests [-s=<scope>]`
List the tests that cover the changes between commits.

    | flag  | Description |
    | ------------- | ------------- |
    | -s, --scope  | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be listed |

#### Test

Usage: `smartest test [-s=<scope>]`
Run tests on the selected scope.

    | flag  | Description |
    | ------------- | ------------- |
    | -s, --scope  | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be executed |


#### Commit

Usage: `smartest commit -m=<message> [-s=<scope>]`
Run tests then record changes to the repository.

    | flag  | Description |
    | ------------- | ------------- |
    | -m, --message=<message>  | Use the given <msg> as the commit message |
    | -s, --scope=<scope> | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be executed |


