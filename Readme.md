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

*Usage*: `smartest list-tests [-s=<scope>]`
__List__ the tests that cover the changes between commits.

| flag  | Description |
| ------------- | ------------- |
| `-s, --scope`  | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be __listed__ |

#### Test

*Usage*: `smartest test [-s=<scope>]`
__Run__ tests on the selected scope.

| flag  | Description |
| ------------- | ------------- |
| `-s, --scope`  | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be __executed__ |


#### Commit

*Usage*: `smartest commit -m=<message> [-s=<scope>]`
__Run__ tests and then __commit__ changes to the repository.

| flag  | Description |
| ------------- | ------------- |
| `-m, --message=<message>`  | Use the given `<msg>` as the __commit message__ |
| `-s, --scope=<scope>` | Give the scope of the analysis (class, method, method and dependencies...), therefore only the test of this scope will be __executed__ |


## Plugins

Smartest supports plugins for the __language__ parsing (Java, Python), the __production tool__ (Maven, Gradle), the __test framework__ (Junit, Pytest) and the __vcs__ (git, svn).
We provide an implement for Java as a language, Maven as a production tool, Junit5 as a test framework and git as a vcs.
If you want to create your own plugin, you have to implement one of the following interfaces and put the jar of your plugin in the plugins directory. The path to the plugins directory can be changed in the `config.smt` of your project.


### Dependency

To create your own plugin, firstly, add the Maven dependency to the plugin project

    <dependency>
        <groupId>fr.smartest</groupId>
        <artifactId>plugin</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
### Implement on of the following interfaces

#### Plugin Interfaces (Common)

```java
public interface Plugin {
    boolean accept(String identifier);
}
```

The `accept` method has to return true if the identifier given correspond to your plugin. For example, a Java plugin should return `true` if the identifier is `java` and false otherwise.

#### Language

```java
interface Language extends Plugin {
    void setUp(List<Module> modules);
    Set<Test> getTestsRelatedToChanges(String scope, Set<Diff> diff);
    void save();
}
```

#### Production Tool

```java
public interface ProductionTool extends Plugin{
    void setUp(String path);
    List<Module> getModules();
    void compile() throws ProductionToolException;
}
```

#### Test Framework

```java
public interface TestFramework extends Plugin {
    void setUp(String path, List<Module> modules);
    Set<TestReport> run(Set<Test> tests) throws TestFrameworkException;
}
```

#### VCS

```java
public interface VCS extends Plugin {
    void setUp(String VCSpath);
    void commit(String message) throws VCSException;
    Set<Diff> diff() throws VCSException;
    void checkout(String version);
    void update();
}

```

### Add it to Smartest

__Compile and package__ your plugin to a jar and then put it in the plugin directory of your project.
Put the corresponding __identifier__ of your plugin in the `config.smt` of your project.
You are ready to go !
