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
 * Contributors:
 *    Kamesh Sampath - initial implementation
 ******************************************************************************/

package com.liferay.ide.eclipse.project.core.util;

import com.liferay.ide.eclipse.core.util.ZipUtil;
import com.liferay.ide.eclipse.project.core.BinaryProjectRecord;
import com.liferay.ide.eclipse.project.core.IPortletFramework;
import com.liferay.ide.eclipse.project.core.ProjectCorePlugin;
import com.liferay.ide.eclipse.project.core.ProjectRecord;
import com.liferay.ide.eclipse.project.core.facet.IPluginFacetConstants;
import com.liferay.ide.eclipse.sdk.ISDKConstants;
import com.liferay.ide.eclipse.sdk.SDK;
import com.liferay.ide.eclipse.server.core.ILiferayRuntime;
import com.liferay.ide.eclipse.server.util.ServerUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.runtime.internal.BridgedRuntime;

/**
 * @author <a href="mailto:kamesh.sampath@hotmail.com">Kamesh Sampath</a>
 */
@SuppressWarnings( "restriction" )
public class ProjectImportUtil {

	/**
	 * @param dataModel
	 * @param pluginBinaryRecord
	 * @param liferaySDK
	 * @return
	 * @throws IOException
	 */
	public static ProjectRecord createPluginProject(
		IDataModel dataModel, BinaryProjectRecord pluginBinaryRecord, SDK liferaySDK ) throws IOException {
		ProjectRecord projectRecord = null;
		if ( !pluginBinaryRecord.isConflicts() ) {
			String displayName = pluginBinaryRecord.getDisplayName();
			String liferayPluginName = pluginBinaryRecord.getLiferayPluginName();
			File binaryFile = pluginBinaryRecord.getBinaryFile();
			IPath projectPath = null;
			IPath sdkPluginProjectFolder = liferaySDK.getLocation();
			BridgedRuntime bridgedRuntime =
				(BridgedRuntime) dataModel.getProperty( IFacetProjectCreationDataModelProperties.FACET_RUNTIME );

			ILiferayRuntime liferayRuntime = ServerUtil.getLiferayRuntime( bridgedRuntime );
			Map<String, String> appServerProperties = ServerUtil.configureAppServerProperties( liferayRuntime );
			String docrootFolder = null;
			// Create Project
			if ( pluginBinaryRecord.isHook() ) {
				projectPath = liferaySDK.createNewHookProject( displayName, displayName );

				sdkPluginProjectFolder = sdkPluginProjectFolder.append( ISDKConstants.HOOK_PLUGIN_PROJECT_FOLDER );
				docrootFolder = IPluginFacetConstants.HOOK_PLUGIN_SDK_CONFIG_FOLDER;
			}
			else if ( pluginBinaryRecord.isPortlet() ) {
				IPortletFramework[] portletFrameworks = ProjectCorePlugin.getPortletFrameworks();
				String portletFrameworkName = null;
				for ( int i = 0; i < portletFrameworks.length; i++ ) {
					IPortletFramework portletFramework = portletFrameworks[i];
					if ( portletFramework.isDefault() ) {
						portletFrameworkName = portletFramework.getShortName();
						break;
					}
				}
				projectPath =
					liferaySDK.createNewPortletProject(
						displayName, displayName, portletFrameworkName, appServerProperties );
				sdkPluginProjectFolder = sdkPluginProjectFolder.append( ISDKConstants.PORTLET_PLUGIN_PROJECT_FOLDER );
				docrootFolder = IPluginFacetConstants.PORTLET_PLUGIN_SDK_CONFIG_FOLDER;
			}
			else if ( pluginBinaryRecord.isTheme() ) {
				projectPath = liferaySDK.createNewThemeProject( displayName, displayName );
				sdkPluginProjectFolder = sdkPluginProjectFolder.append( ISDKConstants.THEME_PLUGIN_PROJECT_FOLDER );
				docrootFolder = IPluginFacetConstants.THEME_PLUGIN_SDK_CONFIG_FOLDER;
			}
			else if ( pluginBinaryRecord.isLayoutTpl() ) {
				projectPath = liferaySDK.createNewLayoutTplProject( displayName, displayName, appServerProperties );
				sdkPluginProjectFolder = sdkPluginProjectFolder.append( ISDKConstants.LAYOUTTPL_PLUGIN_PROJECT_FOLDER );
				docrootFolder = IPluginFacetConstants.LAYOUTTPL_PLUGIN_SDK_CONFIG_FOLDER;
			}

			// Move the porject to Liferay SDK location
			File tempProjectDir = projectPath.append( liferayPluginName ).toFile();
			// System.out.println( "Source Dir:" + tempProjectDir.getAbsolutePath() );
			File liferayPluginDir = sdkPluginProjectFolder.toFile();
			// System.out.println( "Dest Dir:" + liferayPluginDir.getAbsolutePath() );
			File liferayPluginProjectDir = new File( liferayPluginDir, liferayPluginName );
			FileUtils.copyDirectory( tempProjectDir, liferayPluginProjectDir );

			// Extract the contents
			File docRoot = new File( liferayPluginProjectDir, docrootFolder );
			ZipUtil.unzip( binaryFile, docRoot );

			projectRecord = new ProjectRecord( liferayPluginProjectDir );

		}
		return projectRecord;

	}

	/**
	 * This method was added as part of the IDE-381 fix, this method will collect all the binaries based on the binaries
	 * list
	 * 
	 * @return true if the directory has some binaries
	 */
	public static boolean collectBinariesFromDirectory(
		Collection<File> binaryProjectFiles, File directory, boolean recurse, IProgressMonitor monitor ) {
		if ( monitor.isCanceled() ) {
			return false;
		}

		monitor.subTask( NLS.bind( DataTransferMessages.WizardProjectsImportPage_CheckingMessage, directory.getPath() ) );

		List<String> wildCards = Arrays.asList( ISDKConstants.PLUGIN_PROJECT_BINARIES_WILDCARDS );

		WildcardFileFilter wildcardFileFilter = new WildcardFileFilter( wildCards );

		Collection<File> contents = FileUtils.listFiles( directory, wildcardFileFilter, DirectoryFileFilter.INSTANCE );

		if ( contents == null ) {
			return false;
		}
		else {

			for ( File file : contents ) {
				if ( !binaryProjectFiles.contains( file ) ) {
					binaryProjectFiles.add( file );
				}
			}
		}

		return true;
	}

}
