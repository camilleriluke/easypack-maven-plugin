package com.github.easypack.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Test;

import com.github.easypack.assertions.AssertPackaging;
import com.github.easypack.builder.MavenProjectBuilder;
import com.github.easypack.builder.MojoBuilder;

/**
 * Unit test for {@link TarGzPackagerMojo}.
 * 
 * @author agusmunioz
 * 
 */
public class TarGzPackagerMojoTest {

	/**
	 * Test a successful packagin of a project.
	 */
	@Test
	public void packaging() {

		MavenProject project = MavenProjectBuilder.tmpProject();

		TarGzPackagerMojo mojo = (TarGzPackagerMojo) MojoBuilder
				.build(new TarGzPackagerMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("finalName", project.getBuild().getFinalName()).get();

		try {

			mojo.execute();

			AssertPackaging.assertPackaged(project, "GZip");

		} catch (MojoExecutionException | MojoFailureException e) {
			Assert.fail("Unexpected exception " + e);
		}
	}

	/**
	 * Test the packaging fails when a not existing file is configured to be
	 * packaged.
	 * 
	 * @throws MojoExecutionException
	 *             expected.
	 */
	@Test(expected = MojoExecutionException.class)
	public void fileNotFound() throws MojoExecutionException {

		MavenProject project = MavenProjectBuilder.tmpProject();

		TarGzPackagerMojo mojo = (TarGzPackagerMojo) MojoBuilder
				.build(new TarGzPackagerMojo())
				.with(project)
				.with("outputDirectory",
						project.getBuild().getOutputDirectory())
				.with("finalName", project.getBuild().getFinalName())
				.with("includes", new File[] { new File("not-existing") })
				.get();

		try {

			mojo.execute();

			AssertPackaging.assertPackaged(project, "GZip");

		} catch (MojoFailureException e) {
			Assert.fail("Unexpected exception " + e);
		}
	}
}
