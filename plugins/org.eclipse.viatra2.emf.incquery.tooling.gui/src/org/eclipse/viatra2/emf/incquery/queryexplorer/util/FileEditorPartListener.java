package org.eclipse.viatra2.emf.incquery.queryexplorer.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.viatra2.emf.incquery.queryexplorer.QueryExplorer;
import org.eclipse.viatra2.emf.incquery.queryexplorer.handlers.RuntimeMatcherUnRegistrator;

/**
 * The PartListener is used to observe EditorPart close actions.
 * 
 * @author Tamas Szabo
 *
 */
public class FileEditorPartListener implements IPartListener {
	
	private String dialogTitle = ".eiq file editor closing";
	
	@Override
	public void partActivated(IWorkbenchPart part) {

	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {

	}

	@Override
	public void partClosed(IWorkbenchPart part) {
//		if (part != null && part instanceof IEditorPart) {
//			IEditorPart closedEditor = (IEditorPart) part;
//			
//			closedEditor.getSite().getPage().removePartListener(QueryExplorer.getInstance().getFilePartListener());
//			IEditorInput editorInput = closedEditor.getEditorInput();
//			IFile file = null;
//			String question = "";
//			
//			if (editorInput instanceof FileEditorInput) {
//				file = ((FileEditorInput) editorInput).getFile();
//				question = "There are patterns (from file named '"+file.getName()+"') registered in the Query Explorer.\nWould you like to unregister them?";
//				boolean answer = MessageDialog.openQuestion(closedEditor.getSite().getShell(), dialogTitle, question);
//				if (answer) {
//					RuntimeMatcherUnRegistrator job = new RuntimeMatcherUnRegistrator(closedEditor, file);
//					job.run();
//				}
//			}
//		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {

	}

	@Override
	public void partOpened(IWorkbenchPart part) {

	}
}
