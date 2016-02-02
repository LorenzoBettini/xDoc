package org.eclipse.xtext.xdoc.tests.generator.util

import java.net.URL
import org.eclipse.xtext.xdoc.generator.util.GitExtensions
import org.junit.Test

import static org.junit.Assert.*

class GitExtensionsTest {

	@Test
	def void testCalculateTraceFileName() {
		val clazzName = "org/xtext/builddsl/lib/DependsOn.class"
		val url = new URL("jar:file:/plugins/org.xtext.builddsl.lib.jar!/org/xtext/builddsl/lib/DependsOn.class");
		val gitURL = new GitExtensions().calculateTraceFileName(url, clazzName, "org.xtext.builddsl.lib.DependsOn")
		assertNotNull(gitURL)
		assertEquals("jar:file:/plugins/org.xtext.builddsl.lib.jar!/org/xtext/builddsl/lib/.DependsOn.java._trace",
			gitURL.toString)
	}

	@Test
	def void testFindOriginalSource() {
		val gitURL = new GitExtensions().findOriginalSource(GitExtensionsTest.name)
		assertNotNull(gitURL)
		assertEquals("org/eclipse/xtext/xdoc/tests/generator/util/GitExtensionsTest.xtend", gitURL.toString)
	}

	@Test
	def void testFindOriginalSourceFromJar() {
		val gitURL = new GitExtensions().findOriginalSource("org.xtext.mongobeans.lib.IMongoBean")
		assertNotNull(gitURL)
		assertEquals("org/xtext/mongobeans/lib/IMongoBean.xtend", gitURL)
	}
}