# Guice Java Module Plugin example

This example project exposes [Guice](https://github.com/google/guice) dependency injection modules together with Java modules [JPMS/Java Platform Modukle System](https://en.wikipedia.org/wiki/Java_Platform_Module_System) in order to create a modular application, which even supports extending defined service interfaces.

This simple approach does need only one interface `ExtensionRegistry` with already provided default implementation `SimpleExtensionRegistry` which is only needed by the plugin defining the extension point. Any interface or class can be defined as contract to be implemented by additional plugins.

The general idea is to expose Guice module definitions as java service as explained in the article [Handling dependency injection using Java 9 modularity](https://www.oreilly.com/content/handling-dependency-injection-using-java9-modularity). A plugin just needs to define its Guice module class as java service
```
  provides com.google.inject.Module
    with de.vagtsi.examples.guicejavamodule.greeting.english.EnglishGreetingModule;
```
 in order to get loaded by the application core implementation on startup
 ```
  ServiceLoader<Module> pluginLoader = ServiceLoader.load(com.google.inject.Module.class);
 ```
 
Additionally this example creates one `Injector` instance for each module/plugin. In case of parent module dependencies defined by `requires` within the `module-info.java`, the `Injector` is created as child injector of that parent module in order to access any provided instances via `@Inject`. As an example the `EnglishGreetingServiceImpl` within the plugin `greeting-english` gets the `ExampleCoreService` injected provided by its parent plugin `greeting-core`.
To provide extension services, the service implementations just need to be provided in their Guice module
```
  @ProvidesIntoSet
  @Singleton
  public GreetingService englishGreetingService() {
    return new EnglishGreetingServiceImpl();
  }
```
to get automatically registered at the appropriate `ExtensionRegistry`.

The actual service implementations of each plugin are hidden in their `internal` packages. Only the package containing Guice module class and any exposed interfaces need to made accessible to other modules with the `exports` definition in the module descriptor. The access to these internal classes is even not possible with reflection as tested within the application code on trying to instantiate an object of class `ExampleCoreServiceImpl` located a not exported package of the module `greeting-core`.

At runtime we get following injector inheritance. The `SystemInjector` is not created in this example but would be the choice for providing any system services to be injected in real world scenarios:
![Injector hierarchy](doc/injector-hierarchy.png "Injector hierarchy")

As the following UML diagram of the complete code example shows, the final `application` is only depending on the core plugins `greeting-core` and `database-core` defining the service interfaces, it needs to deal with. There is no compile time nor a runtime dependency needed. All extension plugins are dynamically loaded as `jar` files from a directory by creating a dedicated [ModuleLayer](https://docs.oracle.com/javase/9/docs/api/java/lang/ModuleLayer.html) which even calculated the dependency graph of all plugins automatically.

![UML diagram](doc/java-guice-pluginmodule.png "UML class diagram")

## Compile and run
Just compile and execute the configured application with the provided gradle wrapper on the command line:
```
./gradlew run
```
The application will show nice logging output to the stdout to get some insights of the module mechanism.

## Develop
To import the source code into the [Eclipse IDE](https://www.eclipse.org/eclipseide/) you can create the necessary `.project` and `.classpath` files with 
```
./gradlew eclipse
```
and then `Import/Existing Projects into Workspace` https://www.eclipse.org/eclipseide/https://www.eclipse.org/eclipseide/with `Search for nested projects` option switched `on` within Eclipse.

Please note: the generation of Eclipse files with Gradle is necessary until the [Eclipse Buildship plugin](https://projects.eclipse.org/projects/tools.buildship) supports Java modules (see [Support Jigsaw projects](https://github.com/eclipse/buildship/issues/658)).

To import the source code into [IntelliJ IDEA](http://www.jetbrains.com/idea/) you can create the necessary project `.iml` files with:
```
./gradlew idea
```

## Legacy application
This example project does provide an additional non module application in the `application-legacy` folder. This shows the loading of the plugin modules from the classpath. The required jvm argument `--module-path` has been configured in the applications `build.gradle`:
```
 '--module-path', classpath.asPath,
 '--add-modules', 'ALL-MODULE-PATH'
 ```
 To compile and run the application simply execute:
 ```
./gradlew runLegacy
```
 