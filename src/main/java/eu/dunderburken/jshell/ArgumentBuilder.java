package eu.dunderburken.jshell;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	private final String feedbackLevel;
	private final Boolean preview;

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
	 * @param feedbackLevel The desired level of feedback.
	 * @param preview       Enable preview features
	 */
	public ArgumentBuilder(MavenProject project,
			List<String> files,
			List<String> compiler,
			List<String> runtime,
			String feedbackLevel,
			Boolean preview) {
		this.project = project;
		this.files = files.stream()
				.map(f -> isPreset.apply(f) ? f : project.getBasedir().toPath().resolve(f).toString()).toList();
		this.compilerFlags = compiler.stream().map(s -> "-C%s".formatted(s)).toList();
		this.runtimeFlags = runtime.stream().map(s -> "-J%s".formatted(s)).toList();
		this.feedbackLevel = FeedbackLevel.parse(feedbackLevel);
		this.preview = preview;
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
		arguments.addAll(projectClassPath());
		arguments.add("--feedback");
		arguments.add(feedbackLevel);
		arguments.addAll(compilerFlags);
		arguments.addAll(runtimeFlags);
		arguments.addAll(files);

		return arguments;
	}

	private List<String> projectClassPath() {
		try {
			String cp = project.getCompileClasspathElements()
					.stream()
					.collect(Collectors.joining(File.pathSeparator));

			return List.of("--class-path", cp);
		} catch (DependencyResolutionRequiredException e) {
			// getLog().warn("Could not fetch project class path.", e);
			return Collections.emptyList();
		}
	}

}
