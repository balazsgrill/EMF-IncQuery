/*******************************************************************************
 * Copyright (c) 2004-2010 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.emf.incquery.validation.core;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.viatra2.emf.incquery.runtime.api.IMatcherFactory;
import org.eclipse.viatra2.emf.incquery.runtime.api.IPatternSignature;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryMatcher;

/**
 * @author Bergmann Gábor
 *
 */
public abstract class Constraint<Signature extends IPatternSignature> {
	
	/**
	 * Will be printed to the "message" field of the problem marker.
	 * @return a user-friendly message to be displayed in the problem marker
	 */
	public abstract String getMessage(Signature signature);
//	{
//		return getMessage()+" "+prettyPrintSignature(signature);
//	}
	
	/**
	 * Override!
	 * Will be printed to the location field of the problem marker.
	 * @return an {@link EObject} which is the most important context of the validation
	 */
	public abstract EObject getLocationObject(Signature signature);
//	{
//		return null;
//	}
	
	/**
	 * Initialize the matcher factory used for pattern matching.
	 * @return
	 */
	public abstract IMatcherFactory<Signature, ? extends IncQueryMatcher<Signature>> matcherFactory();
		
	/**
	 * Override if needed!
	 */
	public String prettyPrintSignature(Signature signature) {
		return signature.prettyPrint();
	}
	/**
	 * Override if needed!
	 */
	public Object[] extractAffectedElements(Signature signature) {
		return signature.toArray();
	}
	
	protected Constraint() {
	}
	
}
