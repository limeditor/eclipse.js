package net.jeeeyul.eclipsejs.ui;

import net.jeeeyul.eclipsejs.EclipseJSCore;
import net.jeeeyul.eclipsejs.IRuntimeProjectCallback;
import net.jeeeyul.eclipsejs.core.EJSContextFactory;
import net.jeeeyul.eclipsejs.core.Require;
import net.jeeeyul.eclipsejs.core.ScopeFactory;
import net.jeeeyul.eclipsejs.ui.ResourceRegistry.Factory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;

public class CommonView extends ViewPart {
	public static final String ID = "net.jeeeyul.eclipsejs.common.view";

	private PageBook pageBook;
	private Composite viewPage;
	private Context context;
	private ScriptableObject scope;
	private boolean isDisposed = false;

	private ResourceRegistry<String, Image> imageRegistry;

	public CommonView() {
		imageRegistry = new ResourceRegistry<String, Image>();
		imageRegistry.setFactory(new Factory<String, Image>() {
			@Override
			public Image create(String key) {
				try {
					IPath viewFolder = getViewFilePath().removeLastSegments(1);
					IPath imagePath = viewFolder.append(key);
					IFile imageFile = ResourcesPlugin.getWorkspace().getRoot()
							.getFile(imagePath);
					Image image;
					image = new Image(Display.getDefault(), imageFile
							.getContents());
					return image;
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	private Object callViewFunction(String name, Object... args) {
		ScriptableObject view = (ScriptableObject) ScriptableObject
				.getProperty(scope, "view");
		try {
			Object fn = ScriptableObject.getProperty(view, name);
			if (fn instanceof Function) {
				return ((Function) fn).call(context, view, view, args);
			} else {
				return null;
			}
		} catch (RhinoException e) {
			ScriptErrorPresenter.INSTANCE.showError(e);
			return null;
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NORMAL);
		Label loadingLabel = new Label(pageBook, SWT.NORMAL);
		loadingLabel.setText("Preparing Eclipse.JS Runtime...");
		pageBook.showPage(loadingLabel);

		EclipseJSCore.getDefault().getRuntimeProject(
				new IRuntimeProjectCallback() {
					@Override
					public void projectPrepared(IProject project) {
						doCreate(project);
					}
				});
	}

	protected void doCreate(final IProject project) {
		if (Thread.currentThread() != Display.getDefault().getThread()) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					doCreate(project);
				}
			});
			return;
		}

		if (pageBook == null || pageBook.isDisposed()) {
			return;
		}

		viewPage = new Composite(pageBook, SWT.NORMAL);
		viewPage.setLayout(new FillLayout());
		callViewFunction("create", viewPage);
		pageBook.showPage(viewPage);

	}

	private void doInit(final IViewSite site) {
		if (isDisposed) {
			return;
		}

		if (Thread.currentThread() != Display.getDefault().getThread()) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					doInit(site);
				}
			});
			return;
		}

		context = EJSContextFactory.getSharedContext();
		String viewId = getViewId();
		IPath viewFilePath = getViewFilePath();

		scope = ScopeFactory.getInstance().create(viewFilePath);

		context.evaluateString(scope, "var view = require('/"
				+ EclipseJSCore.PROJECT_NAME + "/extensions/views/" + viewId
				+ "')", "view-" + viewId, 1, null);

		callViewFunction("init", site);

		Object partName = callViewFunction("getPartName");
		if (partName != null) {
			setPartName((String) Context.jsToJava(partName, String.class));
			firePropertyChange(PROP_TITLE);
		}

		String imagePath = (String) Context.jsToJava(
				callViewFunction("getIcon"), String.class);
		if (imagePath != null) {
			Image image = imageRegistry.get(imagePath);
			setTitleImage(image);
		}

	}

	private IPath getViewFilePath() {
		String fullPath = new Require(new Path("/" + EclipseJSCore.PROJECT_NAME
				+ "/extensions/views"), null).resolveQualifiedName("./"
				+ getViewId());
		return new Path(fullPath);
	}

	private String getViewId() {
		String viewId = getViewSite().getSecondaryId();
		return viewId;
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		EclipseJSCore.getDefault().getRuntimeProject(
				new IRuntimeProjectCallback() {
					@Override
					public void projectPrepared(IProject project) {
						doInit(getViewSite());
					}
				});
	}

	@Override
	public void setFocus() {
		pageBook.setFocus();
	}

	@Override
	public void dispose() {
		isDisposed = true;
		callViewFunction("dispose");
		imageRegistry.dispose();
		super.dispose();
	}

}
