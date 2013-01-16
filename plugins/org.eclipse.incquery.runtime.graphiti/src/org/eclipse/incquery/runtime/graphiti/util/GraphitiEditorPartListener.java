/*******************************************************************************
 * Copyright (c) 2010-2012, Zoltan Ujhelyi, Abel Hegedus, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zoltan Ujhelyi, Abel Hegedus - initial API and implementation
 *******************************************************************************/

package org.eclipse.incquery.runtime.graphiti.util;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.incquery.tooling.ui.queryexplorer.QueryExplorer;
import org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher.MatcherTreeViewerRootKey;
import org.eclipse.incquery.tooling.ui.queryexplorer.util.BasePartListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The PartListener is used to observe EditorPart close actions on Graphiti editors.
 * 
 * @author Tamas Szabo
 * 
 */
public class GraphitiEditorPartListener extends BasePartListener {

    private static GraphitiEditorPartListener instance;

    protected GraphitiEditorPartListener() {

    }

    public static synchronized GraphitiEditorPartListener getInstance() {
        if (instance == null) {
            instance = new GraphitiEditorPartListener();
        }
        return instance;
    }

    @Override
    public void partClosed(IWorkbenchPart part) {

        if (part instanceof IEditorPart) {
            IEditorPart closedEditor = (IEditorPart) part;
            if (closedEditor instanceof DiagramEditor) {
                DiagramEditor providerEditor = (DiagramEditor) closedEditor;
                ResourceSet resourceSet = providerEditor.getEditingDomain().getResourceSet();
                if (resourceSet.getResources().size() > 0) {
                    MatcherTreeViewerRootKey key = new MatcherTreeViewerRootKey(providerEditor, resourceSet);
                    QueryExplorer.getInstance().getModelConnectorMap().get(key).unloadModel();
                }
            }
        }

    }
}
