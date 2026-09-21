package eu.dunderburken.jshell;

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
