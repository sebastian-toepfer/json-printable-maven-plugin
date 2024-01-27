[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sebastian-toepfer_json-printable-maven-plugin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sebastian-toepfer_json-printable-maven-plugin)
[![Reproducible Builds](https://img.shields.io/badge/Reproducible_Builds-ok-success?labelColor=1e5b96)](https://github.com/jvm-repo-rebuild/reproducible-central#io.github.sebastian-toepfer.json.rpc:json-printable-maven-plugin)

# JSON Printable maven plugin
Generate java classes form the openrpc json schema that implement the printable interface.

## Should you use it?
in short: no, because it is very limited.

longer version: If you are willing to customize it to your needs without breaking it. Deploy the changes and use it. But be warned, I had not written any tests for it, it was more a PoC than a real project.

## Why I developed it
I am very dissatisfied with other code generators.
1. if a JSON schema defines some needed properties, why should it be possible to create an object without them? With this generator it is not possible, any required property must be provided at the time of creation. Currently it is not 100% validated, only that it is present.
2. immutable and fluent, yes fluent is posible, but it changes the inner state.
   so instead of
   ```
   new Person().firstname("michael").lastname("jackson");
   ```
   you must wrote
   ```
   new Person("jackson").withFirstname("michael");
   ```
    but we can create the jackson five simply:
   ```
   final Person jackson = new Person("jackson");
   final Collection jacksonFive = List.of(
     jackson.withFirstname("jackie"),
     jackson.withFirstname("tito"),
     jackson.withFirstname("jermaine"),
     jackson.withFirstname("marlon"),
     jackson.withFirstname("michael")
   );
   ```
the object can be converted to json via:
   ```
   michael.printOn(new JsonObjectMedia());
   ```
   okay a lot of code, but you can print it as every media you own :)

## How to use it
add the default printables to your project
```
<dependency>
  <groupId>io.github.sebastian-toepfer.ddd</groupId>
  <artifactId>printables-core</artifactId>
  <version>0.4.0</version> <!-- this is the min. version -->
</dependency>
```
and the plugin
```
<plugin>
  <groupId>io.github.sebastian-toepfer.json.rpc</groupId>
  <artifactId>json-printable-maven-plugin</artifactId>
  <version>0.2.1</version>
  <executions>
    <execution>
      <goals>
        <goal>generate</goal>
      </goals>
      <configuration>
        <packageName>io.github.sebastiantoepfer.json</packageName>
      </configuration>
    </execution>
  </executions>
</plugin>
```
