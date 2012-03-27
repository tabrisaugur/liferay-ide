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

import com.liferay.ide.eclipse.core.CorePlugin;
import com.liferay.ide.eclipse.core.util.CoreUtil;
import com.liferay.ide.eclipse.server.remote.IRemoteConnection;
import com.liferay.ide.eclipse.server.remote.IRemoteServer;
import com.liferay.ide.eclipse.server.remote.RemoteConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.model.RuntimeDelegate;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plugin life cycle
 * 
 * @author Greg Amerson
 */
public class LiferayServerCorePlugin extends CorePlugin {

	// The plugin ID
	public static final String PLUGIN_ID = "com.liferay.ide.eclipse.server.core";

	private static Map<String, IRemoteConnection> connections = null;

	// The shared instance
	private static LiferayServerCorePlugin plugin;

	private static IPluginPublisher[] pluginPublishers = null;

	private static IRuntimeDelegateValidator[] runtimeDelegateValidators;

	private static ILiferayRuntimeStub[] runtimeStubs;

	public static IStatus createErrorStatus(String msg) {
		return createErrorStatus(PLUGIN_ID, msg);
	}

	public static IStatus createInfoStatus( String msg ) {
		return new Status( IStatus.INFO, PLUGIN_ID, msg );
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LiferayServerCorePlugin getDefault() {
		return plugin;
	}

	public static IPluginPublisher getPluginPublisher(String facetId, String runtimeTypeId) {
		if (CoreUtil.isNullOrEmpty(facetId) || CoreUtil.isNullOrEmpty(runtimeTypeId)) {
			return null;
		}

		IPluginPublisher retval = null;
		IPluginPublisher[] publishers = getPluginPublishers();

		if (publishers != null && publishers.length > 0) {
			for (IPluginPublisher publisher : publishers) {
				if (publisher != null && facetId.equals(publisher.getFacetId()) &&
					runtimeTypeId.equals(publisher.getRuntimeTypeId())) {
					retval = publisher;
					break;
				}
			}
		}

		return retval;
	}

	public static IPluginPublisher[] getPluginPublishers() {
		if (pluginPublishers == null) {
			IConfigurationElement[] elements =
				Platform.getExtensionRegistry().getConfigurationElementsFor(IPluginPublisher.ID);

			try {
				List<IPluginPublisher> deployers = new ArrayList<IPluginPublisher>();

				for (IConfigurationElement element : elements) {
					final Object o = element.createExecutableExtension("class");

					if (o instanceof AbstractPluginPublisher) {
						AbstractPluginPublisher pluginDeployer = (AbstractPluginPublisher) o;
						pluginDeployer.setFacetId(element.getAttribute("facetId"));
						pluginDeployer.setRuntimeTypeId(element.getAttribute("runtimeTypeId"));
						deployers.add(pluginDeployer);
					}
				}

				pluginPublishers = deployers.toArray(new IPluginPublisher[0]);
			}
			catch (Exception e) {
				logError("Unable to get plugin deployer extensions", e);
			}
		}

		return pluginPublishers;
	}

	public static IRemoteConnection getRemoteConnection( final IRemoteServer server ) {
		if ( connections == null ) {
			connections = new HashMap<String, IRemoteConnection>();

			ServerCore.addServerLifecycleListener( new IServerLifecycleListener() {

				public void serverAdded( IServer server ) {
				}

				public void serverChanged( IServer server ) {
				}

				public void serverRemoved( IServer s ) {
					if ( server.equals( s ) ) {
						IRemoteConnection service = connections.get( server.getId() );

						if ( service != null ) {
							service = null;
							connections.put( server.getId(), null );
						}
					}
				}
			} );
		}

		IRemoteConnection service = connections.get( server.getId() );

		if ( service == null ) {
			service = new RemoteConnection();

			updateConnectionSettings( server, service );

			connections.put( server.getId(), service );
		}

		return service;
	}

	public static IRuntimeDelegateValidator[] getRuntimeDelegateValidators() {
		if (runtimeDelegateValidators == null) {
			IConfigurationElement[] elements =
				Platform.getExtensionRegistry().getConfigurationElementsFor(IRuntimeDelegateValidator.ID);

			try {
				List<IRuntimeDelegateValidator> validators = new ArrayList<IRuntimeDelegateValidator>();

				for (IConfigurationElement element : elements) {
					final Object o = element.createExecutableExtension("class");
					final String runtimeTypeId = element.getAttribute("runtimeTypeId");

					if (o instanceof AbstractRuntimeDelegateValidator) {
						AbstractRuntimeDelegateValidator validator = (AbstractRuntimeDelegateValidator) o;
						validator.setRuntimeTypeId(runtimeTypeId);
						validators.add(validator);
					}
				}

				runtimeDelegateValidators = validators.toArray(new IRuntimeDelegateValidator[0]);
			}
			catch (Exception e) {
				logError("Unable to get IRuntimeDelegateValidator extensions", e);
			}
		}

		return runtimeDelegateValidators;
	}

	public static ILiferayRuntimeStub getRuntimeStub( String stubTypeId ) {
		ILiferayRuntimeStub retval = null;

		ILiferayRuntimeStub[] stubs = getRuntimeStubs();

		if ( !CoreUtil.isNullOrEmpty( stubs ) ) {
			for ( ILiferayRuntimeStub stub : stubs ) {
				if ( stub.getRuntimeStubTypeId().equals( stubTypeId ) ) {
					retval = stub;
					break;
				}
			}
		}

		return retval;
	}

	public static ILiferayRuntimeStub[] getRuntimeStubs() {
		if ( runtimeStubs == null ) {
			IConfigurationElement[] elements =
				Platform.getExtensionRegistry().getConfigurationElementsFor( ILiferayRuntimeStub.EXTENSION_ID );

			if ( !CoreUtil.isNullOrEmpty( elements ) ) {
				List<ILiferayRuntimeStub> stubs = new ArrayList<ILiferayRuntimeStub>();

				for ( IConfigurationElement element : elements ) {
					String runtimeTypeId = element.getAttribute( ILiferayRuntimeStub.RUNTIME_TYPE_ID );
					String name = element.getAttribute( ILiferayRuntimeStub.NAME );
					boolean isDefault = Boolean.parseBoolean( element.getAttribute( ILiferayRuntimeStub.DEFAULT ) );

					try {
						LiferayRuntimeStub stub = new LiferayRuntimeStub();
						stub.setRuntimeTypeId( runtimeTypeId );
						stub.setName( name );
						stub.setDefault( isDefault );

						stubs.add( stub );
					}
					catch ( Exception e ) {
						logError( "Could not create liferay runtime stub.", e );
					}
				}

				runtimeStubs = stubs.toArray( new ILiferayRuntimeStub[0] );
			}
		}

		return runtimeStubs;
	}

	public static IPath getTempLocation( String prefix, String fileName ) {
		return getDefault().getStateLocation().append( "tmp" ).append(
			prefix + "/" + System.currentTimeMillis() + ( CoreUtil.isNullOrEmpty( fileName ) ? "" : "/" + fileName ) );
	}

	public static void logError(Exception e) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
	}

	public static void logError(String msg, Exception e) {
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, msg, e));
	}

	public static void updateConnectionSettings( IRemoteServer server ) {
		updateConnectionSettings( server, getRemoteConnection( server ) );
	}

	public static void updateConnectionSettings( IRemoteServer server, IRemoteConnection remoteConnection ) {
		remoteConnection.setHost( server.getHost() );
		remoteConnection.setHttpPort( server.getHttpPort() );
		remoteConnection.setManagerContextPath( server.getServerManagerContextPath() );
		remoteConnection.setUsername( server.getUsername() );
		remoteConnection.setPassword( server.getPassword() );
	}

	public static IStatus validateRuntimeDelegate(RuntimeDelegate runtimeDelegate) {
		if (runtimeDelegate.getRuntime().isStub()) {
			return Status.OK_STATUS;
		}

		String runtimeTypeId = runtimeDelegate.getRuntime().getRuntimeType().getId();

		IRuntimeDelegateValidator[] validators = getRuntimeDelegateValidators();

		if (!CoreUtil.isNullOrEmpty(validators)) {
			for (IRuntimeDelegateValidator validator : validators) {
				if (runtimeTypeId.equals(validator.getRuntimeTypeId())) {
					IStatus status = validator.validateRuntimeDelegate(runtimeDelegate);

					if (!status.isOK()) {
						return status;
					}
				}
			}
		}

		return Status.OK_STATUS;
	}

	/**
	 * The constructor
	 */
	public LiferayServerCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
	 */
	public void start(BundleContext context)
		throws Exception {
		super.start(context);

		plugin = this;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext )
	 */
	public void stop(BundleContext context)
		throws Exception {
		plugin = null;

		super.stop(context);

	}
}
