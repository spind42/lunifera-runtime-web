/**
 * Copyright (c) 2013 COMPEX Systemhaus GmbH Heidelberg. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Jose C. Dominguez - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.tests.presentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.eclipse.emf.ecp.ecview.common.context.ContextException;
import org.eclipse.emf.ecp.ecview.common.editpart.DelegatingEditPartManager;
import org.eclipse.emf.ecp.ecview.common.editpart.IElementEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.editpart.IViewEditpart;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBeanBindingEndpoint;
import org.eclipse.emf.ecp.ecview.common.model.binding.YBindingSet;
import org.eclipse.emf.ecp.ecview.common.model.core.YElement;
import org.eclipse.emf.ecp.ecview.common.model.core.YView;
import org.eclipse.emf.ecp.ecview.common.presentation.IWidgetPresentation;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YGridLayout;
import org.eclipse.emf.ecp.ecview.extension.model.extension.YTextArea;
import org.eclipse.emf.ecp.ecview.extension.model.extension.util.SimpleExtensionModelFactory;
import org.eclipse.emf.ecp.ecview.ui.core.editparts.extension.ITextAreaEditpart;
import org.junit.Before;
import org.junit.Test;
import org.lunifera.runtime.web.ecview.presentation.vaadin.VaadinRenderer;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.AbstractVaadinWidgetPresenter;
import org.lunifera.runtime.web.ecview.presentation.vaadin.internal.TextAreaPresentation;
import org.lunifera.runtime.web.ecview.presentation.vaadin.tests.model.ValueBean;
import org.osgi.framework.BundleException;
import org.osgi.service.cm.ConfigurationException;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;

/**
 * Tests the {@link TextAreaPresentation}.
 */
@SuppressWarnings("restriction")
public class TextAreaPresentationTests {

	private SimpleExtensionModelFactory factory = new SimpleExtensionModelFactory();
	private CssLayout rootLayout = new CssLayout();

	/**
	 * Setup tests.
	 * 
	 * @throws ConfigurationException
	 * @throws BundleException
	 */
	@Before
	public void setup() throws ConfigurationException, BundleException {
		UI.setCurrent(new DefaultUI());
		UI.getCurrent().setContent(rootLayout);
	}

	/**
	 * Tests rendering issues.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_isRendered_unrender_byModel() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yGridLayout
		// .........> yText
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextArea yTextArea = factory.createTextArea();
		yGridlayout.getElements().add(yTextArea);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textAreaEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea);
		IWidgetPresentation<Component> presentation = textAreaEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());

		yGridlayout.getElements().remove(yTextArea);
		assertFalse(presentation.isRendered());
		assertFalse(presentation.isDisposed());
	}

	/**
	 * Tests the internal structure.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_InternalStructure() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YTextArea yTextArea = factory.createTextArea();
		yView.setContent(yTextArea);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textAreaEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea);
		IWidgetPresentation<Component> presentation = textAreaEditpart
				.getPresentation();
		ComponentContainer baseComponentContainer = (ComponentContainer) presentation
				.getWidget();

		TextArea textArea = (TextArea) unwrapText(baseComponentContainer);
		assertEquals(1, baseComponentContainer.getComponentCount());

		// assert layout
		CssLayout layout = (CssLayout) baseComponentContainer;
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_InternalStructure__CSS() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTextArea yTextArea1 = factory.createTextArea();
		yTextArea1.setCssID("ID_0815");
		yTextArea1.setCssClass("anyOtherClass");
		yLayout.getElements().add(yTextArea1);
		YTextArea yTextArea2 = factory.createTextArea();
		yLayout.getElements().add(yTextArea2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textArea1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea1);
		ITextAreaEditpart textArea2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea2);
		IWidgetPresentation<Component> textArea1Presentation = textArea1Editpart
				.getPresentation();
		IWidgetPresentation<Component> textArea2Presentation = textArea2Editpart
				.getPresentation();
		ComponentContainer textArea1BaseComponentContainer = (ComponentContainer) textArea1Presentation
				.getWidget();
		ComponentContainer textArea2BaseComponentContainer = (ComponentContainer) textArea2Presentation
				.getWidget();

		TextArea textArea1 = (TextArea) unwrapText(textArea1BaseComponentContainer);
		TextArea textArea2 = (TextArea) unwrapText(textArea2BaseComponentContainer);

		// assert css class
		assertTrue(textArea1BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));
		assertTrue(textArea2BaseComponentContainer.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL_BASE));

		assertTrue(textArea1.getStyleName().contains("anyOtherClass"));
		assertTrue(textArea2.getStyleName().contains(
				AbstractVaadinWidgetPresenter.CSS_CLASS__CONTROL));

		// assert css id
		assertEquals("ID_0815", textArea1BaseComponentContainer.getId());
		assertNull(textArea1.getId());
		assertEquals(textArea2Editpart.getId(),
				textArea2BaseComponentContainer.getId());
		assertNull(textArea2.getId());
	}

	/**
	 * Test the internal structure based on CSS.
	 * 
	 * @throws Exception
	 */
	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_Bindings() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTextArea yTextArea1 = factory.createTextArea();
		yLayout.getElements().add(yTextArea1);
		YTextArea yTextArea2 = factory.createTextArea();
		yLayout.getElements().add(yTextArea2);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textArea1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea1);
		ITextAreaEditpart textArea2Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea2);
		IWidgetPresentation<Component> textArea1Presentation = textArea1Editpart
				.getPresentation();
		IWidgetPresentation<Component> textArea2Presentation = textArea2Editpart
				.getPresentation();
		ComponentContainer textArea1BaseComponentContainer = (ComponentContainer) textArea1Presentation
				.getWidget();
		ComponentContainer textArea2BaseComponentContainer = (ComponentContainer) textArea2Presentation
				.getWidget();
		TextArea textArea1 = (TextArea) unwrapText(textArea1BaseComponentContainer);
		TextArea textArea2 = (TextArea) unwrapText(textArea2BaseComponentContainer);

		// start tests
		//
		assertTrue(textArea1.isVisible());
		assertTrue(textArea1.isEnabled());
		assertFalse(textArea1.isReadOnly());

		assertTrue(textArea2.isVisible());
		assertTrue(textArea2.isEnabled());
		assertFalse(textArea2.isReadOnly());

		yTextArea1.setVisible(false);
		assertFalse(textArea1.isVisible());

		yTextArea1.setEnabled(false);
		assertFalse(textArea1.isEnabled());

		yTextArea1.setEditable(false);
		assertTrue(textArea1.isReadOnly());

		// target to model
		textArea1.setReadOnly(false);
		assertTrue(yTextArea1.isEditable());

	}

	@Test
	// BEGIN SUPRESS CATCH EXCEPTION
	public void test_ValueBinding() throws Exception {
		// END SUPRESS CATCH EXCEPTION
		// build the view model
		// ...> yView
		// ......> yText
		YView yView = factory.createView();
		YGridLayout yLayout = factory.createGridLayout();
		yView.setContent(yLayout);
		YTextArea yText1 = factory.createTextArea();
		yLayout.getElements().add(yText1);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart text1Editpart = DelegatingEditPartManager
				.getInstance().getEditpart(yText1);
		IWidgetPresentation<Component> text1Presentation = text1Editpart
				.getPresentation();
		ComponentContainer text1BaseComponentContainer = (ComponentContainer) text1Presentation
				.getWidget();
		TextArea text1 = (TextArea) unwrapText(text1BaseComponentContainer);

		// start tests
		//
		YBindingSet yBindingSet = yView.getOrCreateBindingSet();

		yText1.setValue("");
		YBeanBindingEndpoint beanBinding = factory.createBeanBindingEndpoint();
		ValueBean bean = new ValueBean("Huhu");
		beanBinding.setPropertyPath("value");
		beanBinding.setBean(bean);
		yBindingSet.addBinding(yText1.createValueEndpoint(), beanBinding);
		assertEquals("Huhu", text1.getValue());
		assertEquals("Huhu", yText1.getValue());

		// bean = new ValueBean("Huhu11");
		// beanBinding.setPropertyPath("value");
		// TODO Setting a bean later does not cause any sideeffects. See
		// BeanBindingEndpointEditpart. The binding for the bean is not
		// refreshed.
		// beanBinding.setBean(bean);
		// assertEquals("Huhu11", text1.getValue());
		// assertEquals("Huhu11", yText1.getValue());

		bean.setValue("Haha");
		assertEquals("Haha", text1.getValue());
		assertEquals("Haha", yText1.getValue());

		text1.setValue("Haha1");
		assertEquals("Haha1", bean.getValue());
		assertEquals("Haha1", yText1.getValue());

		yText1.setValue("Haha2");
		assertEquals("Haha2", bean.getValue());
		assertEquals("Haha2", text1.getValue());
	}

	/**
	 * Test the automatic disposal of bindings
	 * 
	 * @throws ContextException
	 */
	@Test
	public void testBindingIsDisposed() throws ContextException {
		YView yView = factory.createView();
		YGridLayout yGridlayout = factory.createGridLayout();
		yView.setContent(yGridlayout);
		YTextArea yTextArea = factory.createTextArea();
		yGridlayout.getElements().add(yTextArea);

		VaadinRenderer renderer = new VaadinRenderer();
		renderer.render(rootLayout, yView, null);

		ITextAreaEditpart textAreaEditpart = DelegatingEditPartManager
				.getInstance().getEditpart(yTextArea);
		IWidgetPresentation<Component> presentation = textAreaEditpart
				.getPresentation();
		assertTrue(presentation.isRendered());
		assertFalse(presentation.isDisposed());
		assertEquals(4, presentation.getUIBindings().size());

		presentation.dispose();
		assertFalse(presentation.isRendered());
		assertTrue(presentation.isDisposed());
		assertEquals(0, presentation.getUIBindings().size());
	}

	/**
	 * Unwraps the component from its parent composite.
	 * 
	 * @param component
	 * @return
	 */
	private Component unwrapText(Component component) {
		if (component instanceof ComponentContainer) {
			ComponentContainer composite = (ComponentContainer) component;
			Iterator<Component> iter = composite.iterator();
			return iter.next();
		}
		return component;
	}

	/**
	 * Returns the component for the given model element.
	 * 
	 * @param yView
	 *            model element
	 * @return component
	 */
	@SuppressWarnings("unchecked")
	protected Component getComponent(YElement yView) {
		IElementEditpart editpart = DelegatingEditPartManager.getInstance()
				.getEditpart(yView);

		IWidgetPresentation<Component> presentation = null;
		if (editpart instanceof IViewEditpart) {
			presentation = (IWidgetPresentation<Component>) ((IViewEditpart) editpart)
					.getPresentation();
		} else {
			presentation = ((IEmbeddableEditpart) editpart).getPresentation();
		}
		Component widget = presentation.getWidget();
		return widget;
	}
}
