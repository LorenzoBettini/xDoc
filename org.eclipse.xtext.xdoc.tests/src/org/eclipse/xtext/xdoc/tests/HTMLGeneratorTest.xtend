package org.eclipse.xtext.xdoc.tests

import static org.eclipse.xtext.xdoc.util.ParserTestConstants.TEST_FILE_DIR
import java.io.File
import java.net.URLDecoder
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.JavaIoFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.xdoc.XdocInjectorProvider
import org.eclipse.xtext.xdoc.generator.AbstractSectionExtension
import org.eclipse.xtext.xdoc.generator.HtmlGenerator
import org.eclipse.xtext.xdoc.generator.Outlets
import org.eclipse.xtext.xdoc.generator.util.HTMLNamingExtensions
import org.eclipse.xtext.xdoc.xdoc.AbstractSection
import org.eclipse.xtext.xdoc.xdoc.Chapter
import org.eclipse.xtext.xdoc.xdoc.Document
import org.eclipse.xtext.xdoc.xdoc.Emphasize
import org.eclipse.xtext.xdoc.xdoc.Identifiable
import org.eclipse.xtext.xdoc.xdoc.Section
import org.eclipse.xtext.xdoc.xdoc.TextOrMarkup
import org.eclipse.xtext.xdoc.xdoc.TextPart
import org.eclipse.xtext.xdoc.xdoc.XdocFactory
import org.eclipse.xtext.xdoc.xdoc.XdocFile
import org.junit.Test
import com.google.inject.Inject

@InjectWith(XdocInjectorProvider) class HTMLGeneratorTest extends AbstractXdocGeneratorTest {
	public static String HTML_SRC = '''html«File.separator»'''
	@Inject HtmlGenerator generator
	@Inject HTMLNamingExtensions naming
	@Inject AbstractSectionExtension utils
	@Inject package JavaIoFileSystemAccess fsa

	def protected void generate(Document obj) throws Exception {
		fsa.setOutputPath(Outlets.WEB_SITE, Outlets.WEB_SITE_PATH_NAME)
		fsa.
			setOutputPath('''«System.getProperty("user.dir")»«File.separatorChar»test-gen«File.separatorChar»html«File.separatorChar»''')
		generator.generate(obj, fsa)
	}

	@Test def void testGenerationAllFilesFullHirarchy() throws Exception {
		var String resName = "downToSection4Test"
		var Document doc = createDocumentFrom('''«HTML_SRC»«resName».xdoc''')
		generate(doc)
		assertGenerated(doc)
		for (AbstractSection ^as : doc.getChapters()) {
			generate(^as)
			assertGenerated(^as)
			for (AbstractSection next : utils.sections(^as)) {
				generate(next)
				assertGenerated(next)
			}

		}

	}

	// public void testHeader() throws Exception {
	// Document doc = initDoc("foo");
	// String expected = "<head>\n" +
	// "  <META http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n"
	// +
	// "  <title>foo</title>\n" +
	// "  <link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
	// "  <link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
	// "</head>\n";
	// String actual = generator.header(doc.getTitle()).toString();
	// assertEquals(expected, actual);
	// }
	// public void testEmptyBody() throws Exception {
	// Document doc = initDoc("foo");
	// String expected = "<body>\n" +
	// "</body>\n";
	// String actual = generator.body(doc).toString();
	// assertEquals(expected, actual);
	// }
	@Test def void testToc() throws Exception {
		var Document doc = createDocumentFrom('''«HTML_SRC»downToSection4Test.xdoc''')
		var String expected = '''<div class="toc">
  <ul>
    <li><a href="downToSection4Test_1.php" >bar</a>
    <ul>
      <li><a href="downToSection4Test.php#foo_1_1.php" >foo</a></li>
      <li><a href="downToSection4Test.php#foo_1_2.php" >atom</a></li>
    </ul>
    </li>
  </ul>
</div>
'''
		var String actual = generator.toc(doc).toString()
		assertEquals(expected, actual)
	}

	@Test def void testEm() throws Exception {
		var XdocFactory fac = XdocFactory.eINSTANCE
		var Emphasize emphasize = fac.createEmphasize()
		var TextOrMarkup textOrMarkup = fac.createTextOrMarkup()
		var TextPart textPart = fac.createTextPart()
		textPart.setText("Testtext_")
		textOrMarkup.getContents().add(textPart)
		emphasize.getContents().add(textOrMarkup)
		var String expected = "<em>Testtext_</em>"
		var String actual = generator.genText(emphasize).toString()
		assertEquals(expected, actual) // and for two paragraphs in one em
		var TextOrMarkup textOrMarkup2 = fac.createTextOrMarkup()
		var TextPart textPart2 = fac.createTextPart()
		textPart2.setText("more test")
		textOrMarkup2.getContents().add(textPart2)
		emphasize.getContents().add(textOrMarkup2)
		expected = '''<em><p>
Testtext_
</p>
<p>
more test
</p>
</em>'''
		actual = generator.genText(emphasize).toString()
		assertEquals(expected, actual)
	}

	override void testGenCodeWithLanguage() throws Exception {
		var Document document = createDocumentFrom("codeWithLanguageTest.xdoc")
		generate(document)
		assertGenerated(document)
		generate(document.getChapters().get(0))
		assertGenerated(document.getChapters().get(0))
		validate("codeWithLanguageTest.html", name(document.getChapters().get(0)))
	}

	override void testGenCode() throws Exception {
		var Document document = createDocumentFrom("codeTest.xdoc")
		generate(document)
		assertGenerated(document)
		generate(document.getChapters().get(0))
		assertGenerated(document.getChapters().get(0))
		validate("codeTest.html", name(document.getChapters().get(0)))
	}

	@SuppressWarnings("deprecation") def private String name(Identifiable id) {
		return URLDecoder.decode(naming.getResourceURL(id))
	}

	override void testARef() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»aRefTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("aRefTest.html", name(file.getMainSection()))
	}

	override void testCodeRef() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»codeRef.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("codeRefTest.html", name(file.getMainSection()))
	}

	override void testComment() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»commentTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("commentTest.html", name(file.getMainSection()))
	}

	override void testLink() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»linkTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("linkTest.html", name(file.getMainSection()))
	}

	override void testRefText() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»namedRefAndTextTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("namedRefAndTextTest.html", name(file.getMainSection()))
	}

	override void testNestedList() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»nestedListTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("nestedListTest.html", name(file.getMainSection()))
	}

	override void testSimpleRef() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»simpleRefTest.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("simpleRefTest.html", name(file.getMainSection()))
	}

	override void testTable() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»table.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("table.html", name(file.getMainSection()))
	}

	override void testTwoChapters() throws Exception {
		var XtextResourceSet set = get(XtextResourceSet)
		var Resource res = set.getResource(URI.createURI('''«TEST_FILE_DIR»01-twoChapters.xdoc'''), true)
		var Chapter chapter0 = (getModel(res as XtextResource) as XdocFile).getMainSection() as Chapter
		res = set.getResource(URI.createURI('''«TEST_FILE_DIR»02-twoChapters.xdoc'''), true)
		var Chapter chapter1 = (getModel(res as XtextResource) as XdocFile).getMainSection() as Chapter
		var XdocFile file = getModel(
			set.getResource(URI.createURI('''«TEST_FILE_DIR»twoChaptersDoc.xdoc'''), true) as XtextResource) as XdocFile
		var Document doc = file.getMainSection() as Document

		for (var int i = 0; i < doc.getChapters().size(); i++) {
			var Chapter chapter = doc.getChapters().get(i)
			generate(chapter)
		}
		generate(doc)
		validate("01-twoChapters.html", name(chapter0))
		validate("02-twoChapters.html", name(chapter1))
		validate("twoChaptersDoc.html", name(doc))
	}

	override void testImg() throws Exception {
		assertTrue("Implement", false)
	}

	override void testEscape() throws Exception {
		var XdocFile file = pTest.getDocFromFile('''«TEST_FILE_DIR»testEscape.xdoc''')
		generate(file.getMainSection())
		assertGenerated(file.getMainSection())
		validate("testEscape.html", name(file.getMainSection()))
	}

	override protected Document initDocFromFile(String string, String filename) throws Exception {
		var Document doc = super.initDocFromFile(string, HTML_SRC + filename)
		var URI uri = doc.eResource().getURI()
		var URI appendSegment = uri.trimSegments(1).appendSegment('''«string».xdoc''')
		doc.eResource().setURI(appendSegment)
		return doc
	}

	override protected void generate(EObject eObject) {
		// TODO Auto-generated method stub
	}

	override void testFullHirarchy() throws Exception {
		// TODO Auto-generated method stub
	}

	def Chapter getChapter(String title) {
		var Chapter chapter = XdocFactory.eINSTANCE.createChapter()
		var TextPart tp = XdocFactory.eINSTANCE.createTextPart()
		tp.setText(title)
		var TextOrMarkup tom = XdocFactory.eINSTANCE.createTextOrMarkup()
		tom.getContents().add(tp)
		chapter.setTitle(tom)
		return chapter
	}

	def Section getSection(String title) {
		var Section section = XdocFactory.eINSTANCE.createSection()
		var TextPart tp = XdocFactory.eINSTANCE.createTextPart()
		tp.setText(title)
		var TextOrMarkup tom = XdocFactory.eINSTANCE.createTextOrMarkup()
		tom.getContents().add(tp)
		section.setTitle(tom)
		return section
	}

	def void assertGenerated(AbstractSection doc) {
		var File file = new File(RESULT_DIR + HTML_SRC + name(doc))
		var boolean fileGenerated = file.exists()
		assertTrue('''«name(doc)» not generated''', fileGenerated)
	}

	override protected void validate(String expected, String result) throws Exception {
		super.validate(EXPECTATION_DIR + HTML_SRC + expected, RESULT_DIR + HTML_SRC + result)
	}

}
