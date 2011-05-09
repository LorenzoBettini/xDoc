package org.eclipse.xtext.xdoc.generator

import org.eclipse.xtext.xdoc.xdoc.*
import org.eclipse.xtext.xtend2.lib.StringConcatenation

class PlainText {

	def dispatch genPlainText(TextOrMarkup tom) {
		tom.contents.fold('''''', [e1, e2 | '''�e1��e2.genPlainText�'''])
	}

	def dispatch genPlainText(TextPart tp){
		tp.text
	}

	def dispatch genPlainText(Emphasize em){
		em.contents.fold('''''', [e1, e2 | '''�e1��e2.genPlainText�'''])
	}

	def dispatch genPlainText(Link l) {
		val text = '''�l.text�'''
		if(text.toString != text)
			text
		else 
			'''"�l.url�"'''
	}
}