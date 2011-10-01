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

import com.liferay.ide.eclipse.pml.core.model.internal.AttributeTextNodeValueBinding;
import com.liferay.ide.eclipse.pml.core.model.internal.LayoutColumnPossibleValueService;
import com.liferay.ide.eclipse.pml.core.model.internal.PortletPossibleValueService;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Whitespace;
import org.eclipse.sapphire.modeling.xml.annotations.CustomXmlValueBinding;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@GenerateImpl
public interface IPortletType extends IModelElement {

	ModelElementType TYPE = new ModelElementType( IPortletType.class );

	// *** Id ***

	@Label( standard = "Id" )
	@XmlBinding( path = "@id" )
	@Whitespace( trim = true )
	ValueProperty PROP_ID = new ValueProperty( TYPE, "Id" );

	Value<String> getId();

	void setId( String value );

	// *** Name ***

	@Label( standard = "Portlet Name" )
	@Whitespace( trim = true )
	@Required
	@Service( impl = PortletPossibleValueService.class )
	@CustomXmlValueBinding( impl = AttributeTextNodeValueBinding.class, params = { "name", "portlet" } )
	ValueProperty PROP_NAME = new ValueProperty( TYPE, "Name" );

	Value<String> getName();

	void setName( String value );

	// *** LayoutColumn ***

	@Label( standard = "Layout Column" )
	@Whitespace( trim = true )
	@XmlBinding( path = "@layout-column" )
	@Service( impl = LayoutColumnPossibleValueService.class )
	ValueProperty PROP_LAYOUT_COLUMN = new ValueProperty( TYPE, "LayoutColumn" );

	Value<String> getLayoutColumn();

	void setLayoutColumn( String value );

}
