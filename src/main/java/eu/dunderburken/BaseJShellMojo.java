package eu.dunderburken;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class BaseJShellMojo extends AbstractMojo {

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
	 * Flags to pass to the remote runtime
	 **/
	@Parameter(property = "remoteFlags", required = false)
	private List<String> remoteFlags;

	/**
	 * Module paths, where to find application modules.
	 **/
	@Parameter(property = "modulePaths", required = false)
	private List<String> modulePaths;

	/**
	 * Modules, root modules to resolve in addition to the initial module.
	 **/
	@Parameter(property = "modules", required = false)
	private List<String> modules;

	/**
	 * Class Path, extra classpaths other than the ones defined by
	 * the dependencies of this project.
	 **/
	@Parameter(property = "classPaths", required = false)
	private List<String> classPaths;

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

	/**
	 * Should preview features be enabled?
	 **/
	@Parameter(property = "noStartup", required = false, defaultValue = "false")
	private boolean noStartup;

	List<String> createArguments() {
		return new ArgumentBuilder(project,
								   loadFiles,
								   compilerFlags,
								   runtimeFlags,
								   remoteFlags,
								   modulePaths,
								   modules,
								   classPaths,
								   feedback,
								   enablePreview,
								   noStartup).build();	
	}
	
}
