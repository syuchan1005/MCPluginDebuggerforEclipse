package com.github.syuchan1005.mcplugindebugger.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.SWT;
import javax.inject.Inject;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class DebugConsoleViewFactory extends ViewPart {
	public static final String ID = "com.github.syuchan1005.mcplugindebugger.views.DebugConsoleViewFactory";

	@Inject IWorkbench workbench;
	
	@Override
	public void createPartControl(Composite parent) {
		new DebugConsoleView(parent, SWT.NULL);
	}

	@Override
	public void setFocus() {}
}
