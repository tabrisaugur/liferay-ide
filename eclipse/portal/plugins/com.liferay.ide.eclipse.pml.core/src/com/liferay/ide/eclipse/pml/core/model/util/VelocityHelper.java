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

package com.liferay.ide.eclipse.pml.core.model.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
public class VelocityHelper implements UtilConstants {

	private static VelocityHelper instance;
	final VelocityContext ctx = new VelocityContext();

	private VelocityHelper( String appServerRoot ) {
		Velocity.setProperty( Velocity.RESOURCE_LOADER, "file" );
		Velocity.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, appServerRoot + LFR_LAYOUT_TEMPLATES_DEF_PATH );
	}

	public static VelocityHelper getInstance( String appServerRoot ) {
		if ( instance == null ) {
			instance = new VelocityHelper( appServerRoot );
		}
		return instance;
	}

	/**
	 * @param templateNode
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public List<String> getColumnsForTemplate( String tpl ) throws Exception {
		List<String> columnIds = new ArrayList<String>();

		if ( Velocity.resourceExists( tpl ) ) {
			Template template = Velocity.getTemplate( tpl );

			Writer writer = new StringWriter();
			template.merge( ctx, writer );
			writer.flush();
			// System.out.println(writer);
			InputSource source = new InputSource( new StringReader( writer.toString() ) );
			XPathFactory xPathFactory = XPathFactory.newInstance();

			XPath cutomTplPath = xPathFactory.newXPath();
			XPathExpression cutomTplPathExpr = cutomTplPath.compile( "//div" );
			Object obj = cutomTplPathExpr.evaluate( source, XPathConstants.NODESET );
			if ( obj instanceof NodeList ) {

				NodeList divNodes = (NodeList) obj;
				for ( int i = 0; i < divNodes.getLength(); i++ ) {
					Node divNode = divNodes.item( i );
					NamedNodeMap attributes = divNode.getAttributes();
					Node attrClass = attributes.getNamedItem( "class" );
					if ( attrClass != null && attrClass.getNodeValue().contains( "portlet-column" ) ) {
						// System.out.println( attrClass );
						String id = attributes.getNamedItem( ATTR_ID ).getNodeValue();
						columnIds.add( id );
					}
				}
			}
		}
		return columnIds;
	}
}
