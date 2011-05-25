package org.eclipse.xtext.xdoc.generator;

import com.google.inject.Inject;
import java.util.HashSet;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xdoc.generator.config.GeneratorConfig;
import org.eclipse.xtext.xdoc.generator.util.LatexUtils;
import org.eclipse.xtext.xdoc.generator.util.StringUtils;
import org.eclipse.xtext.xdoc.generator.util.Utils;
import org.eclipse.xtext.xdoc.generator.util.XFloat;
import org.eclipse.xtext.xdoc.xdoc.AbstractSection;
import org.eclipse.xtext.xdoc.xdoc.Anchor;
import org.eclipse.xtext.xdoc.xdoc.Chapter;
import org.eclipse.xtext.xdoc.xdoc.ChapterRef;
import org.eclipse.xtext.xdoc.xdoc.Code;
import org.eclipse.xtext.xdoc.xdoc.CodeBlock;
import org.eclipse.xtext.xdoc.xdoc.CodeRef;
import org.eclipse.xtext.xdoc.xdoc.Document;
import org.eclipse.xtext.xdoc.xdoc.Emphasize;
import org.eclipse.xtext.xdoc.xdoc.Identifiable;
import org.eclipse.xtext.xdoc.xdoc.ImageRef;
import org.eclipse.xtext.xdoc.xdoc.Item;
import org.eclipse.xtext.xdoc.xdoc.LangDef;
import org.eclipse.xtext.xdoc.xdoc.Link;
import org.eclipse.xtext.xdoc.xdoc.MarkupInCode;
import org.eclipse.xtext.xdoc.xdoc.OrderedList;
import org.eclipse.xtext.xdoc.xdoc.Ref;
import org.eclipse.xtext.xdoc.xdoc.Section;
import org.eclipse.xtext.xdoc.xdoc.Section2;
import org.eclipse.xtext.xdoc.xdoc.Section2Ref;
import org.eclipse.xtext.xdoc.xdoc.Section3;
import org.eclipse.xtext.xdoc.xdoc.Section4;
import org.eclipse.xtext.xdoc.xdoc.SectionRef;
import org.eclipse.xtext.xdoc.xdoc.Table;
import org.eclipse.xtext.xdoc.xdoc.TableData;
import org.eclipse.xtext.xdoc.xdoc.TableRow;
import org.eclipse.xtext.xdoc.xdoc.TextOrMarkup;
import org.eclipse.xtext.xdoc.xdoc.TextPart;
import org.eclipse.xtext.xdoc.xdoc.Todo;
import org.eclipse.xtext.xdoc.xdoc.UnorderedList;
import org.eclipse.xtext.xtend2.lib.ResourceExtensions;
import org.eclipse.xtext.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class LatexGenerator implements IGenerator {
  
  @Inject
  private Utils utils;
  
  @Inject
  private GeneratorConfig config;
  
  @Inject
  private HashSet<String> links;
  
  public void doGenerate(final Resource resource, final IFileSystemAccess fsa) {
    Iterable<EObject> _allContentsIterable = ResourceExtensions.allContentsIterable(resource);
    for (EObject element : _allContentsIterable) {
      if ((element instanceof org.eclipse.xtext.xdoc.xdoc.Document)) {
        {
          final Document doc = ((Document) element);
          String _name = doc.getName();
          String _fileName = this.fileName(_name);
          StringConcatenation _generate = this.generate(doc);
          fsa.generateFile(_fileName, _generate);
        }
      }
    }
  }
  
  public void doGenerate(final EObject obj, final IFileSystemAccess fsa) {
    {
      final Document doc = ((Document) obj);
      String _name = doc.getName();
      String _fileName = this.fileName(_name);
      StringConcatenation _generate = this.generate(doc);
      fsa.generateFile(_fileName, _generate);
    }
  }
  
  public String fileName(final String name) {
    String _operator_plus = StringExtensions.operator_plus(name, ".tex");
    return _operator_plus;
  }
  
  protected StringConcatenation _generate(final Document doc) {
    StringConcatenation _builder = new StringConcatenation();
    StringConcatenation _preamble = this.preamble();
    _builder.append(_preamble, "");
    _builder.newLineIfNotEmpty();
    {
      EList<LangDef> _langDefs = doc.getLangDefs();
      for(LangDef lang : _langDefs) {
        StringConcatenation _generate = this.generate(lang);
        _builder.append(_generate, "");
        _builder.newLineIfNotEmpty();
      }
    }
    StringConcatenation _configureTodo = this.configureTodo();
    _builder.append(_configureTodo, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\\usepackage{hyperref}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\\newlength{\\itemindentlen}");
    _builder.newLine();
    StringConcatenation _authorAndTitle = this.authorAndTitle(doc);
    _builder.append(_authorAndTitle, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\\begin{document}");
    _builder.newLine();
    _builder.append("\\maketitle");
    _builder.newLine();
    _builder.append("\\tableofcontents");
    _builder.newLine();
    {
      EList<Chapter> _chapters = doc.getChapters();
      for(Chapter chapter : _chapters) {
        _builder.newLine();
        StringConcatenation _generate_1 = this.generate(chapter);
        _builder.append(_generate_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    StringConcatenation _genListOfLinks = this.genListOfLinks();
    _builder.append(_genListOfLinks, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _release = this.config.release();
      boolean _operator_not = BooleanExtensions.operator_not(_release);
      if (_operator_not) {
        _builder.append("\\listoftodos");
        _builder.newLine();
      }
    }
    _builder.append("\\end{document}");
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation genListOfLinks() {
    StringConcatenation _xifexpression = null;
    boolean _isEmpty = this.links.isEmpty();
    boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
    if (_operator_not) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\\chapter{List of External Links}");
      _builder.newLine();
      {
        for(String link : this.links) {
          _builder.append("\\noindent\\url{");
          _builder.append(link, "");
          _builder.append("}");
          _builder.newLineIfNotEmpty();
        }
      }
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  public StringConcatenation preamble() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\documentclass[a4paper]{scrreprt}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\\usepackage[T1]{fontenc}");
    _builder.newLine();
    _builder.append("\\usepackage{ae,aecompl,aeguill} ");
    _builder.newLine();
    _builder.append("\\usepackage[latin1]{inputenc}");
    _builder.newLine();
    _builder.append("\\usepackage{listings}");
    _builder.newLine();
    _builder.append("\\usepackage[american]{babel}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\\usepackage{todonotes}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\\makeatletter");
    _builder.newLine();
    _builder.append("\\renewcommand\\subsection{\\medskip\\@startsection{subsection}{2}{\\z@}%");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{-.25ex\\@plus -.1ex \\@minus -.2ex}%");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{1.5ex \\@plus .2ex \\@minus -.4ex}%");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("{\\ifnum \\scr@compatibility>\\@nameuse{scr@v@2.96}\\relax");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\\setlength{\\parfillskip}{\\z@ plus 1fil}\\fi");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("\\raggedsection\\normalfont\\sectfont\\nobreak\\size@subsection");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}%");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\\makeatother");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\\lstset{tabsize=4, basicstyle=\\sffamily\\small, commentstyle=\\textsl, keywordstyle=\\bfseries, columns=[r]fullflexible, escapechar={\u00DF}}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation configureTodo() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _release = this.config.release();
      if (_release) {
        _builder.append("\\renewcommand{\\todo}[1]{}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _generate(final LangDef lang) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\lstdefinelanguage{");
    String _name = lang.getName();
    _builder.append(_name, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("{morekeywords={");
    EList<String> _keywords = lang.getKeywords();
    String _join = IterableExtensions.join(_keywords, ", ");
    _builder.append(_join, "  ");
    _builder.append("},");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("sensitive=true,");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("morecomment=[l]{//},");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("morecomment=[s]{/*}{*/},");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("morestring=[b]\",");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("morestring=[b]\',");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public StringConcatenation authorAndTitle(final Document doc) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _operator_and = false;
      TextOrMarkup _authors = doc.getAuthors();
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_authors, null);
      if (!_operator_notEquals) {
        _operator_and = false;
      } else {
        TextOrMarkup _authors_1 = doc.getAuthors();
        EList<EObject> _contents = _authors_1.getContents();
        boolean _isEmpty = _contents.isEmpty();
        boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
        _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _operator_not);
      }
      if (_operator_and) {
        _builder.append("\\author{");
        {
          TextOrMarkup _authors_2 = doc.getAuthors();
          EList<EObject> _contents_1 = _authors_2.getContents();
          for(EObject o : _contents_1) {
            CharSequence _genText = this.genText(o);
            _builder.append(_genText, "");
          }
        }
        _builder.append("}");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      TextOrMarkup _title = doc.getTitle();
      boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_title, null);
      if (_operator_notEquals_1) {
        _builder.append("\\title{");
        {
          TextOrMarkup _title_1 = doc.getTitle();
          EList<EObject> _contents_2 = _title_1==null?(EList<EObject>)null:_title_1.getContents();
          for(EObject o_1 : _contents_2) {
            CharSequence _genText_1 = this.genText(o_1);
            _builder.append(_genText_1, "");
          }
        }
        _builder.append("}");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _generate(final AbstractSection sec) {
    StringConcatenation _builder = new StringConcatenation();
    StringConcatenation _switchResult = null;
    final AbstractSection sec_1 = sec;
    boolean matched = false;
    if (!matched) {
      if (sec_1 instanceof Chapter) {
        final Chapter sec_2 = (Chapter) sec_1;
        matched=true;
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("\\chapter{");
        TextOrMarkup _title = sec_2.getTitle();
        StringConcatenation _genContent = this.genContent(_title);
        _builder_1.append(_genContent, "");
        _builder_1.append("}");
        _switchResult = _builder_1;
      }
    }
    if (!matched) {
      if (sec_1 instanceof Section) {
        final Section sec_3 = (Section) sec_1;
        matched=true;
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("\\section{");
        TextOrMarkup _title_1 = sec_3.getTitle();
        StringConcatenation _genContent_1 = this.genContent(_title_1);
        _builder_2.append(_genContent_1, "");
        _builder_2.append("}");
        _switchResult = _builder_2;
      }
    }
    if (!matched) {
      if (sec_1 instanceof Section2) {
        final Section2 sec_4 = (Section2) sec_1;
        matched=true;
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("\\subsection{");
        TextOrMarkup _title_2 = sec_4.getTitle();
        StringConcatenation _genContent_2 = this.genContent(_title_2);
        _builder_3.append(_genContent_2, "");
        _builder_3.append("}");
        _switchResult = _builder_3;
      }
    }
    if (!matched) {
      if (sec_1 instanceof Section3) {
        final Section3 sec_5 = (Section3) sec_1;
        matched=true;
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("\\subsubsection{");
        TextOrMarkup _title_3 = sec_5.getTitle();
        StringConcatenation _genContent_3 = this.genContent(_title_3);
        _builder_4.append(_genContent_3, "");
        _builder_4.append("}");
        _switchResult = _builder_4;
      }
    }
    if (!matched) {
      if (sec_1 instanceof Section4) {
        final Section4 sec_6 = (Section4) sec_1;
        matched=true;
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("\\paragraph{");
        TextOrMarkup _title_4 = sec_6.getTitle();
        StringConcatenation _genContent_4 = this.genContent(_title_4);
        _builder_5.append(_genContent_4, "");
        _builder_5.append("}");
        _switchResult = _builder_5;
      }
    }
    _builder.append(_switchResult, "");
    _builder.newLineIfNotEmpty();
    StringConcatenation _xifexpression = null;
    String _name = sec.getName();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_name, null);
    if (_operator_notEquals) {
      StringConcatenation _switchResult_1 = null;
      final AbstractSection sec_7 = sec;
      boolean matched_1 = false;
      if (!matched_1) {
        if (sec_7 instanceof Chapter) {
          final Chapter sec_8 = (Chapter) sec_7;
          matched_1=true;
          StringConcatenation _genLabel = this.genLabel(sec_8);
          _switchResult_1 = _genLabel;
        }
      }
      if (!matched_1) {
        if (sec_7 instanceof Section) {
          final Section sec_9 = (Section) sec_7;
          matched_1=true;
          StringConcatenation _genLabel_1 = this.genLabel(sec_9);
          _switchResult_1 = _genLabel_1;
        }
      }
      if (!matched_1) {
        TextOrMarkup _title_5 = sec.getTitle();
        StringConcatenation _genLabel_2 = this.genLabel(_title_5);
        _switchResult_1 = _genLabel_2;
      }
      _xifexpression = _switchResult_1;
    }
    _builder.append(_xifexpression, "");
    _builder.newLineIfNotEmpty();
    StringConcatenation _genContent_5 = this.genContent(sec);
    _builder.append(_genContent_5, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected StringConcatenation _genContent(final Chapter chap) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = chap.getContents();
      for(TextOrMarkup c : _contents) {
        StringConcatenation _genContent = this.genContent(c);
        _builder.append(_genContent, "");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      EList<Section> _subSections = chap.getSubSections();
      for(Section sub : _subSections) {
        StringConcatenation _generate = this.generate(sub);
        _builder.append(_generate, "");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected StringConcatenation _genContent(final Section sec) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = sec.getContents();
      for(TextOrMarkup c : _contents) {
        StringConcatenation _genContent = this.genContent(c);
        _builder.append(_genContent, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Section2> _subSections = sec.getSubSections();
      for(Section2 sub : _subSections) {
        StringConcatenation _generate = this.generate(sub);
        _builder.append(_generate, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _genContent(final Section2 sec) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = sec.getContents();
      for(TextOrMarkup c : _contents) {
        StringConcatenation _genContent = this.genContent(c);
        _builder.append(_genContent, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Section3> _subSections = sec.getSubSections();
      for(Section3 sub : _subSections) {
        StringConcatenation _generate = this.generate(sub);
        _builder.append(_generate, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _genContent(final Section3 sec) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = sec.getContents();
      for(TextOrMarkup c : _contents) {
        StringConcatenation _generate = this.generate(c);
        _builder.append(_generate, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      EList<Section4> _subSections = sec.getSubSections();
      for(Section4 sub : _subSections) {
        StringConcatenation _generate_1 = this.generate(sub);
        _builder.append(_generate_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _genContent(final Section4 sec) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = sec.getContents();
      for(TextOrMarkup c : _contents) {
        StringConcatenation _genContent = this.genContent(c);
        _builder.append(_genContent, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _genContent(final TextOrMarkup tom) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<EObject> _contents = tom.getContents();
      for(EObject e : _contents) {
        CharSequence _genText = this.genText(e);
        _builder.append(_genText, "");
      }
    }
    return _builder;
  }
  
  public StringConcatenation genNonParContent(final TextOrMarkup tom) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<EObject> _contents = tom.getContents();
      for(EObject e : _contents) {
        CharSequence _genText = this.genText(e);
        _builder.append(_genText, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final ChapterRef cRef) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\label{");
    Chapter _chapter = cRef.getChapter();
    String _name = _chapter.getName();
    String _string = _name==null?(String)null:_name.toString();
    _builder.append(_string, "");
    _builder.append("}");
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final Chapter chap) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\label{");
    String _name = chap.getName();
    String _string = _name==null?(String)null:_name.toString();
    _builder.append(_string, "");
    _builder.append("}");
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final SectionRef sRef) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\label{");
    Section _section = sRef.getSection();
    String _name = _section.getName();
    String _string = _name==null?(String)null:_name.toString();
    _builder.append(_string, "");
    _builder.append("}");
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final Section2Ref sRef) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\label{");
    Section2 _section2 = sRef.getSection2();
    String _name = _section2.getName();
    String _string = _name==null?(String)null:_name.toString();
    _builder.append(_string, "");
    _builder.append("}");
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final Section sec) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\label{");
    String _name = sec.getName();
    String _string = _name==null?(String)null:_name.toString();
    _builder.append(_string, "");
    _builder.append("}");
    return _builder;
  }
  
  protected StringConcatenation _genLabel(final TextOrMarkup tom) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public StringConcatenation genURL(final String str) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\noindent\\url{");
    _builder.append(str, "");
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence _genText(final Table tab) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\noindent\\begin{tabular}{");
    EList<TableRow> _rows = tab.getRows();
    TableRow _head = IterableExtensions.<TableRow>head(_rows);
    EList<TableData> _data = _head.getData();
    StringConcatenation _genColumns = this.genColumns(_data);
    _builder.append(_genColumns, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    EList<TableRow> _rows_1 = tab.getRows();
    final Function1<TableRow,CharSequence> _function = new Function1<TableRow,CharSequence>() {
        public CharSequence apply(final TableRow e) {
          CharSequence _genText = LatexGenerator.this.genText(e);
          return _genText;
        }
      };
    List<CharSequence> _map = ListExtensions.<TableRow, CharSequence>map(_rows_1, _function);
    String _join = IterableExtensions.join(_map, "\\\\\n");
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\\end{tabular}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _genText(final TableRow row) {
    EList<TableData> _data = row.getData();
    final Function1<TableData,CharSequence> _function = new Function1<TableData,CharSequence>() {
        public CharSequence apply(final TableData e) {
          CharSequence _genText = LatexGenerator.this.genText(e);
          return _genText;
        }
      };
    List<CharSequence> _map = ListExtensions.<TableData, CharSequence>map(_data, _function);
    String _join = IterableExtensions.join(_map, " & ");
    return _join;
  }
  
  protected CharSequence _genText(final TableData tData) {
    EList<TextOrMarkup> _contents = tData.getContents();
    final Function1<TextOrMarkup,StringConcatenation> _function = new Function1<TextOrMarkup,StringConcatenation>() {
        public StringConcatenation apply(final TextOrMarkup e) {
          StringConcatenation _genContent = LatexGenerator.this.genContent(e);
          return _genContent;
        }
      };
    List<StringConcatenation> _map = ListExtensions.<TextOrMarkup, StringConcatenation>map(_contents, _function);
    String _join = IterableExtensions.join(_map);
    return _join;
  }
  
  protected CharSequence _genText(final TextPart part) {
    String _text = part.getText();
    String _unescapeXdocChars = this.utils.unescapeXdocChars(_text);
    String _escapeLatexChars = this.utils.escapeLatexChars(_unescapeXdocChars);
    return _escapeLatexChars;
  }
  
  protected CharSequence _genText(final OrderedList ol) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\setlength{\\itemindentlen}{\\textwidth}");
    _builder.newLine();
    _builder.append("\\begin{enumerate}");
    _builder.newLine();
    _builder.append("\\addtolength{\\itemindentlen}{-2.5em}");
    _builder.newLine();
    EList<Item> _items = ol.getItems();
    final Function1<Item,CharSequence> _function = new Function1<Item,CharSequence>() {
        public CharSequence apply(final Item e) {
          CharSequence _genText = LatexGenerator.this.genText(e);
          return _genText;
        }
      };
    List<CharSequence> _map = ListExtensions.<Item, CharSequence>map(_items, _function);
    String _join = IterableExtensions.join(_map);
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\\end{enumerate}");
    _builder.newLine();
    _builder.append("\\addtolength{\\itemindentlen}{2.5em}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _genText(final UnorderedList ul) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\setlength{\\itemindentlen}{\\textwidth}");
    _builder.newLine();
    _builder.append("\\begin{itemize}");
    _builder.newLine();
    _builder.append("\\addtolength{\\itemindentlen}{-2.5em}");
    _builder.newLine();
    EList<Item> _items = ul.getItems();
    final Function1<Item,CharSequence> _function = new Function1<Item,CharSequence>() {
        public CharSequence apply(final Item e) {
          CharSequence _genText = LatexGenerator.this.genText(e);
          return _genText;
        }
      };
    List<CharSequence> _map = ListExtensions.<Item, CharSequence>map(_items, _function);
    String _join = IterableExtensions.join(_map);
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\\end{itemize}");
    _builder.newLine();
    _builder.append("\\addtolength{\\itemindentlen}{2.5em}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _genText(final Item item) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\item \\begin{minipage}[t]{\\itemindentlen}");
    {
      EList<TextOrMarkup> _contents = item.getContents();
      TextOrMarkup _head = IterableExtensions.<TextOrMarkup>head(_contents);
      boolean _block = LatexUtils.block(_head);
      if (_block) {
        _builder.append("\\vspace*{-\\baselineskip}");
      }
    }
    _builder.newLineIfNotEmpty();
    EList<TextOrMarkup> _contents_1 = item.getContents();
    final Function1<TextOrMarkup,StringConcatenation> _function = new Function1<TextOrMarkup,StringConcatenation>() {
        public StringConcatenation apply(final TextOrMarkup e) {
          StringConcatenation _genContent = LatexGenerator.this.genContent(e);
          return _genContent;
        }
      };
    List<StringConcatenation> _map = ListExtensions.<TextOrMarkup, StringConcatenation>map(_contents_1, _function);
    String _join = IterableExtensions.join(_map);
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\\end{minipage}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _genText(final Emphasize em) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\textit{");
    EList<TextOrMarkup> _contents = em.getContents();
    final Function1<TextOrMarkup,StringConcatenation> _function = new Function1<TextOrMarkup,StringConcatenation>() {
        public StringConcatenation apply(final TextOrMarkup e) {
          StringConcatenation _genContent = LatexGenerator.this.genContent(e);
          return _genContent;
        }
      };
    List<StringConcatenation> _map = ListExtensions.<TextOrMarkup, StringConcatenation>map(_contents, _function);
    String _join = IterableExtensions.join(_map);
    _builder.append(_join, "");
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence _genText(final Ref ref) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<TextOrMarkup> _contents = ref.getContents();
      boolean _isEmpty = _contents.isEmpty();
      if (_isEmpty) {
        _builder.append("\\autoref{");
        Identifiable _ref = ref.getRef();
        String _name = _ref.getName();
        _builder.append(_name, "");
        _builder.append("}");} else {
        _builder.append("\\hyperref[");
        Identifiable _ref_1 = ref.getRef();
        String _name_1 = _ref_1.getName();
        _builder.append(_name_1, "");
        _builder.append("]{");
        EList<TextOrMarkup> _contents_1 = ref.getContents();
        final Function1<TextOrMarkup,StringConcatenation> _function = new Function1<TextOrMarkup,StringConcatenation>() {
            public StringConcatenation apply(final TextOrMarkup e) {
              StringConcatenation _genContent = LatexGenerator.this.genContent(e);
              return _genContent;
            }
          };
        List<StringConcatenation> _map = ListExtensions.<TextOrMarkup, StringConcatenation>map(_contents_1, _function);
        String _join = IterableExtensions.join(_map);
        _builder.append(_join, "");
        _builder.append("~(\u00A7\\ref*{");
        Identifiable _ref_2 = ref.getRef();
        String _name_2 = _ref_2.getName();
        _builder.append(_name_2, "");
        _builder.append("})}");
      }
    }
    return _builder;
  }
  
  protected CharSequence _genText(final Anchor anchor) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\phantomsection\\label{");
    String _name = anchor.getName();
    _builder.append(_name, "");
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence _genText(final Link link) {
    StringConcatenation _xblockexpression = null;
    {
      String _url = link.getUrl();
      this.links.add(_url);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\\href{");
      String _url_1 = link.getUrl();
      _builder.append(_url_1, "");
      _builder.append("}{");
      String _text = link.getText();
      _builder.append(_text, "");
      _builder.append("}");
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _genText(final CodeBlock block) {
    CodeBlock _removeIndent = StringUtils.removeIndent(block);
    StringConcatenation _specialGenCode = this.specialGenCode(_removeIndent);
    return _specialGenCode;
  }
  
  protected CharSequence _genText(final CodeRef codeRef) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\lstinline\u00B0");
    JvmDeclaredType _element = codeRef.getElement();
    String _qualifiedName = _element.getQualifiedName();
    String _unescapeXdocChars = this.utils.unescapeXdocChars(_qualifiedName);
    String _escapeLatexChars = this.utils.escapeLatexChars(_unescapeXdocChars);
    _builder.append(_escapeLatexChars, "");
    _builder.append("\u00B0");
    return _builder;
  }
  
  protected CharSequence _genText(final ImageRef imgRef) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _genText(final Todo todo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\\todo[inline]{");
    String _text = todo.getText();
    String _unescapeXdocChars = this.utils.unescapeXdocChars(_text);
    String _escapeLatexChars = this.utils.escapeLatexChars(_unescapeXdocChars);
    _builder.append(_escapeLatexChars, "");
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence _genText(final MarkupInCode mic) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _genText(final String str) {
    return str;
  }
  
  protected CharSequence _genText(final EObject o) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public StringConcatenation specialGenCode(final CodeBlock block) {
    StringConcatenation _xifexpression = null;
    boolean _inline = LatexUtils.inline(block);
    if (_inline) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\\lstinline");
      LangDef _language = block.getLanguage();
      StringConcatenation _langSpec = this==null?(StringConcatenation)null:this.langSpec(_language);
      _builder.append(_langSpec, "");
      _builder.append("\u00B0");
      EList<EObject> _contents = block.getContents();
      final Function1<EObject,CharSequence> _function = new Function1<EObject,CharSequence>() {
          public CharSequence apply(final EObject e) {
            CharSequence _genCode = LatexGenerator.this.genCode(e);
            return _genCode;
          }
        };
      List<CharSequence> _map = ListExtensions.<EObject, CharSequence>map(_contents, _function);
      String _join = IterableExtensions.join(_map);
      _builder.append(_join, "");
      _builder.append("\u00B0");
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.newLine();
      _builder_1.append("\\begin{lstlisting}");
      LangDef _language_1 = block.getLanguage();
      StringConcatenation _langSpec_1 = this==null?(StringConcatenation)null:this.langSpec(_language_1);
      _builder_1.append(_langSpec_1, "");
      _builder_1.newLineIfNotEmpty();
      EList<EObject> _contents_1 = block.getContents();
      final Function1<EObject,CharSequence> _function_1 = new Function1<EObject,CharSequence>() {
          public CharSequence apply(final EObject e_1) {
            CharSequence _genCode_1 = LatexGenerator.this.genCode(e_1);
            return _genCode_1;
          }
        };
      List<CharSequence> _map_1 = ListExtensions.<EObject, CharSequence>map(_contents_1, _function_1);
      String _join_1 = IterableExtensions.join(_map_1);
      _builder_1.append(_join_1, "");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\\end{lstlisting}");
      _builder_1.newLine();
      _xifexpression = _builder_1;
    }
    return _xifexpression;
  }
  
  public StringConcatenation langSpec(final LangDef lang) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(lang, null);
      if (_operator_notEquals) {
        _builder.append("[language=");
        String _name = lang.getName();
        _builder.append(_name, "");
        _builder.append("]");
      }
    }
    return _builder;
  }
  
  protected CharSequence _genCode(final Code code) {
    String _contents = code.getContents();
    String _unescapeXdocChars = this.utils.unescapeXdocChars(_contents);
    String _prepareListingsString = this.utils.prepareListingsString(_unescapeXdocChars);
    return _prepareListingsString;
  }
  
  protected CharSequence _genCode(final MarkupInCode mic) {
    StringConcatenation _builder = new StringConcatenation();
    String _lstEscapeToTex = LatexUtils.lstEscapeToTex();
    _builder.append(_lstEscapeToTex, "");
    CharSequence _genText = this.genText(mic);
    _builder.append(_genText, "");
    String _lstEscapeToTex_1 = LatexUtils.lstEscapeToTex();
    _builder.append(_lstEscapeToTex_1, "");
    return _builder;
  }
  
  protected CharSequence _genCode(final Object o) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public StringConcatenation genColumns(final List<TableData> tabData) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isEmpty = tabData.isEmpty();
      boolean _operator_not = BooleanExtensions.operator_not(_isEmpty);
      if (_operator_not) {
        _builder.append("|");
        {
          for(TableData td : tabData) {
            _builder.append("p{");
            XFloat _xFloat = new XFloat(1);
            int _size = tabData.size();
            XFloat _xFloat_1 = new XFloat(_size);
            XFloat _operator_divide = _xFloat.operator_divide(_xFloat_1);
            _builder.append(_operator_divide, "");
            _builder.append("\\textwidth}|");
          }
        }
      }
    }
    return _builder;
  }
  
  public StringConcatenation generate(final EObject doc) {
    if ((doc instanceof Document)) {
      return _generate((Document)doc);
    } else if ((doc instanceof AbstractSection)) {
      return _generate((AbstractSection)doc);
    } else if ((doc instanceof LangDef)) {
      return _generate((LangDef)doc);
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  public StringConcatenation genContent(final EObject chap) {
    if ((chap instanceof Chapter)) {
      return _genContent((Chapter)chap);
    } else if ((chap instanceof Section)) {
      return _genContent((Section)chap);
    } else if ((chap instanceof Section2)) {
      return _genContent((Section2)chap);
    } else if ((chap instanceof Section3)) {
      return _genContent((Section3)chap);
    } else if ((chap instanceof Section4)) {
      return _genContent((Section4)chap);
    } else if ((chap instanceof TextOrMarkup)) {
      return _genContent((TextOrMarkup)chap);
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  public StringConcatenation genLabel(final EObject cRef) {
    if ((cRef instanceof ChapterRef)) {
      return _genLabel((ChapterRef)cRef);
    } else if ((cRef instanceof Section2Ref)) {
      return _genLabel((Section2Ref)cRef);
    } else if ((cRef instanceof SectionRef)) {
      return _genLabel((SectionRef)cRef);
    } else if ((cRef instanceof Chapter)) {
      return _genLabel((Chapter)cRef);
    } else if ((cRef instanceof Section)) {
      return _genLabel((Section)cRef);
    } else if ((cRef instanceof TextOrMarkup)) {
      return _genLabel((TextOrMarkup)cRef);
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  public CharSequence genText(final Object anchor) {
    if ((anchor instanceof Anchor)) {
      return _genText((Anchor)anchor);
    } else if ((anchor instanceof CodeBlock)) {
      return _genText((CodeBlock)anchor);
    } else if ((anchor instanceof CodeRef)) {
      return _genText((CodeRef)anchor);
    } else if ((anchor instanceof Emphasize)) {
      return _genText((Emphasize)anchor);
    } else if ((anchor instanceof ImageRef)) {
      return _genText((ImageRef)anchor);
    } else if ((anchor instanceof Link)) {
      return _genText((Link)anchor);
    } else if ((anchor instanceof OrderedList)) {
      return _genText((OrderedList)anchor);
    } else if ((anchor instanceof Ref)) {
      return _genText((Ref)anchor);
    } else if ((anchor instanceof Table)) {
      return _genText((Table)anchor);
    } else if ((anchor instanceof Todo)) {
      return _genText((Todo)anchor);
    } else if ((anchor instanceof UnorderedList)) {
      return _genText((UnorderedList)anchor);
    } else if ((anchor instanceof Item)) {
      return _genText((Item)anchor);
    } else if ((anchor instanceof MarkupInCode)) {
      return _genText((MarkupInCode)anchor);
    } else if ((anchor instanceof TableData)) {
      return _genText((TableData)anchor);
    } else if ((anchor instanceof TableRow)) {
      return _genText((TableRow)anchor);
    } else if ((anchor instanceof TextPart)) {
      return _genText((TextPart)anchor);
    } else if ((anchor instanceof String)) {
      return _genText((String)anchor);
    } else if ((anchor instanceof EObject)) {
      return _genText((EObject)anchor);
    } else {
      throw new IllegalArgumentException();
    }
  }
  
  public CharSequence genCode(final Object code) {
    if ((code instanceof Code)) {
      return _genCode((Code)code);
    } else if ((code instanceof MarkupInCode)) {
      return _genCode((MarkupInCode)code);
    } else if ((code instanceof Object)) {
      return _genCode((Object)code);
    } else {
      throw new IllegalArgumentException();
    }
  }
}