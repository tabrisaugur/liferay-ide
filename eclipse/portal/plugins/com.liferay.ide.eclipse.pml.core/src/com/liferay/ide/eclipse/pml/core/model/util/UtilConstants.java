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

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
public interface UtilConstants {

	final String LFR_THEME_FILE = "liferay-look-and-feel.xml";

	final String LFR_LAYOUT_TEMPLATES_FILE = "liferay-layout-templates.xml";

	final String LFR_OOB_PORTLETS_FILE = "webapps/ROOT/WEB-INF/portlet-custom.xml";
	final String LFR_THEMES = "webapps/ROOT/WEB-INF/liferay-look-and-feel.xml";
	final String LFR_LAYOUT_TEMPLATES = "webapps/ROOT/WEB-INF/liferay-layout-templates.xml";
	
	final String LFR_LAYOUT_TEMPLATES_DEF_PATH = "/webapps/ROOT/layouttpl/custom/";

	static final String DISPLAY_NAME = "display-name";

	static final String PORTLET_NAME = "portlet-name";

	static final String PORTLET_XPATH = "/portlet-app/portlet";

	static final String LFR_THEME_XPATH = "/look-and-feel/theme";

	static final String ATTR_NAME = "name";

	static final String ATTR_ID = "id";

	static final String LFR_LAYOUT_XPATH = "/layout-templates/custom/layout-template";
}
