package org.eclipse.viatra2.patternlanguage.core.annotations;

import java.util.Arrays;
import java.util.Hashtable;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.viatra2.patternlanguage.core.annotations.impl.ExtensionBasedPatternAnnotationParameter;
import org.eclipse.viatra2.patternlanguage.core.annotations.impl.ExtensionBasedPatternAnnotationValidator;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class PatternAnnotationProvider {

	static final String EXTENSIONID = "org.eclipse.viatra2.patternlanguage.core.annotation";
	Hashtable<String, IPatternAnnotationValidator> annotationValidators;

	protected void initializeValidators() {
		annotationValidators = new Hashtable<String, IPatternAnnotationValidator>();
		final IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(EXTENSIONID);
		for (IConfigurationElement e : config) {
			final String annotationName = e.getAttribute("name");
			final String languageName = e.getAttribute("language");

			final IConfigurationElement[] parameters = e
					.getChildren("annotationparameter");
			final Iterable<ExtensionBasedPatternAnnotationParameter> parameterIterable = Iterables
					.transform(
							Arrays.asList(parameters),
							new Function<IConfigurationElement, ExtensionBasedPatternAnnotationParameter>() {

								@Override
								public ExtensionBasedPatternAnnotationParameter apply(
										IConfigurationElement input) {
									final String parameterName = input
											.getAttribute("name");
									final boolean mandatory = Boolean
											.parseBoolean(input
													.getAttribute("mandatory"));
									final boolean multiple = Boolean
											.parseBoolean(input
													.getAttribute("multiple"));
									final String type = input
											.getAttribute("type");
									return new ExtensionBasedPatternAnnotationParameter(
											parameterName, type, multiple,
											mandatory);
								}
							});
			final IPatternAnnotationValidator annotationValidator = new ExtensionBasedPatternAnnotationValidator(annotationName, languageName, parameterIterable);
			annotationValidators.put(annotationName, annotationValidator);
		}
	}

	/**
	 * Returns a pattern annotation validator for a selected annotation name
	 * 
	 * @param annotationName
	 * @return a pattern annotation validator
	 */
	public IPatternAnnotationValidator getValidator(String annotationName) {
		if (annotationValidators == null) {
			initializeValidators();
		}
		return annotationValidators.get(annotationName);
	}

	/**
	 * Decides whether a validator is defined for the selected annotation name.
	 * 
	 * @param annotationName
	 * @return true, if a validator is defined
	 */
	public boolean hasValidator(String annotationName) {
		if (annotationValidators == null) {
			initializeValidators();
		}
		return annotationValidators.containsKey(annotationName);
	}
}
