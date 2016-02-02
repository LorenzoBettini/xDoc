/**
 * 
 */
package org.eclipse.xtext.xdoc.util;

import org.eclipse.xtext.xdoc.XdocInjectorProvider;
import org.eclipse.xtext.xdoc.XdocRuntimeModule;
import org.eclipse.xtext.xdoc.XdocStandaloneSetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Lorenzo Bettini
 *
 */
public class XdocInjectorProviderCustom extends XdocInjectorProvider {

	@Override
	protected Injector internalCreateInjector() {
		return new XdocStandaloneSetup() {
			@Override
			public Injector createInjector() {
				return Guice.createInjector(new XdocRuntimeModule() {
					// this is required only by the CompilationTestHelper since
					// Xtext 2.7
					@SuppressWarnings("unused")
					public Class<? extends org.eclipse.xtend.lib.macro.file.MutableFileSystemSupport> bindMutableFileSystemSupport() {
						return org.eclipse.xtext.xbase.file.JavaIOFileSystemSupport.class;
					}

					// this is required only by the CompilationTestHelper since
					// Xtext 2.7
					@SuppressWarnings("unused")
					public Class<? extends com.google.inject.Provider<org.eclipse.xtext.xbase.file.WorkspaceConfig>> provideWorkspaceConfig() {
						return org.eclipse.xtext.xbase.file.RuntimeWorkspaceConfigProvider.class;
					}
				});
			}
		}.createInjectorAndDoEMFRegistration();
	}
}
