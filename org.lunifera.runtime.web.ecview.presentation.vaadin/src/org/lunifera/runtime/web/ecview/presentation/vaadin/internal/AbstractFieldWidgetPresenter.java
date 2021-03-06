/**
 * Copyright (c) 2012 Lunifera GmbH (Austria) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Florian Pirchner - initial API and implementation
 */
package org.lunifera.runtime.web.ecview.presentation.vaadin.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecp.ecview.common.editpart.IEmbeddableEditpart;
import org.eclipse.emf.ecp.ecview.common.presentation.IFieldPresentation;
import org.eclipse.emf.ecp.ecview.common.validation.IValidator;

import com.vaadin.data.Validator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

/**
 * An abstract implementation of the {@link IFieldPresentation}.
 */
public abstract class AbstractFieldWidgetPresenter<A extends Component> extends
		AbstractVaadinWidgetPresenter<A> implements IFieldPresentation<A> {

	private Map<IValidator, ValidatorAdapter> validators;

	public AbstractFieldWidgetPresenter(IEmbeddableEditpart editpart) {
		super(editpart);
	}

	@Override
	public void addValidator(IValidator validator) {
		if (validators == null) {
			validators = new HashMap<IValidator, ValidatorAdapter>(1);
		}

		if (!validators.containsKey(validator)) {
			validators.put(validator, new ValidatorAdapter(validator));
		}

		doUpdateValidator(new Event(Event.ADD, validators.get(validator)));
	}

	@Override
	public void removeValidator(IValidator validator) {
		if (validators == null) {
			return;
		}

		ValidatorAdapter adapter = validators.remove(validator);
		if (adapter != null) {
			doUpdateValidator(new Event(Event.REMOVE, adapter));
		}
	}

	public Map<IValidator, ValidatorAdapter> getValidators() {
		return validators != null ? java.util.Collections
				.unmodifiableMap(validators) : Collections
				.<IValidator, ValidatorAdapter> emptyMap();
	}

	/**
	 * Should be implemented by subclasses to update their validator.
	 * 
	 * @param event
	 *            - the event
	 */
	protected void doUpdateValidator(Event event) {
		Field<?> field = doGetField();
		if (field != null) {
			switch (event.getType()) {
			case Event.ADD:
				field.addValidator((Validator) event.getValidator());
				break;
			case Event.REMOVE:
				field.removeValidator((Validator) event.getValidator());
				break;
			}
		}
	}

	/**
	 * Is called by subclasses to attach all available validators at the given
	 * field.
	 * 
	 * @param field
	 *            - the field all validators should be added to
	 */
	protected void attachValidators(Field<?> field) {
		if (validators == null) {
			return;
		}

		for (Validator validator : validators.values()) {
			field.addValidator(validator);
		}
	}

	/**
	 * Is called by subclasses to detach all validators that are registered at
	 * this instance.
	 * 
	 * @param field
	 *            - the field all validators should be added to
	 */
	protected void detachValidators(Field<?> field) {
		if (validators == null) {
			return;
		}

		for (Validator validator : validators.values()) {
			field.removeValidator(validator);
		}
	}

	/**
	 * Returns the field of the current presentation. May return
	 * <code>null</code>. Should be implemented by sub classes.
	 * 
	 * @return
	 */
	protected abstract Field<?> doGetField();

	/**
	 * Called by subclasses to initialize the field with validators.
	 * 
	 * @param field
	 *            - the field to be initialized.
	 */
	protected void initializeField(Field<?> field) {
		attachValidators(field);
	}

}
