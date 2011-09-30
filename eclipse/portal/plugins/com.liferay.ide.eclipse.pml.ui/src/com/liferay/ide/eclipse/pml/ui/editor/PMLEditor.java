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

package com.liferay.ide.eclipse.pml.ui.editor;

import com.liferay.ide.eclipse.pml.core.model.IPortal;
import com.liferay.ide.eclipse.pml.ui.PMLUIPlugin;

import org.eclipse.sapphire.ui.swt.xml.editor.SapphireEditorForXml;

/**
 * @author <a href="mailto:kamesh.sampath@accenture.com">Kamesh Sampath</a>
 */
public class PMLEditor extends SapphireEditorForXml {

	private static final String EDITOR_DEFINITION_PATH =
		"com.liferay.ide.eclipse.pml.ui/com/liferay/ide/eclipse/pml/ui/editor/internal/pml-editor.sdef/DetailsPage";

	public PMLEditor() {
		super( PMLUIPlugin.PLUGIN_ID );
		initEditorSettings();
	}

	/**
	 * this method will initialize the editor with its settings like model element, UI definition etc.,
	 */
	private void initEditorSettings() {

		setEditorDefinitionPath( EDITOR_DEFINITION_PATH );
		setRootModelElementType( IPortal.TYPE );

	}
}
