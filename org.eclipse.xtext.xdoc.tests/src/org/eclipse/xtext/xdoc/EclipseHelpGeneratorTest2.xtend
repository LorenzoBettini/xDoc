package org.eclipse.xtext.xdoc

import com.google.inject.Inject
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.xbase.compiler.CompilationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*
import org.eclipse.xtext.xdoc.generator.util.Utils

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(XdocInjectorProvider))
class EclipseHelpGeneratorTest2 {
	
	@Inject extension CompilationTestHelper
	@Inject extension Utils
	
	@Test
	def public void testFormattedCode() {
'''
document[Main]

chapter[Dummy Title]

code[
class Foo {
	public static void main(String\[\] args){
		System.out.println("Hello World\n");
	}
}
]
'''.assertGeneratedHtml(
'''
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>Dummy Title</title>

<link href="book.css" rel="stylesheet" type="text/css">
<link href="code.css" rel="stylesheet" type="text/css">
<link rel="home" href="MyFile.html" title="">
</head>
<body>
<a name="Main_1"></a>
<h1>Dummy Title</h1>
<p>
<div class="literallayout">
<div class="incode">
<p class="code">
class&nbsp;Foo&nbsp;{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;static&nbsp;void&nbsp;main(String[]&nbsp;args){<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<span class="string">"Hello&nbsp;World\n"</span>);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
}<br/>
</p>
</div>
</div>

</p>
</body>
</html>
''')
	}

	@Test
	def public void testFormattedCode_02() {
'''
document[Main]

chapter[Dummy Title]

code[

class Foo {
	
	public static void main(String\[\] args){
		System.out.println("Hello World\n");
	}
}
]
'''.assertGeneratedHtml(
'''
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>Dummy Title</title>

<link href="book.css" rel="stylesheet" type="text/css">
<link href="code.css" rel="stylesheet" type="text/css">
<link rel="home" href="MyFile.html" title="">
</head>
<body>
<a name="Main_1"></a>
<h1>Dummy Title</h1>
<p>
<div class="literallayout">
<div class="incode">
<p class="code">
class&nbsp;Foo&nbsp;{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;static&nbsp;void&nbsp;main(String[]&nbsp;args){<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(<span class="string">"Hello&nbsp;World\n"</span>);<br/>
&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
}<br/>
</p>
</div>
</div>

</p>
</body>
</html>
''')
	}

	@Test
	def public void testFormattedCode_03() {
'''
document[Main]

chapter[Dummy Title]

code[
	public void foo(String\[\] args);
	
	public void bar(String\[\] args);
]
'''.assertGeneratedHtml(
'''
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>Dummy Title</title>

<link href="book.css" rel="stylesheet" type="text/css">
<link href="code.css" rel="stylesheet" type="text/css">
<link rel="home" href="MyFile.html" title="">
</head>
<body>
<a name="Main_1"></a>
<h1>Dummy Title</h1>
<p>
<div class="literallayout">
<div class="incode">
<p class="code">
public&nbsp;void&nbsp;foo(String[]&nbsp;args);<br/>
<br/>
public&nbsp;void&nbsp;bar(String[]&nbsp;args);<br/>
</p>
</div>
</div>

</p>
</body>
</html>
''')
	}

	@Test
	def public void testFormattedCodeWithNL() {
		val input =
		"document[Main]\n\nchapter[Dummy Title]\n\ncode[\n" +
		"class Foo {\n" +
		"\tpublic \n" +
		"]\n"

input.assertGeneratedHtml(
'''
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>Dummy Title</title>

<link href="book.css" rel="stylesheet" type="text/css">
<link href="code.css" rel="stylesheet" type="text/css">
<link rel="home" href="MyFile.html" title="">
</head>
<body>
<a name="Main_1"></a>
<h1>Dummy Title</h1>
<p>
<div class="literallayout">
<div class="incode">
<p class="code">
class&nbsp;Foo&nbsp;{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;<br/>
</p>
</div>
</div>

</p>
</body>
</html>
''')
	}

	@Test
	def public void testFormattedCodeWithCRNL() {
		val input =
		"document[Main]\n\nchapter[Dummy Title]\n\ncode[\r\n" +
		"class Foo {\r\n" +
		"\tpublic \r\n" +
		"]\n"

input.assertGeneratedHtml(
'''
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>Dummy Title</title>

<link href="book.css" rel="stylesheet" type="text/css">
<link href="code.css" rel="stylesheet" type="text/css">
<link rel="home" href="MyFile.html" title="">
</head>
<body>
<a name="Main_1"></a>
<h1>Dummy Title</h1>
<p>
<div class="literallayout">
<div class="incode">
<p class="code">
class&nbsp;Foo&nbsp;{<br/>
&nbsp;&nbsp;&nbsp;&nbsp;public&nbsp;<br/>
</p>
</div>
</div>

</p>
</body>
</html>
''')
	}
	
	def void assertGeneratedHtml(CharSequence input, CharSequence expected) {
		input.compile[
			expected.toString.removeCR.assertEquals(
				getAllGeneratedResources.
					get("DEFAULT_OUTPUTMyFile_1.html").removeCR)
		]
	}
}