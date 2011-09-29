/*******************************************************************************
 * Copyright (c) 2000-2011 Accenture Services Pvt. Ltd., All rights reserved.
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
 * Contributors:
 *    Kamesh Sampath - initial implementation
 ******************************************************************************/

package com.liferay.ide.eclipse.pml.core.model.internal;

import com.liferay.ide.eclipse.pml.core.model.IOrganization;
import com.liferay.ide.eclipse.pml.core.model.OrganizationType;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelPropertyValidationService;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
public class OraganizationTypeValidationService extends ModelPropertyValidationService<Value<OrganizationType>> {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.ModelPropertyValidationService#validate()
	 */
	@Override
	public Status validate() {
		Value<OrganizationType> orgType = target();
		OrganizationType eorgType = orgType.getContent();
		IModelElement root = element();
		if ( OrganizationType.REGULAR_ORGANIZATION.equals( eorgType ) ) {
			root.write( IOrganization.PROP_COUNTRY, null );
			root.write( IOrganization.PROP_REGION, null );
		}
		return Status.createOkStatus();
	}
}
