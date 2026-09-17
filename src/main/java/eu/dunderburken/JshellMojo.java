package eu.dunderburken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "jshell", requiresDependencyResolution = ResolutionScope.COMPILE)
public class JshellMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Starting JShell...");

		ProcessBuilder builder = new ProcessBuilder(buildArguments())
				.inheritIO();

		try (Process p = builder.start()) {
			// Do nothing, JShell is now running... Our work is done.
		} catch (IOException e) {
			getLog().error("Could not start JShell", e);
		}
	}

	private List<String> buildArguments() {
		List<String> arguments = new ArrayList<>();

		arguments.add("jshell");
		arguments.add("--class-path");
		arguments.add(projectClassPath());

		return arguments;
	}

	/**
	 * The delimiter only for *nix systems and mac os,
	 * for windows the separator should be ;
	 **/
	private String projectClassPath() {
		try {
			return project.getCompileClasspathElements()
					.stream()
					.collect(Collectors.joining(":"));
		} catch (DependencyResolutionRequiredException e) {
			getLog().warn("Could not fetch project class path.", e);
			return "";
		}
	}
}
