package eu.dunderburken;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

public class ArgumentBuilder {

	private final MavenProject project;
	private final List<String> files;
	private final FeedbackLevel feedbackLevel;
	private final Boolean preview;

	public ArgumentBuilder(MavenProject project,
			List<String> files,
			String feedbackLevel,
			Boolean preview) {
		this.project = project;
		this.files = files;
		this.feedbackLevel = FeedbackLevel.parse(feedbackLevel);
		this.preview = preview;
	}

	public List<String> build() {
		List<String> arguments = new ArrayList<>();

		arguments.add("jshell");
		if (preview) {
			arguments.add("--enable-preview");
		}

		arguments.addAll(projectClassPath());
		arguments.add("--feedback");
		arguments.add(feedbackLevel.name().toLowerCase());
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
