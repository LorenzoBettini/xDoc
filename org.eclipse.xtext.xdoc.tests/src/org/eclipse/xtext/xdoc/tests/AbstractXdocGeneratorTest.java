package org.eclipse.xtext.xdoc.tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderImpl;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.type.impl.java.JavaBeansMetaModel;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.xdoc.XdocInjectorProvider;
import org.eclipse.xtext.xdoc.util.ParserTestUtil;
import org.eclipse.xtext.xdoc.xdoc.AbstractSection;
import org.eclipse.xtext.xdoc.xdoc.Chapter;
import org.eclipse.xtext.xdoc.xdoc.Document;
import org.eclipse.xtext.xdoc.xdoc.TextOrMarkup;
import org.eclipse.xtext.xdoc.xdoc.TextPart;
import org.eclipse.xtext.xdoc.xdoc.XdocFactory;
import org.eclipse.xtext.xdoc.xdoc.XdocFile;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

@RunWith(XtextRunner.class)
@InjectWith(XdocInjectorProvider.class)
public abstract class AbstractXdocGeneratorTest extends org.eclipse.xtext.junit4.AbstractXtextTests {

	protected static final String RESULT_DIR = "test-gen/";
	public static String EXPECTATION_DIR = "expectations/";
	public static String SRC_DIR = "testfiles/";
	@Inject
	protected ParserTestUtil pTest;
	private XpandExecutionContextImpl xpandCtx;

	public AbstractXdocGeneratorTest() {
		super();
		File f = new File(RESULT_DIR);
		if (f.exists()) {
			deleteRecursive(f.listFiles());
		} else {
			f.mkdir();
		}
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.pTest.setUp();

		Output output = new OutputImpl();
		Outlet outlet = new Outlet(RESULT_DIR);
		output.addOutlet(outlet);

		ResourceLoaderFactory.setCurrentThreadResourceLoader(new ResourceLoaderImpl(getClass().getClassLoader()));
		xpandCtx = new XpandExecutionContextImpl(output, null);
		Map<String, Variable> variables = xpandCtx.getGlobalVariables();
		Variable srcDir = new Variable("srcDir", SRC_DIR);
		variables.put("srcDir", srcDir);
		Variable dir = new Variable("dir", RESULT_DIR);
		variables.put("dir", dir);
		xpandCtx.registerMetaModel(new JavaBeansMetaModel());
		ResourceLoaderFactory.setCurrentThreadResourceLoader(null);
	}

	abstract protected void generate(EObject eObject);

	protected void validate(String expected, String result) throws Exception {
		String expectedContents = readFileAsString(expected);
		String actualContents = readFileAsString(result);
		assertEquals(expectedContents, actualContents);
	}

	protected static String readFileAsString(String file) throws IOException {
		return readFileAsString(new File(file));
	}

	protected static String readFileAsString(File file) throws IOException {
		byte[] buffer = new byte[(int) file.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(file));
			f.read(buffer);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		return new String(buffer).replaceAll("\\r", "");
	}

	protected XpandExecutionContext getXpandCtx() {
		return xpandCtx;
	}

	private void deleteRecursive(File... files) {
		for (File file : files) {
			if (file.isDirectory()) {
				deleteRecursive(file.listFiles());
				file.delete();
			}
			file.delete();
		}
	}

	protected Document initDocFromFile(String string, String filename) throws Exception {
		XdocFile file = pTest.getDocFromFile(SRC_DIR + filename);
		AbstractSection mainSection = file.getMainSection();
		if (mainSection instanceof Document) {
			return (Document) mainSection;
		} else if (mainSection instanceof Chapter) {
			Document doc = initDoc(string);
			doc.getChapters().add((Chapter) mainSection);
			return doc;
		}
		return null;
	}

	protected XdocFile initMissingDocFromFile(String string, String filename) throws Exception {
		XdocFile file = pTest.getDocFromFile(SRC_DIR + filename);
		AbstractSection mainSection = file.getMainSection();
		if (mainSection instanceof Chapter) {
			Document doc = initDoc(string);
			doc.getChapters().add((Chapter) mainSection);
			file.setMainSection(doc);
		}
		return file;
	}

	protected Document initDoc(String name) {
		Document result = XdocFactory.eINSTANCE.createDocument();
		TextOrMarkup tomTitle = XdocFactory.eINSTANCE.createTextOrMarkup();
		TextPart title = XdocFactory.eINSTANCE.createTextPart();
		title.setText(name);
		tomTitle.getContents().add(title);
		result.setTitle(tomTitle);
		return result;
	}

	protected Document createDocumentFrom(String mainDocument, String... docs) {
		XtextResourceSet set = get(XtextResourceSet.class);
		Resource ret = set.getResource(URI.createURI(SRC_DIR + mainDocument), true);
		for (String doc : docs) {
			set.getResource(URI.createURI(SRC_DIR + doc), true);
		}
		return (Document) ((XdocFile) getModel((XtextResource) ret)).getMainSection();
	}

	protected Chapter createChapterFrom(String file) {
		XtextResourceSet set = get(XtextResourceSet.class);
		Resource ret = set.getResource(URI.createURI(SRC_DIR + file), true);
		return (Chapter) ((XdocFile) getModel((XtextResource) ret)).getMainSection();
	}

	@Test
	public abstract void testGenCodeWithLanguage() throws Exception;

	@Test
	public abstract void testGenCode() throws Exception;

	@Test
	public abstract void testARef() throws Exception;

	@Test
	public abstract void testCodeRef() throws Exception;

	@Test
	public abstract void testComment() throws Exception;

	@Test
	public abstract void testImg() throws Exception;

	@Test
	public abstract void testLink() throws Exception;

	@Test
	public abstract void testRefText() throws Exception;

	@Test
	public abstract void testNestedList() throws Exception;

	@Test
	public abstract void testSimpleRef() throws Exception;

	@Test
	public abstract void testEscape() throws Exception;

	@Test
	public abstract void testTable() throws Exception;

	@Test
	public abstract void testTwoChapters() throws Exception;

	@Test
	public abstract void testFullHirarchy() throws Exception;
}