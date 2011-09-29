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

package com.liferay.ide.eclipse.pml.core.model;

import org.eclipse.sapphire.modeling.annotations.EnumSerialization;
import org.eclipse.sapphire.modeling.annotations.Label;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
@Label( standard = "Page Type" )
public enum PageType {
	@Label( standard = "Portlet" )
	@EnumSerialization( primary = "Portlet" )
	PORTLET,

	@Label( standard = "Web Content" )
	@EnumSerialization( primary = "WebContent" )
	WEBCONTENT,

	@Label( standard = "URL" )
	@EnumSerialization( primary = "URL" )
	URL,

	@Label( standard = "Embeded" )
	@EnumSerialization( primary = "Embedded" )
	EMBEDED, 
	
	@Label( standard = "Link to Page" )
	@EnumSerialization( primary = "LinkToPage" )
	LINKTOPAGE
}
