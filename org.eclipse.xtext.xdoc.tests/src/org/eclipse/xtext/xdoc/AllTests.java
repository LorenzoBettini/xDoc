package org.eclipse.xtext.xdoc;

import org.eclipse.xtext.xdoc.tests.EclipseHelpGeneratorCRLFTest;
import org.eclipse.xtext.xdoc.tests.EclipseHelpGeneratorCompilationTest;
import org.eclipse.xtext.xdoc.tests.EclipseHelpGeneratorTest;
import org.eclipse.xtext.xdoc.tests.LatexGeneratorTest;
import org.eclipse.xtext.xdoc.tests.LexerTest;
import org.eclipse.xtext.xdoc.tests.ParserTest;
import org.eclipse.xtext.xdoc.tests.UtilityTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ParserTest.class,
	LexerTest.class,
	UtilityTest.class,
	EclipseHelpGeneratorCompilationTest.class,
	EclipseHelpGeneratorCRLFTest.class,
	LatexGeneratorTest.class,
	EclipseHelpGeneratorTest.class
})
public class AllTests {

}