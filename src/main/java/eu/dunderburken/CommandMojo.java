package eu.dunderburken;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "command", requiresDependencyResolution = ResolutionScope.COMPILE)
public class CommandMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
	 * paths to jshell scripts in the project relative to the project root and
	 * default
	 * files that come bundled with jshell (PRINTING, JAVASE and DEFAULT)
	 **/
	@Parameter(property = "loadFiles", required = false)
	private List<String> loadFiles;

	/**
	 * Flags to pass to the compiler.
	 **/
	@Parameter(property = "compilerFlags", required = false)
	private List<String> compilerFlags;

	/**
	 * Flags to pass to the JVM
	 **/
	@Parameter(property = "runtimeFlags", required = false)
	private List<String> runtimeFlags;

	/**
	 * Feedback in response to what is entered, normal is default.
	 * Available values are: verbose, normal, concise, silent and custom.
	 **/
	@Parameter(property = "feedback", required = false, defaultValue = "normal")
	private String feedback;

	/**
	 * Should preview features be enabled?
	 **/
	@Parameter(property = "enablePreview", required = false, defaultValue = "false")
	private boolean enablePreview;

	@Override
	public void execute() {
		ArgumentBuilder ab = new ArgumentBuilder(project, loadFiles, compilerFlags, runtimeFlags, feedback,
				enablePreview);

		getLog().info("Argument is:");
		String command = ab.build()
				.stream()
				.collect(Collectors.joining(" "));

		getLog().info(command);
	}

}
