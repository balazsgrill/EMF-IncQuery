/*******************************************************************************
 * Copyright (c) 2010-2012, Tamas Szabo, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tamas Szabo - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.emf.incquery.base.itc.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.viatra2.emf.incquery.base.itc.alg.fw.FloydWarshallAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.alg.incscc.IncSCCAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.alg.misc.dfs.DFSAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.graphimpl.Graph;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph1;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph2;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph3;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph4;
import org.junit.Test;

public class IncSCCTestCase {

	@Test
	public void testResult() {
		
		Graph1 g1 = new Graph1();
    	FloydWarshallAlg<Integer> fwa = new FloydWarshallAlg<Integer>(g1);
    	IncSCCAlg<Integer> isa = new IncSCCAlg<Integer>(g1);
		g1.modify();	
		assertTrue(isa.checkTcRelation(fwa.getTcRelation()));
        
        Graph2 g2 = new Graph2();
    	fwa = new FloydWarshallAlg<Integer>(g2);
    	isa = new IncSCCAlg<Integer>(g2);
		g2.modify();	
		assertTrue(isa.checkTcRelation(fwa.getTcRelation()));
        
        Graph3 g3 = new Graph3();
    	fwa = new FloydWarshallAlg<Integer>(g3);
    	isa = new IncSCCAlg<Integer>(g3);
		g3.modify();	
		assertTrue(isa.checkTcRelation(fwa.getTcRelation()));
		
		Graph4 g4 = new Graph4();
    	fwa = new FloydWarshallAlg<Integer>(g4);
    	isa = new IncSCCAlg<Integer>(g4);
		g4.modify();	
		assertTrue(isa.checkTcRelation(fwa.getTcRelation()));
		
		int nodeCount = 10;
		Graph<Integer> g = new Graph<Integer>();
		DFSAlg<Integer> da = new DFSAlg<Integer>(g);
		isa = new IncSCCAlg<Integer>(g);
		
		for (int i = 0; i < nodeCount; i++) {
			g.insertNode(i);
		}

		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j) {
					g.insertEdge(i, j);
					assertTrue(isa.checkTcRelation(da.getTcRelation()));
				}
			}
		}

		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j) {
					g.deleteEdge(i, j);
					assertTrue(isa.checkTcRelation(da.getTcRelation()));
				}
			}
		}
	}
}