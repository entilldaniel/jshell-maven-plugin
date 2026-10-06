# JShell Maven Plugin

This plugin enables you to run [JShell](https://docs.oracle.com/en/java/javase/11/tools/jshell.html) or alternatively export the command to run jshell for your project with the correct classpath.  

A bare bones configuration would look like this:
```xml
<plugin>
	<groupId>eu.dunderburken</groupId>
	<artifactId>jshell-maven-plugin</artifactId>
	<version>1.0.1</version>
</plugin>
```

Running `mvn eu.dunderburken.jshell-maven-plugin:jshell` will start a JShell instance with the project classpath.  


## Configuration
All the following is part of the plugin configuration and thus ends up within a `configuration` tag.

### Loading files
If you want to add scripts they are relative to the root of the project.

The built in alternatives are:
 - DEFAULT
 - JAVASE
 - PRINTING

```xml
<loadFiles>
	<item>JAVASE</item>
	<item>PRINTING</item>
	<item>scripts/init.jshell</item>
</loadFiles>

```
### Preview features
Is a boolean that defaults to false. To enable it:
```xml
<enablePreview>true</enablePreview>
```


### Verbosity
JShell default verbosity is `normal` and the alternatives are:
 - verbose
 - normal
 - concise
 - silent
 - custom
It is set like this:
```xml
<enablePreview>true</enablePreview>
```

### Compiler Flags
```xml
<compilerFlags>
	<flag>-g:source</flag>
</compilerFlags>
```

### Runtime Flags
```xml
<runtimeFlags>
	<flag>-Xms128m</flag>
</runtimeFlags>
```
