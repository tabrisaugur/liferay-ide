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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.ServerCore;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
public class PmlUtil implements UtilConstants {

	static PmlUtil instance;

	final IWorkspace workspace;
	final IWorkspaceRoot wroot;
	IPath liferayRuntimePath;

	public static final Map<String, String> PORTLETS_MAP = new HashMap<String, String>();
	public static final Map<String, String> THEMES_MAP = new HashMap<String, String>();
	public static final Map<String, String> LAYOUT_TEMPLATES_MAP = new HashMap<String, String>();
	public static final Map<String, List<String>> LAYOUT_TEMPLATES_COLS_MAP = new HashMap<String, List<String>>();

	private PmlUtil() throws Exception {
		IRuntime[] serverRuntimes = ServerCore.getRuntimes();
		for ( IRuntime iRuntime : serverRuntimes ) {
			if ( iRuntime != null ) {
				IRuntimeType runtimeType = iRuntime.getRuntimeType();
				if ( runtimeType != null &&
					( runtimeType.getName().contains( "liferay" ) || runtimeType.getName().contains( "Liferay" ) ) ) {
					liferayRuntimePath = iRuntime.getLocation();
					break;
				}
			}
		}
		workspace = ResourcesPlugin.getWorkspace();
		wroot = workspace.getRoot();
		init();
	}

	private void init() throws Exception {
		parseLiferayAndCacheOoBPortlets();
		parseLiferayAndCacheOoBThemes();
		parseLiferayAndCacheOoBLayoutTemplates();

	}

	/**
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws FileNotFoundException
	 * @throws XPathExpressionException
	 */
	private void parseLiferayAndCacheOoBLayoutTemplates() throws Exception {
		if ( liferayRuntimePath != null ) {
			File lfrLayoutTplFile = liferayRuntimePath.append( LFR_LAYOUT_TEMPLATES ).toFile();
			if ( lfrLayoutTplFile.exists() ) {
				InputSource source = new InputSource( new FileReader( lfrLayoutTplFile ) );
				XPathFactory xPathFactory = XPathFactory.newInstance();
				XPath xpath = xPathFactory.newXPath();
				XPathExpression xpathExpr = xpath.compile( LFR_LAYOUT_XPATH );
				Object obj = xpathExpr.evaluate( source, XPathConstants.NODESET );

				if ( obj instanceof NodeList ) {
					NodeList nl = (NodeList) obj;
					for ( int i = 0; i < nl.getLength(); i++ ) {
						Node node = nl.item( i );

						String id = node.getAttributes().getNamedItem( ATTR_ID ).getNodeValue();
						String name = node.getAttributes().getNamedItem( ATTR_NAME ).getNodeValue();
						LAYOUT_TEMPLATES_MAP.put( id, name );
						NodeList childNodes = node.getChildNodes();
						for ( int j = 0; j < childNodes.getLength(); j++ ) {
							Node childNode = childNodes.item( j );
							if ( childNode != null ) {
								if ( "template-path".equals( childNode.getNodeName() ) ) {
									String nodeText = childNode.getTextContent();
									if ( nodeText != null && nodeText.indexOf( "/" ) != -1 ) {
										String tpl =
											childNode.getTextContent().substring(
												childNode.getTextContent().lastIndexOf( "/" ) + 1 );
										// Get the columns
										VelocityHelper velocityHelper =
											VelocityHelper.getInstance( liferayRuntimePath.toPortableString() );
										List<String> columns = velocityHelper.getColumnsForTemplate( tpl );
										if ( !columns.isEmpty() ) {
											LAYOUT_TEMPLATES_COLS_MAP.put( id, columns );
										}
										break;
									}
								}
							}
						}
					}
				}

			}
		}

	}

	/**
	 * @throws FileNotFoundException
	 * @throws XPathExpressionException
	 *             TODO: make it recursive in to sub-directories
	 */
	private void parseLiferayAndCacheOoBThemes() throws Exception {
		if ( liferayRuntimePath != null ) {
			File lfrThemesFile = liferayRuntimePath.append( LFR_THEMES ).toFile();
			if ( lfrThemesFile.exists() ) {
				InputSource source = new InputSource( new FileReader( lfrThemesFile ) );

				XPathFactory xPathFactory = XPathFactory.newInstance();

				XPath xpath = xPathFactory.newXPath();
				XPathExpression xpathExpr = xpath.compile( LFR_THEME_XPATH );
				Object obj = xpathExpr.evaluate( source, XPathConstants.NODESET );

				if ( obj instanceof NodeList ) {
					NodeList nl = (NodeList) obj;
					for ( int i = 0; i < nl.getLength(); i++ ) {
						Node node = nl.item( i );

						String id = node.getAttributes().getNamedItem( ATTR_ID ).getNodeValue();
						String name = node.getAttributes().getNamedItem( ATTR_NAME ).getNodeValue();
						THEMES_MAP.put( id, name );
					}
				}
			}
		}

	}

	/**
	 * TODO: make it recursive in to sub-directories
	 * 
	 * @throws FileNotFoundException
	 * @throws XPathExpressionException
	 */
	private void parseLiferayAndCacheOoBPortlets() throws Exception {
		if ( liferayRuntimePath != null ) {
			File lfrPortletsFile = liferayRuntimePath.append( LFR_OOB_PORTLETS_FILE ).toFile();
			if ( lfrPortletsFile.exists() ) {
				DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
				domFactory.setNamespaceAware( false );
				Document document = domFactory.newDocumentBuilder().parse( lfrPortletsFile );

				XPathFactory xPathFactory = XPathFactory.newInstance();

				XPath xpath = xPathFactory.newXPath();
				XPathExpression xpathExpr = xpath.compile( PORTLET_XPATH );
				Object obj = xpathExpr.evaluate( document, XPathConstants.NODESET );
				if ( obj instanceof NodeList ) {
					NodeList nl = (NodeList) obj;
					for ( int i = 0; i < nl.getLength(); i++ ) {
						Node node = nl.item( i );
						NodeList childNodes = node.getChildNodes();
						for ( int j = 0; j < childNodes.getLength(); j++ ) {
							Node childNode = childNodes.item( j );
							String id = "";
							String name = "";
							if ( childNode != null ) {
								if ( PORTLET_NAME.equalsIgnoreCase( childNode.getNodeName() ) ) {
									id = childNode.getTextContent();
									name = childNodes.item( j + 2 ).getTextContent();
								}
								PORTLETS_MAP.put( id, name );

							}
						}

					}
				}
			}
		}

	}

	/**
	 * @param element
	 * @return
	 * @throws CoreException
	 * @throws FileNotFoundException
	 * @throws XPathExpressionException
	 */
	public static PmlUtil getInstance() throws Exception {
		if ( instance == null ) {
			instance = new PmlUtil();
		}
		return instance;
	}

	/**
	 * @param map
	 * @param name
	 * @return
	 */
	public static String getIdByName( String mapOwner, String name ) {
		Map<String, String> map = null;
		if ( "theme".equals( mapOwner ) ) {
			map = THEMES_MAP;
		}
		else if ( "portlet".equals( mapOwner ) ) {
			map = PORTLETS_MAP;
		}
		else if ( "layout".equals( mapOwner ) ) {
			map = LAYOUT_TEMPLATES_MAP;
		}
		if ( map != null ) {
			Set<String> keys = map.keySet();
			for ( String key : keys ) {
				String value = map.get( key );
				if ( value.equalsIgnoreCase( name ) ) {
					return key;
				}
			}
		}
		return null;
	}

	public static void main( String[] args ) {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware( false );
			Document document =
				domFactory.newDocumentBuilder().parse(
					new File( "c:/liferay/ce/bundles/tomcat-7.0.21/webapps/ROOT/WEB-INF/liferay-layout-templates.xml" ) );

			XPathFactory xPathFactory = XPathFactory.newInstance();

			XPath cutomTplPath = xPathFactory.newXPath();
			XPathExpression cutomTplPathExpr = cutomTplPath.compile( LFR_LAYOUT_XPATH );
			Object obj = cutomTplPathExpr.evaluate( document, XPathConstants.NODESET );

			if ( obj instanceof NodeList ) {
				NodeList nl = (NodeList) obj;
				for ( int i = 0; i < nl.getLength(); i++ ) {
					Node node = nl.item( i );
					NodeList childNodes = node.getChildNodes();
					for ( int j = 0; j < childNodes.getLength(); j++ ) {
						Node childNode = childNodes.item( j );
						if ( childNode != null ) {
							if ( "template-path".equals( childNode.getNodeName() ) ) {

								try {
									VelocityHelper helper =
										VelocityHelper.getInstance( "c:/liferay/ce/bundles/tomcat-7.0.21" );
									String txt = childNode.getTextContent();
									List<String> columns = helper.getColumnsForTemplate( txt );
									for ( String col : columns ) {
										System.out.print( col + "," );
									}
									System.out.println( "\n" );
								}
								catch ( Exception e ) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}
				}

			}
		}
		catch ( XPathExpressionException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( DOMException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( SAXException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( ParserConfigurationException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
