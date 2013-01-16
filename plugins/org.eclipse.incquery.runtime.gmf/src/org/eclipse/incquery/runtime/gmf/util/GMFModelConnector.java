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
package org.eclipse.incquery.runtime.gmf.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts.DiagramDocumentEditor;
import org.eclipse.incquery.tooling.ui.queryexplorer.content.matcher.MatcherTreeViewerRootKey;
import org.eclipse.incquery.tooling.ui.queryexplorer.handlers.util.EMFModelConnector;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IEditorPart;

/**
 * @author Tamas Szabo
 * 
 */
public class GMFModelConnector extends EMFModelConnector {

    public GMFModelConnector(MatcherTreeViewerRootKey key) {
        super(key);
    }

    @Override
    protected TreePath createTreePath(IEditorPart editor, EObject obj) {
        if (editor instanceof DiagramDocumentEditor) {
            DiagramDocumentEditor providerEditor = (DiagramDocumentEditor) editor;
            EditPart epBegin = providerEditor.getDiagramEditPart().getPrimaryChildEditPart();
            if (epBegin instanceof GraphicalEditPart) {
                List<Object> nodes = new ArrayList<Object>();
                epBegin = ((GraphicalEditPart) epBegin).findEditPart(epBegin.getRoot(), obj);
                if (epBegin != null) {
                    nodes.add(epBegin);
                    return new TreePath(nodes.toArray());
                }
            }
        }
        return null;
    }

    @Override
    protected void navigateToElements(IEditorPart editorPart, IStructuredSelection selection) {
        super.navigateToElements(editorPart, selection);
        if (editorPart instanceof DiagramDocumentEditor) {
            DiagramDocumentEditor providerEditor = (DiagramDocumentEditor) editorPart;
            IDiagramGraphicalViewer viewer = providerEditor.getDiagramGraphicalViewer();
            if (selection.getFirstElement() instanceof GraphicalEditPart) {
                GraphicalEditPart part = (GraphicalEditPart) selection.getFirstElement();
                viewer.reveal(part);
            }
        }
    }
}
