package org.eclipse.xtext.xdoc.tests

import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.xdoc.util.XdocInjectorProviderCustom
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.AfterClass

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XdocInjectorProviderCustom))
class EclipseHelpGeneratorCRLFTest extends EclipseHelpGeneratorCompilationTest {

	var static String originalLineSeparator

	@BeforeClass
	def static void setNewLine() {
		originalLineSeparator = System.setProperty("line.separator", "\r\n")
	}

	@AfterClass
	def static void resetNewLine() {
		System.setProperty("line.separator", originalLineSeparator)
	}
}