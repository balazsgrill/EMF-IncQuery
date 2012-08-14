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

import static org.junit.Assert.assertEquals;

import org.eclipse.viatra2.emf.incquery.base.itc.alg.fw.FloydWarshallAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.alg.kingopt.KingOptAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.alg.misc.dfs.DFSAlg;
import org.eclipse.viatra2.emf.incquery.base.itc.graphimpl.Graph;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph1;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph2;
import org.eclipse.viatra2.emf.incquery.base.itc.test.graphs.Graph3;
import org.junit.Test;

public class KingOptTestCase {

	@Test
	public void testResult() {
		
		Graph1 g1 = new Graph1();
    	FloydWarshallAlg<Integer> fwa = new FloydWarshallAlg<Integer>(g1);
    	KingOptAlg<Integer> koa = new KingOptAlg<Integer>(g1);
    	g1.detachObserver(koa);
		g1.modify();	
    	
		koa.fullGen();
        assertEquals(koa.getTcRelation(), fwa.getTcRelation());
        
        Graph2 g2 = new Graph2();
    	fwa = new FloydWarshallAlg<Integer>(g2);
    	koa = new KingOptAlg<Integer>(g2);
    	g2.detachObserver(koa);
		g2.modify();	
    	
		koa.fullGen();
		assertEquals(koa.getTcRelation(), fwa.getTcRelation());
        
        Graph3 g3 = new Graph3();
    	fwa = new FloydWarshallAlg<Integer>(g3);
    	koa = new KingOptAlg<Integer>(g3);
    	g3.detachObserver(koa);
		g3.modify();	
    	
		koa.fullGen();
		assertEquals(koa.getTcRelation(), fwa.getTcRelation());
		
		int nodeCount = 10;
		Graph<Integer> g = new Graph<Integer>();
		DFSAlg<Integer> da = new DFSAlg<Integer>(g);
		koa = new KingOptAlg<Integer>(g);
		g.detachObserver(koa);
		
		for (int i = 0; i < nodeCount; i++) {
			g.insertNode(i);
		}

		//System.out.println("insert");
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j) {
					g.insertEdge(i, j);
					koa.fullGen();
					assertEquals(koa.getTcRelation(), da.getTcRelation());
				}
			}
		}

		//System.out.println("delete");
		for (int i = 0; i < nodeCount; i++) {
			for (int j = 0; j < nodeCount; j++) {
				if (i != j) {
					g.deleteEdge(i, j);
					koa.fullGen();
					assertEquals(koa.getTcRelation(), da.getTcRelation());
				}
			}
		}
	}
}