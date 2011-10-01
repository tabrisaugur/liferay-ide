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

import com.liferay.ide.eclipse.pml.core.model.internal.CountryPossibleValueService;
import com.liferay.ide.eclipse.pml.core.model.internal.OraganizationTypeValidationService;

import org.eclipse.sapphire.modeling.ListProperty;
import org.eclipse.sapphire.modeling.ModelElementList;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Image;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.annotations.Whitespace;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@GenerateImpl
@Image( path = "images/elcl16/organization_16x16.png" )
public interface IOrganization extends ICommonProperties, IPageContainer {

	ModelElementType TYPE = new ModelElementType( IOrganization.class );

	// *** Organizations ***

	@Type( base = IOrganization.class )
	@Label( standard = "Organizations" )
	@XmlListBinding( path = "organizations", mappings = { @XmlListBinding.Mapping( element = "organization", type = IOrganization.class ) } )
	ListProperty PROP_ORGANIZATIONS = new ListProperty( TYPE, "Organizations" );

	ModelElementList<IOrganization> getOrganizations();

	// *** Type ***

	@Type( base = OrganizationType.class )
	@Label( standard = "Organization Type" )
	@Required
	@DefaultValue(text="Regular Organization")
	@XmlBinding( path = "@type" )
	@Service( impl = OraganizationTypeValidationService.class )
	ValueProperty PROP_TYPE = new ValueProperty( TYPE, "Type" );

	Value<OrganizationType> getType();

	void setType( OrganizationType version );

	void setType( String version );

	// *** FriendlyUrl ***

	@Label( standard = "Friendly Url" )
	@XmlBinding( path = "@friendly-url" )
	@Whitespace( trim = true )
	ValueProperty PROP_FRIENDLY_URL = new ValueProperty( TYPE, "FriendlyUrl" );

	Value<String> getFriendlyUrl();

	void setFriendlyUrl( String value );

	// *** Country ***

	@Label( standard = "Country" )
	@XmlBinding( path = "@location-country" )
	@Service( impl = CountryPossibleValueService.class )
	ValueProperty PROP_COUNTRY = new ValueProperty( TYPE, "Country" );

	Value<String> getCountry();

	void setCountry( String value );

	// *** Region ***

	@Label( standard = "Region" )
	@XmlBinding( path = "@location-region" )
	ValueProperty PROP_REGION = new ValueProperty( TYPE, "Region" );

	Value<String> getRegion();

	void setRegion( String value );

}
