package org.lunifera.runtime.web.ecview.presentation.vaadin.internal.binding;

import org.eclipse.emf.ecp.ecview.common.context.IContext;
import org.eclipse.emf.ecp.ecview.common.context.IViewContext;
import org.eclipse.emf.ecp.ecview.common.disposal.IDisposable;
import org.eclipse.emf.ecp.ecview.common.services.IServiceProvider;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.vaadin.databinding.VaadinObservables;

import com.vaadin.ui.Component;

public class BindingManagerProvider implements IServiceProvider {

	@Override
	public boolean isFor(String selector, IContext context) {
		if (context instanceof IViewContext) {
			if (!org.eclipse.emf.ecp.ecview.common.binding.IECViewBindingManager.class
					.getName().equals(selector)) {
				return false;
			}
			IViewContext viewContext = (IViewContext) context;
			if (!VaadinRenderer.UI_KIT_URI.equals(viewContext
					.getPresentationURI())) {
				return false;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A> A createService(String selector, IContext context) {
		IViewContext viewContext = (IViewContext) context;
		final BindingManager bindingManager = new BindingManager(viewContext,
				VaadinObservables.getRealm(VaadinObservables
						.getUI((Component) viewContext.getRootLayout())));
		viewContext.addDisposeListener(new IDisposable.Listener() {
			@Override
			public void notifyDisposed(IDisposable notifier) {
				bindingManager.dispose();
			}
		});
		return (A) bindingManager;
	}

}
