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

package com.liferay.ide.eclipse.pml.core.model;

import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Image;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlRootBinding;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@GenerateImpl
@XmlRootBinding( elementName = "portal",namespace="http://www.liferay.com/pml/1.0" )
@Image( path = "images/elcl16/portal_16x16.png" )
public interface IPortal extends ICommonProperties {

	ModelElementType TYPE = new ModelElementType( IPortal.class );

	// *** Portal Admin User ***

	@Label( standard = "Portal Admin User" )
	@XmlBinding( path = "portal-admin-user" )
	ValueProperty PROP_PORTAL_ADMIN_USER = new ValueProperty( TYPE, "PortalAdminUser" );

	Value<String> getPortalAdminUser();

	void setPortalAdminUser( String value );

	// *** PortalAdminUserPassword ***

	@Label( standard = "Portal Admin Password" )
	@XmlBinding( path = "portal-admin-user-pwd" )
	ValueProperty PROP_PORTAL_ADMIN_USER_PASSWORD = new ValueProperty( TYPE, "PortalAdminUserPassword" );

	Value<String> getPortalAdminUserPassword();

	void setPortalAdminUserPassword( String value );

	// *** Organizations ***

	@Type( base = IOrganization.class )
	@Label( standard = "Organizations" )
	@XmlListBinding( path = "organizations", mappings = { @XmlListBinding.Mapping( element = "organization", type = IOrganization.class ) } )
	ListProperty PROP_ORGANIZATIONS = new ListProperty( TYPE, "Organizations" );

	ModelElementList<IOrganization> getOrganizations();

	// *** Sites ***

	@Type( base = ISite.class )
	@Label( standard = "Sites" )
	@XmlListBinding( path = "sites", mappings = { @XmlListBinding.Mapping( element = "site", type = ISite.class ) } )
	ListProperty PROP_SITES = new ListProperty( TYPE, "Sites" );

	ModelElementList<ISite> getSites();

}
