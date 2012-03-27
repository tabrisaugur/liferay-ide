/*******************************************************************************
 * Copyright (c) 2010-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.ide.eclipse.server.remote;

import com.liferay.ide.eclipse.server.core.ILiferayServer;

import org.eclipse.wst.server.core.model.IURLProvider;

/**
 * @author Greg Amerson
 */
public interface IRemoteServer extends ILiferayServer, IURLProvider {

	String ATTR_ADJUST_DEPLOYMENT_TIMESTAMP = "adjust-deployment-timestamp";

	String ATTR_HOSTNAME = "hostname";

	String ATTR_HTTP_PORT = "http-port";

	String ATTR_LIFERAY_PORTAL_CONTEXT_PATH = "liferay-portal-context-path";

	String ATTR_SERVER_MANAGER_CONTEXT_PATH = "server-manager-context-path";

	boolean DEFAULT_ADJUST_DEPLOYMENT_TIMESTAMP = _defaultPrefs.getBoolean( "adjust.deployment.timestamp", true );

	String DEFAULT_HTTP_PORT = _defaultPrefs.get( "default.http.port", "" );

	String DEFAULT_LIFERAY_PORTAL_CONTEXT_PATH = _defaultPrefs.get( "default.liferay.portal.context.path", "" );

	String DEFAULT_SERVER_MANAGER_CONTEXT_PATH = _defaultPrefs.get( "default.server.manager.context.path", "" );

	boolean getAdjustDeploymentTimestamp();

	String getLiferayPortalContextPath();

	String getServerManagerContextPath();

	void setAdjustDeploymentTimestamp( boolean adjustDemploymentTimestamp );

}
