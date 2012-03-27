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

package com.liferay.ide.eclipse.server.ui;

import com.liferay.ide.eclipse.server.remote.ILiferayServerWorkingCopy;
import com.liferay.ide.eclipse.server.remote.IRemoteServer;
import com.liferay.ide.eclipse.server.ui.cmd.SetPasswordCommand;
import com.liferay.ide.eclipse.server.ui.cmd.SetUsernameCommand;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.ui.editor.ServerEditorSection;


public class AdminSettingsEditorSection extends ServerEditorSection {

	protected PropertyChangeListener listener;
	protected ILiferayServerWorkingCopy liferayServer;
	protected Section adminSettings;
	protected Text textPassword;
	protected Text textUsername;
	protected boolean updating = false;

	public AdminSettingsEditorSection() {
		super();
	}

	@Override
	public void createSection( Composite parent ) {
		FormToolkit toolkit = getFormToolkit( parent.getDisplay() );

		adminSettings = createSettingsSection( parent, toolkit );
		adminSettings.setText( "Liferay Admin Settings" );
		adminSettings.setLayoutData( new GridData( GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL ) );
		adminSettings.setDescription( "Specify admin settings for Liferay Portal server." );

		Composite settingsComposite = createSectionComposite( toolkit, adminSettings );

		Label usernameLabel = createLabel( toolkit, settingsComposite, "Username:" );
		usernameLabel.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, false, false ) );

		textUsername = toolkit.createText( settingsComposite, null );
		textUsername.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
		textUsername.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent e ) {
				if ( updating ) {
					return;
				}

				updating = true;
				execute( new SetUsernameCommand( liferayServer, textUsername.getText().trim() ) );
				updating = false;
				// validate();
			}

		} );

		Label passwordLabel = createLabel( toolkit, settingsComposite, "Password:" );
		passwordLabel.setLayoutData( new GridData( SWT.BEGINNING, SWT.CENTER, false, false ) );

		textPassword = toolkit.createText( settingsComposite, null, SWT.PASSWORD );
		textPassword.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
		textPassword.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent e ) {
				if ( updating ) {
					return;
				}

				updating = true;
				execute( new SetPasswordCommand( liferayServer, textPassword.getText().trim() ) );
				updating = false;
				// validate();
			}

		} );

		initialize();

		IStatus status = validateSection();

		if ( !status.isOK() ) {
			this.getManagedForm().getMessageManager().addMessage(
				liferayServer, status.getMessage(), status,
				status.getSeverity() == IStatus.ERROR ? IMessageProvider.ERROR : IMessageProvider.WARNING );
		}
	}

	@Override
	public IStatus[] getSaveStatus() {
		IStatus status = validateSection();

		if ( !status.isOK() ) {
			this.getManagedForm().getMessageManager().addMessage(
				liferayServer, status.getMessage(), status,
				status.getSeverity() == IStatus.ERROR ? IMessageProvider.ERROR : IMessageProvider.WARNING );
		}
		else {
			this.getManagedForm().getMessageManager().removeMessage( liferayServer );
		}

		return new IStatus[] { Status.OK_STATUS };
	}

	public void init( IEditorSite site, IEditorInput input ) {
		super.init( site, input );

		if ( server != null ) {
			liferayServer = (ILiferayServerWorkingCopy) server.loadAdapter( ILiferayServerWorkingCopy.class, null );
			addChangeListeners();
		}

		initialize();
	}

	protected void addChangeListeners() {
		listener = new PropertyChangeListener() {

			public void propertyChange( final PropertyChangeEvent event ) {
				// LiferayServerCorePlugin.updateConnectionSettings( liferayServer );

				if ( updating ) {
					return;
				}

				updating = true;

				Display.getDefault().syncExec( new Runnable() {

					public void run() {
						if ( IRemoteServer.ATTR_USERNAME.equals( event.getPropertyName() ) ) {
							String s = (String) event.getNewValue();
							textUsername.setText( s );
						}
						else if ( IRemoteServer.ATTR_PASSWORD.equals( event.getPropertyName() ) ) {
							String s = (String) event.getNewValue();
							textPassword.setText( s );
						}
					}
				} );

				updating = false;
			}
		};

		server.addPropertyChangeListener( listener );
	}

	protected Label createLabel( FormToolkit toolkit, Composite parent, String text ) {
		Label label = toolkit.createLabel( parent, text );
		label.setForeground( toolkit.getColors().getColor( IFormColors.TITLE ) );
		return label;
	}

	protected Composite createSectionComposite( FormToolkit toolkit, Section section ) {
		Composite composite = toolkit.createComposite( section );
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL ) );
		toolkit.paintBordersFor( composite );
		section.setClient( composite );
		return composite;
	}

	protected Section createSettingsSection( Composite parent, FormToolkit toolkit ) {
		return toolkit.createSection( parent, ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED |
			ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE );
	}

	protected void initialize() {
		if( liferayServer == null || textUsername == null || textPassword == null )
		{
			return;
		}

		updating = true;

		textUsername.setText( liferayServer.getUsername() );

		textPassword.setText( liferayServer.getPassword() );

		updating = false;
	}

	protected IStatus validateSection() {
		final IStatus[] status = new IStatus[1];

		try {
			PlatformUI.getWorkbench().getProgressService().run( true, false, new IRunnableWithProgress() {

				public void run( IProgressMonitor monitor ) throws InvocationTargetException, InterruptedException {

					status[0] = liferayServer.validate( monitor );
				}
			} );
		}
		catch ( Exception e ) {
		}

		return status[0];
	}
}
