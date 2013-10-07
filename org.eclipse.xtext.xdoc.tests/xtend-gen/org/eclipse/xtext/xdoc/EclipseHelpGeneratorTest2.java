package org.eclipse.xtext.xdoc;

import com.google.inject.Inject;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.util.IAcceptor;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper;
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper.Result;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xdoc.XdocInjectorProvider;
import org.eclipse.xtext.xdoc.generator.util.Utils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(XdocInjectorProvider.class)
@SuppressWarnings("all")
public class EclipseHelpGeneratorTest2 {
  @Inject
  @Extension
  private CompilationTestHelper _compilationTestHelper;
  
  @Inject
  @Extension
  private Utils _utils;
  
  @Test
  public void testFormattedCode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("document[Main]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("chapter[Dummy Title]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("code[");
    _builder.newLine();
    _builder.append("class Foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void main(String\\[\\] args){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("System.out.println(\"Hello World\\n\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("<html>");
    _builder_1.newLine();
    _builder_1.append("<head>");
    _builder_1.newLine();
    _builder_1.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >");
    _builder_1.newLine();
    _builder_1.append("<title>Dummy Title</title>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("<link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link rel=\"home\" href=\"MyFile.html\" title=\"\">");
    _builder_1.newLine();
    _builder_1.append("</head>");
    _builder_1.newLine();
    _builder_1.append("<body>");
    _builder_1.newLine();
    _builder_1.append("<a name=\"Main_1\"></a>");
    _builder_1.newLine();
    _builder_1.append("<h1>Dummy Title</h1>");
    _builder_1.newLine();
    _builder_1.append("<p>");
    _builder_1.newLine();
    _builder_1.append("<div class=\"literallayout\">");
    _builder_1.newLine();
    _builder_1.append("<div class=\"incode\">");
    _builder_1.newLine();
    _builder_1.append("<p class=\"code\">");
    _builder_1.newLine();
    _builder_1.append("class&nbsp;Foo&nbsp;{<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;static&nbsp;void&nbsp;main(String[]&nbsp;args){<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<span class=\"string\">\"Hello&nbsp;World\\n\"</span>);<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;}<br/>");
    _builder_1.newLine();
    _builder_1.append("}<br/>");
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</body>");
    _builder_1.newLine();
    _builder_1.append("</html>");
    _builder_1.newLine();
    this.assertGeneratedHtml(_builder, _builder_1);
  }
  
  @Test
  public void testFormattedCode_02() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("document[Main]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("chapter[Dummy Title]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("code[");
    _builder.newLine();
    _builder.newLine();
    _builder.append("class Foo {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void main(String\\[\\] args){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("System.out.println(\"Hello World\\n\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("<html>");
    _builder_1.newLine();
    _builder_1.append("<head>");
    _builder_1.newLine();
    _builder_1.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >");
    _builder_1.newLine();
    _builder_1.append("<title>Dummy Title</title>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("<link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link rel=\"home\" href=\"MyFile.html\" title=\"\">");
    _builder_1.newLine();
    _builder_1.append("</head>");
    _builder_1.newLine();
    _builder_1.append("<body>");
    _builder_1.newLine();
    _builder_1.append("<a name=\"Main_1\"></a>");
    _builder_1.newLine();
    _builder_1.append("<h1>Dummy Title</h1>");
    _builder_1.newLine();
    _builder_1.append("<p>");
    _builder_1.newLine();
    _builder_1.append("<div class=\"literallayout\">");
    _builder_1.newLine();
    _builder_1.append("<div class=\"incode\">");
    _builder_1.newLine();
    _builder_1.append("<p class=\"code\">");
    _builder_1.newLine();
    _builder_1.append("class&nbsp;Foo&nbsp;{<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;static&nbsp;void&nbsp;main(String[]&nbsp;args){<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<span class=\"string\">\"Hello&nbsp;World\\n\"</span>);<br/>");
    _builder_1.newLine();
    _builder_1.append("&nbsp;&nbsp;&nbsp;&nbsp;}<br/>");
    _builder_1.newLine();
    _builder_1.append("}<br/>");
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</body>");
    _builder_1.newLine();
    _builder_1.append("</html>");
    _builder_1.newLine();
    this.assertGeneratedHtml(_builder, _builder_1);
  }
  
  @Test
  public void testFormattedCode_03() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("document[Main]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("chapter[Dummy Title]");
    _builder.newLine();
    _builder.newLine();
    _builder.append("code[");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void foo(String\\[\\] args);");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void bar(String\\[\\] args);");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("<html>");
    _builder_1.newLine();
    _builder_1.append("<head>");
    _builder_1.newLine();
    _builder_1.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >");
    _builder_1.newLine();
    _builder_1.append("<title>Dummy Title</title>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("<link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder_1.newLine();
    _builder_1.append("<link rel=\"home\" href=\"MyFile.html\" title=\"\">");
    _builder_1.newLine();
    _builder_1.append("</head>");
    _builder_1.newLine();
    _builder_1.append("<body>");
    _builder_1.newLine();
    _builder_1.append("<a name=\"Main_1\"></a>");
    _builder_1.newLine();
    _builder_1.append("<h1>Dummy Title</h1>");
    _builder_1.newLine();
    _builder_1.append("<p>");
    _builder_1.newLine();
    _builder_1.append("<div class=\"literallayout\">");
    _builder_1.newLine();
    _builder_1.append("<div class=\"incode\">");
    _builder_1.newLine();
    _builder_1.append("<p class=\"code\">");
    _builder_1.newLine();
    _builder_1.append("public&nbsp;void&nbsp;foo(String[]&nbsp;args);<br/>");
    _builder_1.newLine();
    _builder_1.append("<br/>");
    _builder_1.newLine();
    _builder_1.append("public&nbsp;void&nbsp;bar(String[]&nbsp;args);<br/>");
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.append("</div>");
    _builder_1.newLine();
    _builder_1.newLine();
    _builder_1.append("</p>");
    _builder_1.newLine();
    _builder_1.append("</body>");
    _builder_1.newLine();
    _builder_1.append("</html>");
    _builder_1.newLine();
    this.assertGeneratedHtml(_builder, _builder_1);
  }
  
  @Test
  public void testFormattedCodeWithNL() {
    String _plus = ("document[Main]\n\nchapter[Dummy Title]\n\ncode[\n" + 
      "class Foo {\n");
    String _plus_1 = (_plus + 
      "\tpublic \n");
    final String input = (_plus_1 + 
      "]\n");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<html>");
    _builder.newLine();
    _builder.append("<head>");
    _builder.newLine();
    _builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >");
    _builder.newLine();
    _builder.append("<title>Dummy Title</title>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder.newLine();
    _builder.append("<link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder.newLine();
    _builder.append("<link rel=\"home\" href=\"MyFile.html\" title=\"\">");
    _builder.newLine();
    _builder.append("</head>");
    _builder.newLine();
    _builder.append("<body>");
    _builder.newLine();
    _builder.append("<a name=\"Main_1\"></a>");
    _builder.newLine();
    _builder.append("<h1>Dummy Title</h1>");
    _builder.newLine();
    _builder.append("<p>");
    _builder.newLine();
    _builder.append("<div class=\"literallayout\">");
    _builder.newLine();
    _builder.append("<div class=\"incode\">");
    _builder.newLine();
    _builder.append("<p class=\"code\">");
    _builder.newLine();
    _builder.append("class&nbsp;Foo&nbsp;{<br/>");
    _builder.newLine();
    _builder.append("&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;<br/>");
    _builder.newLine();
    _builder.append("</p>");
    _builder.newLine();
    _builder.append("</div>");
    _builder.newLine();
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("</p>");
    _builder.newLine();
    _builder.append("</body>");
    _builder.newLine();
    _builder.append("</html>");
    _builder.newLine();
    this.assertGeneratedHtml(input, _builder);
  }
  
  @Test
  public void testFormattedCodeWithCRNL() {
    String _plus = ("document[Main]\n\nchapter[Dummy Title]\n\ncode[\r\n" + 
      "class Foo {\r\n");
    String _plus_1 = (_plus + 
      "\tpublic \r\n");
    final String input = (_plus_1 + 
      "]\n");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<html>");
    _builder.newLine();
    _builder.append("<head>");
    _builder.newLine();
    _builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >");
    _builder.newLine();
    _builder.append("<title>Dummy Title</title>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<link href=\"book.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder.newLine();
    _builder.append("<link href=\"code.css\" rel=\"stylesheet\" type=\"text/css\">");
    _builder.newLine();
    _builder.append("<link rel=\"home\" href=\"MyFile.html\" title=\"\">");
    _builder.newLine();
    _builder.append("</head>");
    _builder.newLine();
    _builder.append("<body>");
    _builder.newLine();
    _builder.append("<a name=\"Main_1\"></a>");
    _builder.newLine();
    _builder.append("<h1>Dummy Title</h1>");
    _builder.newLine();
    _builder.append("<p>");
    _builder.newLine();
    _builder.append("<div class=\"literallayout\">");
    _builder.newLine();
    _builder.append("<div class=\"incode\">");
    _builder.newLine();
    _builder.append("<p class=\"code\">");
    _builder.newLine();
    _builder.append("class&nbsp;Foo&nbsp;{<br/>");
    _builder.newLine();
    _builder.append("&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;<br/>");
    _builder.newLine();
    _builder.append("</p>");
    _builder.newLine();
    _builder.append("</div>");
    _builder.newLine();
    _builder.append("</div>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("</p>");
    _builder.newLine();
    _builder.append("</body>");
    _builder.newLine();
    _builder.append("</html>");
    _builder.newLine();
    this.assertGeneratedHtml(input, _builder);
  }
  
  public void assertGeneratedHtml(final CharSequence input, final CharSequence expected) {
    try {
      final IAcceptor<Result> _function = new IAcceptor<Result>() {
        public void accept(final Result it) {
          String _string = expected.toString();
          String _removeCR = EclipseHelpGeneratorTest2.this._utils.removeCR(_string);
          Map<String,CharSequence> _allGeneratedResources = it.getAllGeneratedResources();
          CharSequence _get = _allGeneratedResources.get("DEFAULT_OUTPUTMyFile_1.html");
          String _removeCR_1 = EclipseHelpGeneratorTest2.this._utils.removeCR(_get);
          Assert.assertEquals(_removeCR, _removeCR_1);
        }
      };
      this._compilationTestHelper.compile(input, _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
