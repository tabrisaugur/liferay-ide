/*******************************************************************************
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 *******************************************************************************/

package com.liferay.ide.eclipse.layouttpl.ui.parts;

import com.liferay.ide.eclipse.layouttpl.ui.model.PortletColumn;
import com.liferay.ide.eclipse.layouttpl.ui.policies.PortletColumnComponentEditPolicy;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;


public class PortletColumnTreeEditPart extends BaseTreeEditPart {


	public PortletColumnTreeEditPart(PortletColumn model) {
		super(model);
	}


	protected void createEditPolicies() {
		// allow removal of the associated model element
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PortletColumnComponentEditPolicy());
	}


	private PortletColumn getCastedModel() {
		return (PortletColumn) getModel();
	}


	protected Image getImage() {
		return getCastedModel().getIcon();
	}


	protected String getText() {
		return getCastedModel().toString();
	}


	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}
}