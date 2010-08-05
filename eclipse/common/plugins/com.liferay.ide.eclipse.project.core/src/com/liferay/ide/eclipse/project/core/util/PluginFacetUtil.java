/*******************************************************************************
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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

package com.liferay.ide.eclipse.project.core.util;

import com.liferay.ide.eclipse.project.core.ISDKProjectsImportDataModelProperties;
import com.liferay.ide.eclipse.project.core.facet.IPluginFacetConstants;
import com.liferay.ide.eclipse.project.core.facet.IPluginProjectDataModelProperties;
import com.liferay.ide.eclipse.sdk.ISDKConstants;
import com.liferay.ide.eclipse.sdk.SDK;
import com.liferay.ide.eclipse.sdk.SDKManager;

import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.common.project.facet.IJavaFacetInstallDataModelProperties;
import org.eclipse.jst.common.project.facet.core.JavaFacetInstallConfig;
import org.eclipse.jst.j2ee.web.project.facet.IWebFacetInstallDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject.Action;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IPreset;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;

/**
 * @author Greg Amerson
 */
public class PluginFacetUtil {

	public static void configureJavaFacet(IFacetedProjectWorkingCopy fpjwc, IProjectFacet requiredFacet, IPreset preset) {

		Action action = fpjwc.getProjectFacetAction(requiredFacet);

		Object config = action.getConfig();

		JavaFacetInstallConfig javaConfig = (JavaFacetInstallConfig) config;

		IDataModel dm = (IDataModel) Platform.getAdapterManager().getAdapter(config, IDataModel.class);

		if (preset.getId().contains("portlet")) {
			javaConfig.setSourceFolder(new Path(IPluginFacetConstants.PORTLET_PLUGIN_SDK_SOURCE_FOLDER));
			javaConfig.setDefaultOutputFolder(new Path(IPluginFacetConstants.PORTLET_PLUGIN_SDK_DEFAULT_OUTPUT_FOLDER));

			// TODO try to remove default output folder from project to ensure
			// it gets setup correctly?
			dm.setStringProperty(
				IJavaFacetInstallDataModelProperties.SOURCE_FOLDER_NAME,
				IPluginFacetConstants.PORTLET_PLUGIN_SDK_SOURCE_FOLDER);

			dm.setStringProperty(
				IJavaFacetInstallDataModelProperties.DEFAULT_OUTPUT_FOLDER_NAME,
				IPluginFacetConstants.PORTLET_PLUGIN_SDK_DEFAULT_OUTPUT_FOLDER);
		}
		else if (preset.getId().contains("hook")) {
			// javaConfig.setSourceFolder(new
			// Path(IPluginFacetConstants.HOOK_PLUGIN_SDK_SOURCE_FOLDER));
			// javaConfig.setDefaultOutputFolder(new
			// Path(IPluginFacetConstants.HOOK_PLUGIN_SDK_DEFAULT_OUTPUT_FOLDER));

			dm.setStringProperty(
				IJavaFacetInstallDataModelProperties.SOURCE_FOLDER_NAME,
				IPluginFacetConstants.HOOK_PLUGIN_SDK_SOURCE_FOLDER);

			dm.setStringProperty(
				IJavaFacetInstallDataModelProperties.DEFAULT_OUTPUT_FOLDER_NAME,
				IPluginFacetConstants.HOOK_PLUGIN_SDK_DEFAULT_OUTPUT_FOLDER);
		}
	}

	public static void configureLiferayFacet(
		IFacetedProjectWorkingCopy fpjwc, IProjectFacet requiredFacet, IDataModel model) {

		Action action = fpjwc.getProjectFacetAction(requiredFacet);

		Object config = action.getConfig();

		IDataModel dm = (IDataModel) config;

		String sdkLocation = model.getStringProperty(ISDKProjectsImportDataModelProperties.SDK_LOCATION);

		dm.setProperty(IPluginProjectDataModelProperties.LIFERAY_SDK_NAME, getSDKName(sdkLocation));
	}

	public static void configureLiferayFacet(
		IFacetedProjectWorkingCopy fpjwc, IProjectFacetVersion requiredFacetVersion, IDataModel model) {
		configureLiferayFacet(fpjwc, requiredFacetVersion.getProjectFacet(), model);
	}

	public static void configureProjectAsPlugin(IFacetedProjectWorkingCopy fpjwc, IDataModel model) {

		// final IPreset preset = template.getInitialPreset();
		final IRuntime runtime = (IRuntime) model.getProperty(IFacetProjectCreationDataModelProperties.FACET_RUNTIME);

		fpjwc.setTargetedRuntimes(Collections.<IRuntime> emptySet());

		if (runtime != null) {
			// final Set<IProjectFacetVersion> minFacets = new
			// HashSet<IProjectFacetVersion>();
			//
			// try {
			// for( IProjectFacet f : fpjwc.getFixedProjectFacets() ) {
			// minFacets.add( f.getLatestSupportedVersion( runtime ) );
			// }
			// }
			// catch( CoreException e ) {
			// throw new RuntimeException( e );
			// }
			//
			// fpjwc.setProjectFacets( minFacets );

			fpjwc.setTargetedRuntimes(Collections.singleton(runtime));
		}

		fpjwc.setPrimaryRuntime(runtime);

		// fpjwc.setSelectedPreset(
		// FacetedProjectFramework.DEFAULT_CONFIGURATION_PRESET_ID );

		// IFacetedProjectTemplate template = getLiferayTemplateForProject(fpjwc);
		IPreset preset = getLiferayPresetForProject(fpjwc);

		Set<IProjectFacetVersion> currentProjectFacets = fpjwc.getProjectFacets();

		// Set<IProjectFacet> requiredFacets = template.getFixedProjectFacets();
		Set<IProjectFacetVersion> requiredFacetVersions = preset.getProjectFacets();

		for (IProjectFacetVersion requiredFacetVersion : requiredFacetVersions) {
			boolean hasRequiredFacet = false;

			for (IProjectFacetVersion pfv : currentProjectFacets) {
				if (pfv.getProjectFacet().equals(requiredFacetVersion.getProjectFacet())) {
					if (pfv.getVersionString().equals(requiredFacetVersion.getVersionString())) {
						hasRequiredFacet = true;
					}
					else {
						fpjwc.removeProjectFacet(pfv);
					}

					break;
				}
			}

			if (ProjectUtil.isJavaFacet(requiredFacetVersion)) {
				configureJavaFacet(fpjwc, requiredFacetVersion.getProjectFacet(), preset);
			}

			if (!hasRequiredFacet) {
				fpjwc.addProjectFacet(requiredFacetVersion);

				if (ProjectUtil.isLiferayFacet(requiredFacetVersion)) {
					configureLiferayFacet(fpjwc, requiredFacetVersion, model);
				}
				else if (ProjectUtil.isDynamicWebFacet(requiredFacetVersion)) {
					configureWebFacet(fpjwc, requiredFacetVersion.getProjectFacet(), preset);
				}
			}
		}

	}

	public static void configureWebFacet(IFacetedProjectWorkingCopy fpjwc, IProjectFacet requiredFacet, IPreset preset) {

		Action action = fpjwc.getProjectFacetAction(requiredFacet);

		Object config = action.getConfig();

		IDataModel dm = (IDataModel) config;

		if (preset.getId().contains("portlet")) {
			dm.setStringProperty(IWebFacetInstallDataModelProperties.CONFIG_FOLDER,

			IPluginFacetConstants.PORTLET_PLUGIN_SDK_CONFIG_FOLDER);
			dm.setStringProperty(IWebFacetInstallDataModelProperties.SOURCE_FOLDER,

			IPluginFacetConstants.PORTLET_PLUGIN_SDK_SOURCE_FOLDER);
		}
		else if (preset.getId().contains("hook")) {
			dm.setStringProperty(
				IWebFacetInstallDataModelProperties.CONFIG_FOLDER, IPluginFacetConstants.HOOK_PLUGIN_SDK_CONFIG_FOLDER);

			dm.setStringProperty(
				IWebFacetInstallDataModelProperties.SOURCE_FOLDER, IPluginFacetConstants.HOOK_PLUGIN_SDK_SOURCE_FOLDER);
		}
	}

	public static IPreset getLiferayPresetForProject(IFacetedProjectWorkingCopy fpjwc) {
		IPreset preset = null;

		String projName = fpjwc.getProjectName();

		if (projName.endsWith(ISDKConstants.PORTLET_PLUGIN_PROJECT_SUFFIX)) {
			preset = ProjectFacetsManager.getPreset(IPluginFacetConstants.LIFERAY_PORTLET_PRESET);
		}
		else if (projName.endsWith(ISDKConstants.HOOK_PLUGIN_PROJECT_SUFFIX)) {
			preset = ProjectFacetsManager.getPreset(IPluginFacetConstants.LIFERAY_HOOK_PRESET);
		}
		else if (projName.endsWith(ISDKConstants.EXT_PLUGIN_PROJECT_SUFFIX)) {
			preset = ProjectFacetsManager.getPreset(IPluginFacetConstants.LIFERAY_EXT_PRESET);
		}
		else if (projName.endsWith(ISDKConstants.THEME_PLUGIN_PROJECT_SUFFIX)) {
			preset = ProjectFacetsManager.getPreset(IPluginFacetConstants.LIFERAY_THEME_PRESET);
		}
		else if (projName.endsWith(ISDKConstants.LAYOUTTPL_PLUGIN_PROJECT_SUFFIX)) {
			preset = ProjectFacetsManager.getPreset(IPluginFacetConstants.LIFERAY_LAYOUTTPL_PRESET);
		}

		return preset;
	}

	public static IFacetedProjectTemplate getLiferayTemplateForProject(IFacetedProjectWorkingCopy fpjwc) {
		IFacetedProjectTemplate template = null;

		String projName = fpjwc.getProjectName();

		if (projName.endsWith(ISDKConstants.PORTLET_PLUGIN_PROJECT_SUFFIX)) {
			template = ProjectFacetsManager.getTemplate(IPluginFacetConstants.LIFERAY_PORTLET_PLUGIN_FACET_TEMPLATE_ID);
		}
		else if (projName.endsWith(ISDKConstants.HOOK_PLUGIN_PROJECT_SUFFIX)) {
			template = ProjectFacetsManager.getTemplate(IPluginFacetConstants.LIFERAY_HOOK_PLUGIN_FACET_TEMPLATE_ID);
		}
		else if (projName.endsWith(ISDKConstants.EXT_PLUGIN_PROJECT_SUFFIX)) {
			template = ProjectFacetsManager.getTemplate(IPluginFacetConstants.LIFERAY_EXT_PLUGIN_FACET_TEMPLATE_ID);
		}
		else if (projName.endsWith(ISDKConstants.THEME_PLUGIN_PROJECT_SUFFIX)) {
			template = ProjectFacetsManager.getTemplate(IPluginFacetConstants.LIFERAY_THEME_PLUGIN_FACET_TEMPLATE_ID);
		}
		else if (projName.endsWith(ISDKConstants.LAYOUTTPL_PLUGIN_PROJECT_SUFFIX)) {
			template =
				ProjectFacetsManager.getTemplate(IPluginFacetConstants.LIFERAY_LAYOUTTPL_PLUGIN_FACET_TEMPLATE_ID);
		}

		return template;
	}

	public static String getSDKName(String sdkLocation) {
		IPath sdkLocationPath = new Path(sdkLocation);

		SDK sdk = SDKManager.getSDKByLocation(sdkLocationPath);

		String sdkName = null;

		if (sdk != null) {
			sdkName = sdk.getName();
		}
		else {
			sdk = SDKManager.getInstance().createSDKFromLocation(sdkLocationPath);

			SDKManager.getInstance().addSDK(sdk);

			sdkName = sdk.getName();
		}

		return sdkName;
	}

}
