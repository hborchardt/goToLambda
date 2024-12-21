package gotolambda.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

import gotolambda.actions.GoToLambdaAction;

public class GoToLambdaDefinition extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			new GoToLambdaAction().openElement(null, getCurrentSelection().getFirstElement());
		} catch (CoreException e) {
			JDIDebugUIPlugin.statusDialog(e.getStatus());
		}
		return null;
	}

	/**
	 * Returns the currently selected item(s) from the current workbench page or
	 * <code>null</code> if the current active page could not be resolved.
	 *
	 * @return the currently selected item(s) or <code>null</code>
	 */
	protected IStructuredSelection getCurrentSelection() {
		IWorkbenchPage page = JDIDebugUIPlugin.getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				return (IStructuredSelection) selection;
			}
		}
		return null;
	}
}
