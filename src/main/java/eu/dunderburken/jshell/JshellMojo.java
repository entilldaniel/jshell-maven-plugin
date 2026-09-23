package eu.dunderburken.jshell;

import java.io.IOException;
import java.util.List;

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

	@Parameter(property = "loadFiles", required = false)
	private List<String> loadFiles;

	@Parameter(property = "compilerFlags", required = false)
	private List<String> compilerFlags;

	@Parameter(property = "runtimeFlags", required = false)
	private List<String> runtimeFlags;

	@Parameter(property = "feedback", required = false, defaultValue = "normal")
	private String feedback;

	@Parameter(property = "enablePreview", required = false, defaultValue = "false")
	private boolean enablePreview;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ArgumentBuilder ab = new ArgumentBuilder(project, loadFiles, compilerFlags, runtimeFlags, feedback,
				enablePreview);

		ProcessBuilder builder = new ProcessBuilder(ab.build())
				.inheritIO();

		try {
			Process p = builder.start();
			p.waitFor();
		} catch (IOException e) {
			getLog().error("Could not start JShell", e);
		} catch (InterruptedException e) {
			getLog().error("Could not wait for process to finish.");
		}
	}

}
