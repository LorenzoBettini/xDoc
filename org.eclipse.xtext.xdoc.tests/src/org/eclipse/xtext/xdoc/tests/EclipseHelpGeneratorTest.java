package org.eclipse.xtext.xdoc.tests;


import static org.eclipse.xtext.xdoc.util.ParserTestConstants.TEST_FILE_DIR;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.generator.AbstractFileSystemAccess;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xdoc.generator.EclipseHelpGenerator;
import org.eclipse.xtext.xdoc.xdoc.Chapter;
import org.eclipse.xtext.xdoc.xdoc.Document;
import org.eclipse.xtext.xdoc.xdoc.XdocFile;
import org.junit.Test;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EclipseHelpGeneratorTest extends AbstractXdocGeneratorTest {

	private static final String CUSTOM_TEST_FILE_DIR = TEST_FILE_DIR + "eclipsehelp/";

	@Inject
	private Provider<JavaIoFileSystemAccess> javaFSAccessProvider;
	
	@Override
	public void testGenCodeWithLanguage() throws Exception {
		XdocFile file = pTest.getDocFromFile(TEST_FILE_DIR + "codeWithLanguageTest.xdoc");
		Document doc = (Document) file.getMainSection();
		generate(doc);
		validate(EXPECTATION_DIR + "codeWithLanguage.html", RESULT_DIR + "codeWithLanguageTest_1.html");
	}

	@Override
	public void testGenCode() throws Exception {
		XdocFile file = pTest.getDocFromFile(TEST_FILE_DIR + "codeTest.xdoc");
		Document doc = (Document) file.getMainSection();
		generate(doc);
		validate(EXPECTATION_DIR + "codeTest_1.html", RESULT_DIR + "codeTest_1.html");
	}

	@Override
	public void testARef() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "aRefTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "aRefExp.html", RESULT_DIR + "aRefTest_1.html");
	}

	@Override
	public void testCodeRef() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "codeRef.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "codeRef.html", RESULT_DIR + "codeRef_1.html");
	}

	@Override
	public void testComment() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "commentTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "commentTest.html", RESULT_DIR + "commentTest_1.html");
	}

	@Override
	public void testImg() throws Exception {
		XdocFile file = pTest.getDocFromFile(TEST_FILE_DIR + "imgTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "imgTest.html", RESULT_DIR + "imgTest_1.html");
//		validate(EXPECTATION_DIR + "test.png", RESULT_DIR + "test.png");
	}

	@Override
	public void testLink() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "linkTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "linkTest.html", RESULT_DIR + "linkTest_1.html");
	}

	@Override
	public void testRefText() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "namedRefAndTextTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "namedRefTextTest.html", RESULT_DIR + "namedRefAndTextTest_1.html");
	}

	@Override
	public void testNestedList() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "nestedListTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "nestedListTest.html", RESULT_DIR + "nestedListTest_1.html");
	}

	@Override
	public void testSimpleRef() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "simpleRefTest.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "simpleRefTest.html", RESULT_DIR + "simpleRefTest_1.html");
	}

	@Override
	public void testTable() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "table.xdoc");
		generate(file);
		validate(EXPECTATION_DIR + "table.html", RESULT_DIR + "table_1.html");
	}

	@Override
	public void testTwoChapters() throws Exception {
		XtextResourceSet set = get(XtextResourceSet.class);
		set.getResource(URI.createURI(TEST_FILE_DIR + "01-twoChapters.xdoc"), true);
		set.getResource(URI.createURI(TEST_FILE_DIR + "02-twoChapters.xdoc"), true);
		XdocFile file = (XdocFile) getModel((XtextResource)set.getResource(URI.createURI(TEST_FILE_DIR + "twoChaptersDoc.xdoc"), true));
		Document doc = (Document) file.getMainSection();
		for(int i = 0; i < doc.getChapters().size(); i++) {
			Chapter chapter = doc.getChapters().get(i);
			generate(chapter);
		}
		generate(doc);
		validate(EXPECTATION_DIR + "twoChaptersTOC.xml", RESULT_DIR + "toc.xml");
		validate(EXPECTATION_DIR + "twoChapters.xdoc.html", RESULT_DIR + "twoChaptersDoc.html");
		validate(EXPECTATION_DIR + "01-twoChapters.xdoc.html", RESULT_DIR + "01-twoChapters.html");
		validate(EXPECTATION_DIR + "02-twoChapters.xdoc.html", RESULT_DIR + "02-twoChapters.html");
	}

	@Test
	public void testTwoChaptersDirect() throws Exception {
		Document doc = (Document) pTest.getDocFromFile(TEST_FILE_DIR + "twoChapters.xdoc").getMainSection();
		generate(doc);
		validate(EXPECTATION_DIR + "twoChaptersDirectTOC.xml", RESULT_DIR + "toc.xml");
		validate(EXPECTATION_DIR + "twoChaptersDirect.html", RESULT_DIR + "twoChapters.html");
		validate(EXPECTATION_DIR + "twoChaptersDirect_1.html", RESULT_DIR + "twoChapters_1.html");
		validate(EXPECTATION_DIR + "twoChaptersDirect_2.html", RESULT_DIR + "twoChapters_2.html");
	}

	@Override
	public void testFullHirarchy () throws Exception {
		XdocFile file = pTest.getDocFromFile(TEST_FILE_DIR + "downToSection4Test.xdoc");
		// gen toc.xml
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "fullHirarchyTOC.xml", RESULT_DIR + "toc.xml");
		validate(EXPECTATION_DIR + "fullHirarchy.xdoc.html", RESULT_DIR + "downToSection4Test_1.html");
	}

	@Override
	public void testEscape() throws Exception {
		XdocFile file = pTest.getDocFromFile(CUSTOM_TEST_FILE_DIR + "testEscape.xdoc");
		generate(file.getMainSection());
		validate(EXPECTATION_DIR + "escapeTest.html", RESULT_DIR + "testEscape_1.html");
	}

	@Inject
	private EclipseHelpGenerator generator;

	@Override
	protected void generate(EObject obj) {
		AbstractFileSystemAccess fsa = javaFSAccessProvider.get();
		fsa.setOutputPath(System.getProperty("user.dir") + File.separatorChar+"test-gen"+ File.separatorChar);
		generator.doGenerate(obj.eResource(), fsa);
	}
}
