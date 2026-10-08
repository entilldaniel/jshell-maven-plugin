package eu.dunderburken;

import static org.assertj.core.api.Assertions.assertThatCollection;

import java.io.File;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ArgumentBuilderTest {

	@Test
	void should_succeed_on_minimal_arguments() {
		MavenProject project = Mockito.mock(MavenProject.class);
		Mockito.when(project.getBasedir())
				.thenReturn(new File("/foo/bar"));
		ArgumentBuilder ab = new ArgumentBuilder(project,
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				"NORMAL",
				false,
				false,
				true);
		assertThatCollection(ab.build()).contains("jshell", "--feedback", "normal");
	}

	@Test
	void should_render_script_path_correctly() {
		MavenProject project = Mockito.mock(MavenProject.class);
		Mockito.when(project.getBasedir())
				.thenReturn(new File("/a/b"));
		ArgumentBuilder ab = new ArgumentBuilder(project,
				List.of("/c/d.jshell"),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				List.of(),
				"NORMAL",
				false,
				false,
				true);
		assertThatCollection(ab.build()).contains("/a/b/c/d.jshell");
	}

}
