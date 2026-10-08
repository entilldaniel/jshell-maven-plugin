package eu.dunderburken;

import java.util.stream.Collectors;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "command", requiresDependencyResolution = ResolutionScope.COMPILE)
public class CommandMojo extends BaseJShellMojo {

	@Override
	public void execute() {
		getLog().info("Command is:");
		String command = createArguments()
				.stream()
				.collect(Collectors.joining(" "));

		getLog().info(command);
	}

}
