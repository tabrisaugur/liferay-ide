/*******************************************************************************
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.ide.eclipse.server.core;

import java.net.URL;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author Greg Amerson
 */
public interface ILiferayServer {

	@SuppressWarnings( "deprecation" )
	IEclipsePreferences _defaultPrefs = new DefaultScope().getNode( LiferayServerCorePlugin.PLUGIN_ID );

	URL getPortalHomeUrl();

	URL getWebServicesListURL();

	URL getPluginContextURL( String context );

	String getHttpPort();

	String getHost();

	String getId();

	String ATTR_USERNAME = "username";

	String ATTR_PASSWORD = "password";

	String DEFAULT_USERNAME = _defaultPrefs.get( "default.username", "" );

	String DEFAULT_PASSWORD = _defaultPrefs.get( "default.password", "" );

	String getPassword();

	String getUsername();
}
