package eu.dunderburken;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "jshell", requiresDependencyResolution = ResolutionScope.TEST)
public class JshellMojo extends BaseJShellMojo {

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ProcessBuilder builder = new ProcessBuilder(createArguments())
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
