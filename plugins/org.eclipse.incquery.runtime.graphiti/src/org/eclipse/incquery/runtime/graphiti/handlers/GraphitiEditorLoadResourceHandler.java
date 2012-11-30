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

package org.eclipse.incquery.runtime.graphiti.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.incquery.runtime.graphiti.util.GraphitiModelConnector;
import org.eclipse.incquery.tooling.ui.queryexplorer.QueryExplorer;
import org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher.MatcherTreeViewerRootKey;
import org.eclipse.incquery.tooling.ui.queryexplorer.handlers.LoadModelHandler;
import org.eclipse.incquery.tooling.ui.queryexplorer.handlers.util.ModelConnector;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class GraphitiEditorLoadResourceHandler extends LoadModelHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

        if (editorPart instanceof DiagramEditor) {
            DiagramEditor providerEditor = (DiagramEditor) editorPart;

            PictogramElement[] selectedElements = providerEditor.getSelectedPictogramElements();

            if (selectedElements.length > 0) {
                PictogramElement element = selectedElements[0];
                Resource resource = Graphiti.getLinkService().getBusinessObjectForLinkedPictogramElement(element)
                        .eResource();
                MatcherTreeViewerRootKey key = new MatcherTreeViewerRootKey(providerEditor, resource);
                ModelConnector contentModel = new GraphitiModelConnector(key);
                QueryExplorer.getInstance().getModelConnectorMap().put(key, contentModel);
                contentModel.loadModel();
            }
        }
        return null;
    }

}
