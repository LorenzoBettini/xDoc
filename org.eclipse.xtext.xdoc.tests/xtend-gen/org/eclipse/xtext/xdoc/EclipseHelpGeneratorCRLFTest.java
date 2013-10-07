package org.eclipse.xtext.xdoc;

import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.xdoc.EclipseHelpGeneratorTest2;
import org.eclipse.xtext.xdoc.XdocInjectorProvider;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(XdocInjectorProvider.class)
@SuppressWarnings("all")
public class EclipseHelpGeneratorCRLFTest extends EclipseHelpGeneratorTest2 {
  @BeforeClass
  public static void setNewLine() {
    System.setProperty("line.separator", "\r\n");
  }
}
