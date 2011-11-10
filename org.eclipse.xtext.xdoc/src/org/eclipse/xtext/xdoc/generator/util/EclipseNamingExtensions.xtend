package org.eclipse.xtext.xdoc.generator.util

import com.google.inject.Inject
import org.eclipse.xtext.xdoc.generator.AbstractSectionExtension
import org.eclipse.xtext.xdoc.xdoc.AbstractSection
import org.eclipse.xtext.xdoc.xdoc.ChapterRef
import org.eclipse.xtext.xdoc.xdoc.Identifiable
import org.eclipse.xtext.xdoc.xdoc.Part
import org.eclipse.xtext.xdoc.xdoc.PartRef
import org.eclipse.xtext.xdoc.xdoc.Section2Ref
import org.eclipse.xtext.xdoc.xdoc.SectionRef
import org.eclipse.xtext.xdoc.xdoc.XdocFile
import org.eclipse.emf.common.util.URI

class EclipseNamingExtensions {

	@Inject extension AbstractSectionExtension sectionExtension

	def String getLocalId(Identifiable identifiable) {
		switch identifiable {
			ChapterRef : identifiable.chapter.localId			
			SectionRef : identifiable.section.localId			
			Section2Ref : identifiable.section2.localId			
			default : {
				if (identifiable.name != null)
					return URI::encodeFragment(identifiable.name, false)
				val parent = identifiable.eContainer as AbstractSection
				if (parent == null)
					return "0"
				else
					return parent.localId+"_"+parent.eContents.indexOf(identifiable)
			}
		}
	}

	def String getFullURL(Identifiable identifiable) {
		switch identifiable {
			PartRef : identifiable.part.fullURL
			ChapterRef : identifiable.chapter.fullURL
			SectionRef : identifiable.section.fullURL
			Section2Ref : identifiable.section2.fullURL
			case identifiable.eContainer instanceof XdocFile :
				identifiable.resourceURL
			Part:
				identifiable.eResource.URI.trimFileExtension.lastSegment + "_" + identifiable.eContainer.eContents.indexOf(identifiable) + ".html"
			default :
				identifiable.resourceURL + '#' + identifiable.localId
		}
	}
	
	def String getResourceURL(Identifiable identifiable) {
		switch identifiable {
			PartRef : identifiable.part.resourceURL
			ChapterRef : identifiable.chapter.resourceURL			
			SectionRef : identifiable.section.resourceURL			
			Section2Ref : identifiable.section2.resourceURL			
			default : {
				return identifiable.eResource.URI.trimFileExtension.lastSegment+".html"
			}
		}
	}

	def String getFullPHPURL(Identifiable identifiable) {
		getFullURL(identifiable).replace(".html", ".php")
	}
}
