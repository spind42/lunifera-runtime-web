/*******************************************************************************
 * Copyright (c) 2012 by committers of lunifera.org

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Based on ideas of org.eclipse.jface.databinding.swt (EPL)
 * 
 * Contributor:
 * 		Florian Pirchner - porting swt databinding to support vaadin
 * 
 *******************************************************************************/
package org.lunifera.runtime.web.vaadin.databinding.model.internal;

import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.set.SetDiff;
import org.eclipse.core.databinding.property.IProperty;
import org.eclipse.core.databinding.property.ISimplePropertyListener;
import org.eclipse.core.databinding.property.NativePropertyListener;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Viewer;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeNotifier;
import com.vaadin.ui.AbstractSelect;

/**
 */
@SuppressWarnings("serial")
public class MultiSelectionSetChangeListener extends NativePropertyListener
		implements Property.ValueChangeListener {

	private Set<Object> oldItems;
	private Object source;

	public MultiSelectionSetChangeListener(IProperty property,
			ISimplePropertyListener listener) {
		super(property, listener);
	}

	protected void doAddTo(Object source) {
		this.source = source;
		getNotifier(source).addValueChangeListener(this);

		cacheOldValues(source);
	}

	@SuppressWarnings("unchecked")
	protected void cacheOldValues(Object source) {
		oldItems = (Set<Object>) getWidget(source).getValue();
	}

	protected Property.ValueChangeNotifier getNotifier(Object source) {
		return (ValueChangeNotifier) source;
	}

	protected AbstractSelect getWidget(Object source) {
		return (AbstractSelect) source;
	}

	protected Container getContainer(Object source) {
		Container.Viewer viewer = (Viewer) source;
		Container ds = viewer.getContainerDataSource();
		return ds;
	}

	protected void doRemoveFrom(Object source) {
		getNotifier(source).removeValueChangeListener(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void valueChange(ValueChangeEvent event) {
		SetDiff diffs = Diffs.computeSetDiff(oldItems, (Set) event
				.getProperty().getValue());
		cacheOldValues(source);

		fireChange(source, diffs);
	}
}