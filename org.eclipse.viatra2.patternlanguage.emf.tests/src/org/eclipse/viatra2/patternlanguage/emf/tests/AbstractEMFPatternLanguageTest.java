package org.eclipse.viatra2.patternlanguage.emf.tests;

import java.util.Collection;

import org.eclipse.viatra2.patternlanguage.EMFPatternLanguageInjectorProvider;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipselabs.xtext.utils.unittesting.XtextRunner2;
import org.eclipselabs.xtext.utils.unittesting.XtextTest;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableSet;

@RunWith(XtextRunner2.class)
@InjectWith(EMFPatternLanguageInjectorProvider.class)
public abstract class AbstractEMFPatternLanguageTest extends XtextTest {

	static final ImmutableSet<String> defaultPackages = ImmutableSet
			.of("http://www.eclipse.org/viatra2/patternlanguage/core/PatternLanguage");

	public String addImports(String content, Collection<String> packages) {
		StringBuilder sb = new StringBuilder();
		for (String pack : packages) {
			sb.append("import \"" + pack + "\"\n");
		}
		sb.append(content);
		return sb.toString();
	}

	public String addDefaultImports(String content) {
		return addImports(content, defaultPackages);
	}
}
