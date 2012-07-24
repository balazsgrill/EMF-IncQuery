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

package org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.construction.psystem;

import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.tuple.Tuple;

/**
 * @author Bergmann Gábor
 *
 */
public abstract class KeyedEnumerablePConstraint<KeyType, PatternDescription, StubHandle> 
	extends EnumerablePConstraint<PatternDescription, StubHandle> 
{

	protected KeyType supplierKey;

	/**
	 * @param variablesTuple
	 * @param buildable
	 * @param supplierKey
	 */
	public KeyedEnumerablePConstraint(PSystem<PatternDescription, StubHandle, ?> pSystem, Tuple variablesTuple, KeyType supplierKey) {
		super(pSystem, variablesTuple);
		this.supplierKey = supplierKey;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.construction.psystem.EnumerablePConstraint#toStringRestRest()
	 */
	@Override
	protected String toStringRestRest() {
		return supplierKey == null ? "$any(null)" : keyToString();
	}

	/**
	 * @return
	 */
	protected abstract String keyToString();

	/**
	 * @return the supplierKey
	 */
	public KeyType getSupplierKey() {
		return supplierKey;
	}
	
	
}
