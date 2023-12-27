# Sample GraalVM project

Testing benefits of native images

## Requirements

- sdkman
- GraalVM (java 21)
- Gradle 8.5+

## Setup

In order to play with native images, a native-capable jvm is needed. 

The easiest way to get a jvm like that is using [sdkman.io][sdk]:

```bash
sdk list java | grep graal
````

Should produce an output like this:

     GraalVM CE     |     | 21.0.1       | graalce |            | 21.0.1-graalce      
                    |     | 17.0.9       | graalce |            | 17.0.9-graalce      
     GraalVM Oracle |     | 21.0.1       | graal   |            | 21.0.1-graal        
                    |     | 17.0.9       | graal   |            | 17.0.9-graal  

Install the proper jdk:

```bash
sdk install java 21.0.1-graal
```

Another option is to use the command `sdk env install` inside this project
folder, since it has a `.sdkmanrc` file.

## Running on jvm vs running a native build

Once you get the proper jvm, use gradle to build the project:

```bash
./gradlew build test shadowJar
```

It calls the [shadow jar plugin][shadow-jar] to create a single, batteries
included, runnable jar file.

Run it normally:

```bash
java -jar build/libs/sample-graalvm-all.jar
```

The expected output is more or less like this:

    17:51:23.616 [main] INFO  io.javalin.Javalin - Starting Javalin ...
    17:51:23.623 [main] INFO  org.eclipse.jetty.server.Server - jetty-11.0.16; built: 2023-08-25T19:43:30.438Z; git: bedff458c4dd1a716d59e17b8cb0d2042eeab291; jvm 21.0.1+12-jvmci-23.1-b19
    17:51:23.743 [main] INFO  o.e.j.s.s.DefaultSessionIdManager - Session workerName=node0
    17:51:23.759 [main] INFO  o.e.j.server.handler.ContextHandler - Started o.e.j.s.ServletContextHandler@18ece7f4{/,null,AVAILABLE}
    17:51:23.770 [main] INFO  o.e.jetty.server.AbstractConnector - Started ServerConnector@32b260fa{HTTP/1.1, (http/1.1)}{0.0.0.0:7070}
    17:51:23.803 [main] INFO  org.eclipse.jetty.server.Server - Started Server@17497425{STARTING}[11.0.16,sto=0] @1173ms
    17:51:23.803 [main] INFO  io.javalin.Javalin -
           __                  ___           _____
          / /___ __   ______ _/ (_)___      / ___/
     __  / / __ `/ | / / __ `/ / / __ \    / __ \
    / /_/ / /_/ /| |/ / /_/ / / / / / /   / /_/ /
    \____/\__,_/ |___/\__,_/_/_/_/ /_/    \____/
    
           https://javalin.io/documentation
    
    17:51:23.805 [main] INFO  io.javalin.Javalin - Javalin started in 336ms \o/
    17:51:23.809 [main] INFO  io.javalin.Javalin - Listening on http://localhost:7070/
    17:51:23.827 [main] INFO  io.javalin.Javalin - You are running Javalin 6.0.0-beta.4 (released December 16, 2023).

Some highlights:

- Jar file is ~20MB in size
- Execution startup time, according to the log, is 1173ms (jetty) + 336ms (javalin)
- Runs on any jvm version 21 or newer

### Prepare the native build

GraalVM list some [libraries required][native-libs] so compiler works properly

Then simply convert your app using `native-image`:

```bash
native-image -jar build/libs/sample-graalvm-all.jar -o build/sample-graalvm
```

Sample output:

    Warning: The option '-H:ReflectionConfigurationResources=META-INF/native-image/io.micrometer/micrometer-core/reflect-config.json' is experimental and must be enabled via '-H:+UnlockExperimentalVMOptions' in the future.
    Warning: Please re-evaluate whether any experimental option is required, and either remove or unlock it. The build output lists all active experimental options, including where they come from and possible alternatives. If you think an experimental option should be considered as stable, please file an issue.
    ========================================================================================================================
    GraalVM Native Image: Generating 'sample-graalvm' (executable)...
    ========================================================================================================================
    [1/8] Initializing...                                                                                    (5,9s @ 0,17GB)
    Java version: 21.0.1+12, vendor version: Oracle GraalVM 21.0.1+12.1
    Graal compiler: optimization level: 2, target machine: x86-64-v3, PGO: ML-inferred
    C compiler: gcc (redhat, x86_64, 13.2.1)
    Garbage collector: Serial GC (max heap size: 80% of RAM)
    1 user-specific feature(s):
    - com.oracle.svm.thirdparty.gson.GsonFeature
    ------------------------------------------------------------------------------------------------------------------------
    1 experimental option(s) unlocked:
    - '-H:ReflectionConfigurationResources' (origin(s): 'META-INF/native-image/io.micrometer/micrometer-core/native-image.properties' in 'file:///home/sombriks/git/sample-graalvm/build/libs/sample-graalvm-all.jar')
    ------------------------------------------------------------------------------------------------------------------------
    Build resources:
    - 11,57GB of memory (75,6% of 15,32GB system memory, determined at start)
      - 12 thread(s) (100,0% of 12 available processor(s), determined at start)
        Found pending operations, continuing analysis.
        [2/8] Performing analysis...  [*****]                                                                   (28,2s @ 0,76GB)
        7.560 reachable types   (80,5% of    9.395 total)
        10.931 reachable fields  (57,6% of   18.969 total)
        39.411 reachable methods (51,0% of   77.202 total)
        2.456 types,   126 fields, and 1.523 methods registered for reflection
        58 types,    55 fields, and    53 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
        [3/8] Building universe...                                                                               (4,0s @ 0,85GB)
    
    Warning: Resource access method java.lang.ClassLoader.getResource invoked at org.eclipse.jetty.util.TypeUtil.getClassLoaderLocation(TypeUtil.java:644)
    Warning: Resource access method java.lang.ClassLoader.getResources invoked at org.slf4j.LoggerFactory.findPossibleStaticLoggerBinderPathSet(LoggerFactory.java:230)
    Warning: Aborting stand-alone image build due to accessing resources without configuration.
    ------------------------------------------------------------------------------------------------------------------------
                            4,1s (10,3% of total time) in 86 GCs | Peak RSS: 1,64GB | CPU load: 8,44
    ========================================================================================================================
    Finished generating 'sample-graalvm' in 38,4s.
    Generating fallback image...
    Warning: Image 'sample-graalvm' is a fallback image that requires a JDK for execution (use --no-fallback to suppress fallback image generation and to print more detailed information why a fallback image was necessary).

Then Execute it:

```bash
./build/sample-graalvm
```

Output is quite the same of jar version, but:

- Executable file is ~10MB in size
- Execution startup time, according to the log, is 840ms (jetty) + 287ms (javalin)
- The binary is a regular executable (ELF 64-bit LSB executable, x86-64)

There is one important note however. This warning in the output:

    Warning: Image 'sample-graalvm' is a fallback image that requires a JDK for execution (use --no-fallback to suppress fallback image generation and to print more detailed information why a fallback image was necessary).

It means that we still need a proper jvm installed to run it.

The binary produced by adding the suggested `--no-fallback` flag is almost 40MB
in size and simply does not work.

If the code uses reflection there is no way a native image ges that properly, so
the binary is just a fancy way to produce runtime errors.

## Docker

Since it's a binary the usual trick to create the jar file and then just wrap it
inside an image will need that extra step.

Which means a [multi-stage build][msb].

There are official [GraalVM images][gvi] available. There are no official
graalvm on docker hub.

Build the docker image using the [sample dockerfile][df] and this command:

```bash
docker build -f src/infrastructure/Dockerfile -t sombriks/sample-graalvm:testing .
```

## Conclusion

All in all keep jvm dependency isn't a bad deal and the performance gain is
promising.

[sdk]: https://sdkman.io
[shadow-jar]: https://imperceptiblethoughts.com/shadow/getting-started/#getting-started
[native-libs]: https://www.graalvm.org/latest/reference-manual/native-image/#prerequisites
[msb]: https://docs.docker.com/build/building/multi-stage
[gvi]: https://github.com/orgs/graalvm/packages?repo_name=container
[df]: src/infrastructure/Dockerfile
