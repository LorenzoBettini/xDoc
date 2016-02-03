/**
 * 
 */
package org.eclipse.xtext.xdoc.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.xdoc.XdocStandaloneSetup;
import org.eclipse.xtext.xdoc.xdoc.XdocFile;

/**
 * @author Lorenzo Bettini
 *
 */
public class ParserTestUtil extends org.eclipse.xtext.junit4.AbstractXtextTests {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		with(new XdocStandaloneSetup());
	}

	public XdocFile getDoc(String string) throws Exception {
		return (XdocFile) doGetResource(new StringInputStream(string), URI.createFileURI("mytestmodel.xdoc"))
				.getContents().get(0);
	}

	public XdocFile getDocFromFile(String fileName) throws FileNotFoundException, Exception {
		return (XdocFile) doGetResource(new FileInputStream(fileName), URI.createFileURI(fileName)).getContents()
				.get(0);
	}
}
