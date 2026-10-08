package eu.dunderburken;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

/**
 * Responsible for building the list of arguments to pass on to the
 * ProcessBuilder
 * so that JShell can be started.
 **/
public class ArgumentBuilder {

	private final MavenProject project;
	private final List<String> files;
	private final List<String> compilerFlags;
	private final List<String> runtimeFlags;
	private final List<String> remoteFlags;
	private final List<String> modulePaths;
	private final List<String> modules;
	private final List<String> classPaths;
	private final String feedbackLevel;
	private final Boolean preview;
	private final Boolean noStartup;
	private final Boolean includeTest;

	private Function<String, Boolean> isPreset = (name) -> Set.of("PRINTING", "JAVASE", "DEFAULT").contains(name);

	/**
	 * Constructor
	 *
	 * @param project       The maven representation of this project
	 * @param files         Scripts and/or defaults for JShell such as; JAVASE,
	 *                      PRINTING and DEFAULT. Paths are realtive to the project
	 *                      root.
	 * @param compiler      Compiler flags to pass to jshell
	 * @param runtime       Runtime flags to pass to jshell
	 * @param remote        Runtime flags to pass to jshell
	 * @param modulePaths   Where the jvm can look for modules
	 * @param modules       Which modules to load
	 * @param classPaths    Extra classpaths except the ones from the project
	 * @param feedbackLevel The desired level of feedback.
	 * @param preview       Enable preview features
	 * @param noStartup     If true the flag --no-startup will be added only adding
	 *                      the scripts defined in files
	 * @param includeTest   Include the test class path also?
	 */
	public ArgumentBuilder(MavenProject project,
			List<String> files,
			List<String> compiler,
			List<String> runtime,
			List<String> remote,
			List<String> modulePaths,
			List<String> modules,
			List<String> classPaths,
			String feedbackLevel,
			Boolean preview,
			Boolean noStartup,
			Boolean includeTest) {
		this.project = project;
		this.files = files.stream()
				.map(f -> isPreset.apply(f) ? f : project.getBasedir().toPath().resolve(f))
				.map(Object::toString)
				.collect(Collectors.toList());
		this.modulePaths = modulePaths;
		this.modules = modules;
		this.classPaths = classPaths;
		this.compilerFlags = flags(compiler, "-C%s");
		this.runtimeFlags = flags(runtime, "-J%s");
		this.remoteFlags = flags(remote, "-R%s");

		this.feedbackLevel = FeedbackLevel.parse(feedbackLevel);
		this.preview = preview;
		this.noStartup = noStartup;
		this.includeTest = includeTest;
	}

	/**
	 * Build the list of arguments to pass to JShell.
	 **/
	public List<String> build() {
		List<String> arguments = new ArrayList<>();

		arguments.add("jshell");
		if (preview) {
			arguments.add("--enable-preview");
		}
		if (noStartup) {
			arguments.add("--no-startup");
		}
		arguments.addAll(classPathFlag(classPaths, includeTest));
		arguments.addAll(modulePathFlag(modulePaths));
		arguments.addAll(addModulesFlag(modules));
		arguments.add("--feedback");
		arguments.add(feedbackLevel);
		arguments.addAll(compilerFlags);
		arguments.addAll(runtimeFlags);
		arguments.addAll(remoteFlags);
		arguments.addAll(files);

		return arguments;
	}

	private List<String> flags(List<String> argumentFlags, String template) {
		return argumentFlags.stream()
				.map(s -> String.format(template, s))
				.collect(Collectors.toList());
	}

	private List<String> modulePathFlag(List<String> paths) {
		if (paths.isEmpty()) {
			return Collections.emptyList();
		}

		return List.of(
				"--module-path",
				paths.stream().collect(Collectors.joining(File.pathSeparator)));
	}

	private List<String> addModulesFlag(List<String> modules) {
		if (modules.isEmpty()) {
			return Collections.emptyList();
		}

		return List.of(
				"--add-modules",
				modules.stream().collect(Collectors.joining(",")));
	}

	private List<String> classPathFlag(List<String> extras, boolean includeTests) {
		try {
			List<String> tests = includeTests ? project.getTestClasspathElements() : Collections.emptyList();
			tests.removeAll(project.getCompileClasspathElements());

			String cp = Stream.of(project.getCompileClasspathElements(), extras, tests)
					.flatMap(List::stream)
					.collect(Collectors.joining(File.pathSeparator));

			return List.of("--class-path", cp);
		} catch (DependencyResolutionRequiredException e) {
			System.err.println("Dependency resolution failed, no class paths loaded.");
			System.err.println(e.getMessage());

			return Collections.emptyList();
		}
	}

}
