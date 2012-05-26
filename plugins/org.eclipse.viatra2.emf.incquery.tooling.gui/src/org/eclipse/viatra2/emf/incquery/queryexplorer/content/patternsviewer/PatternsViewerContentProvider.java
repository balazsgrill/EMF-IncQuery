package org.eclipse.viatra2.emf.incquery.queryexplorer.content.patternsviewer;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PatternsViewerContentProvider implements ITreeContentProvider {
	
	public PatternsViewerContentProvider() {
		
	}
	
	@Override
	public void dispose() {}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof PatternComposite) {
			return ((PatternComposite) inputElement).getChildren().toArray();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement != null && parentElement instanceof PatternComposite) {
			return ((PatternComposite) parentElement).getChildren().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element != null && element instanceof PatternComponent) {
			return ((PatternComponent) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element != null && element instanceof PatternComposite) {
			return ((PatternComposite) element).getChildren().size() > 0;
		}
		return false;
	}

}