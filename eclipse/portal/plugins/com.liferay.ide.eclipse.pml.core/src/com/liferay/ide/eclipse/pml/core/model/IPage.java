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

import com.liferay.ide.eclipse.pml.core.model.internal.InvertingBooleanXmlValueBinding;

import org.eclipse.sapphire.modeling.ImpliedElementProperty;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.CountConstraint;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Image;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.annotations.Whitespace;
import org.eclipse.sapphire.modeling.xml.annotations.CustomXmlValueBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@GenerateImpl
@Image( path = "images/elcl16/page_16x16.png" )
public interface IPage extends ICommonProperties, IPageContainer {

	ModelElementType TYPE = new ModelElementType( IPage.class );

	// *** Type ***

	@Type( base = PageType.class )
	@Label( standard = "Page Type" )
	@DefaultValue( text = "Portlet" )
	@Required
	@XmlBinding( path = "@type" )
	ValueProperty PROP_TYPE = new ValueProperty( TYPE, "Type" );

	Value<PageType> getType();

	void setType( PageType version );

	void setType( String version );

	// *** FriendlyUrl ***

	@Label( standard = "Friendly Url" )
	@XmlBinding( path = "@friendly-url" )
	@Whitespace( trim = true )
	ValueProperty PROP_FRIENDLY_URL = new ValueProperty( TYPE, "FriendlyUrl" );

	Value<String> getFriendlyUrl();

	void setFriendlyUrl( String value );

	// *** Portlet ***

	@Type( base = IPortletType.class )
	@Label( standard = "Portlet" )
	@XmlBinding( path = "portlet" )
	@CountConstraint( max = 1, min = 0 )
	ImpliedElementProperty PROP_PORTLET = new ImpliedElementProperty( TYPE, "Portlet" );

	IPortletType getPortlet();

	// *** WebContent ***

	@Type( base = IWebContent.class )
	@Label( standard = "Web Content" )
	@CountConstraint( max = 1, min = 0 )
	ImpliedElementProperty PROP_WEBCONTENT = new ImpliedElementProperty( TYPE, "WebContent" );

	IWebContent getWebContent();

	// *** LinkToPage ***

	@Type( base = ILinkToPage.class )
	@Label( standard = "LinkToPage" )
	@CountConstraint( max = 1, min = 0 )
	ImpliedElementProperty PROP_LINK_TO_PAGE = new ImpliedElementProperty( TYPE, "LinkToPage" );

	ILinkToPage getLinkToPage();

	// *** URL ***

	@Type( base = IEmbeddedType.class )
	@Label( standard = "URL" )
	@CountConstraint( max = 1, min = 0 )
	ImpliedElementProperty PROP_URL = new ImpliedElementProperty( TYPE, "URL" );

	IEmbeddedType getURL();

	// *** EMBEDDED ***

	@Type( base = IEmbeddedType.class )
	@Label( standard = "Embedded" )
	@CountConstraint( max = 1, min = 0 )
	ImpliedElementProperty PROP_EMBEDDED = new ImpliedElementProperty( TYPE, "Embedded" );

	IEmbeddedType getEMBEDDED();

	// *** Hidden ***

	@Type( base = Boolean.class )
	@Label( standard = "Hidden" )
	@XmlBinding( path = "@hidden" )
	@CustomXmlValueBinding( impl = InvertingBooleanXmlValueBinding.class,params="@hidden" )
	ValueProperty PROP_HIDDEN = new ValueProperty( TYPE, "Hidden" );

	Value<Boolean> getHidden();

	void setHidden( String value );

	void setHidden( Boolean value );

	// *** Layout ***

	@Label( standard = "Layout" )
	@Whitespace( trim = true )
	ValueProperty PROP_LAYOUT = new ValueProperty( TYPE, "Layout" );

	Value<String> getLayout();

	void setLayout( String value );

}
