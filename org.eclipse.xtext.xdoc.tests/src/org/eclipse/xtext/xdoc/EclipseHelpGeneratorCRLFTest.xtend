package org.eclipse.xtext.xdoc

import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.junit.runner.RunWith
import org.junit.BeforeClass

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XdocInjectorProvider))
class EclipseHelpGeneratorCRLFTest extends EclipseHelpGeneratorTest2 {
	
	@BeforeClass
	def static void setNewLine() {
		System::setProperty("line.separator", "\r\n")
	}
}