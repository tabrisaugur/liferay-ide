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

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelElementType;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.GenerateImpl;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Whitespace;
import org.eclipse.sapphire.modeling.xml.annotations.XmlBinding;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@GenerateImpl
public interface IEmbeddedType extends IModelElement {

	ModelElementType TYPE = new ModelElementType( IEmbeddedType.class );

	// *** URL ***

	@Label( standard = "URL" )
	@Whitespace( trim = true )
	@XmlBinding( path = "@url" )
	ValueProperty PROP_URL = new ValueProperty( TYPE, "URL" );

	Value<String> getURL();

	void setURL( String value );

	// *** Description ***

	@Label( standard = "Description" )
	@Whitespace( trim = true )
	@XmlBinding( path = "description" )
	ValueProperty PROP_DESCRIPTION = new ValueProperty( TYPE, "Description" );

	Value<String> getDescription();

	void setDescription( String value );

}
