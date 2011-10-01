/*******************************************************************************
 * Copyright (c) 2000-2011 Accenture Services Pvt Ltd., All rights reserved.
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

import com.liferay.ide.eclipse.pml.core.model.util.PmlUtil;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.xml.XmlAttribute;
import org.eclipse.sapphire.modeling.xml.XmlElement;
import org.eclipse.sapphire.modeling.xml.XmlNamespaceResolver;
import org.eclipse.sapphire.modeling.xml.XmlNode;
import org.eclipse.sapphire.modeling.xml.XmlPath;
import org.eclipse.sapphire.modeling.xml.XmlValueBindingImpl;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */

public final class AttributeTextNodeValueBinding

extends XmlValueBindingImpl

{

	private XmlPath path;
	private String[] params;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.BindingImpl#init(org.eclipse.sapphire.modeling.IModelElement,
	 * org.eclipse.sapphire.modeling.ModelProperty, java.lang.String[])
	 */
	@Override
	public void init( final IModelElement element, final ModelProperty property, final String[] params ) {
		super.init( element, property, params );

		final XmlNamespaceResolver xmlNamespaceResolver = resource().getXmlNamespaceResolver();
		this.path = new XmlPath( params[0], xmlNamespaceResolver );
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.ValueBindingImpl#read()
	 */
	@Override
	public String read() {
		String value = null;

		final XmlAttribute attribute = xml( false ).getAttribute( params[0], false );
		if ( attribute != null ) {
			value = attribute.getText();
			if ( value != null ) {
				value = getDisplayText( value.trim() );
			}

		}

		return value;
	}

	private String getDisplayText( String id ) {
		String displayText = null;
		if ( params != null && "theme".equals( params[1] ) ) {
			displayText = PmlUtil.THEMES_MAP.get( id );
		}
		else if ( params != null && "portlet".equals( params[1] ) ) {
			displayText = PmlUtil.PORTLETS_MAP.get( id );
		}
		else if ( params != null && "layout".equals( params[1] ) ) {
			displayText = PmlUtil.LAYOUT_TEMPLATES_MAP.get( id );
		}
		return displayText;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.ValueBindingImpl#write(java.lang.String)
	 */
	@Override
	public void write( final String value ) {
		String val = value;

		// System.out.println( "VALUE ___________________ " + val );

		if ( val != null ) {
			val = value.trim();
		}

		// System.out.println( "AttributeTextNodeValueBinding.write() - Parent " + xml( true ).getParent() );

		xml( true ).getAttribute( params[0], true ).setText( getIdText( val ) );
	}

	/**
	 * @param name
	 * @return
	 */
	private String getIdText( String name ) {
		String idValue = null;
		if ( params != null && "theme".equals( params[1] ) ) {
			idValue = PmlUtil.getIdByName( "theme", name );
		}
		else if ( params != null && "portlet".equals( params[1] ) ) {
			idValue = PmlUtil.getIdByName( "portlet", name );
		}
		else if ( params != null && "layout".equals( params[1] ) ) {
			idValue = PmlUtil.getIdByName( "layout", name );
		}
		return idValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.sapphire.modeling.xml.XmlValueBindingImpl#getXmlNode()
	 */
	@Override
	public XmlNode getXmlNode() {
		final XmlElement element = xml( false );

		if ( element != null ) {
			return element.getChildNode( this.path, false );
		}

		return null;
	}

}
