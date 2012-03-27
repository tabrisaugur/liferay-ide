/*******************************************************************************
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
 * 		Gregory Amerson - initial implementation and ongoing maintenance
 *******************************************************************************/

package com.liferay.ide.eclipse.server.ui.cmd;

import com.liferay.ide.eclipse.server.remote.ILiferayServerWorkingCopy;

/**
 * @author Greg Amerson
 */
public class SetPasswordCommand extends LiferayServerCommand
{

	protected String oldPassword;
	protected String password;

	public SetPasswordCommand( ILiferayServerWorkingCopy server, String password )
	{
		super( server, "Set Password" );
		this.password = password;
	}

	/**
	 * Execute setting the memory args
	 */
	public void execute()
	{
		oldPassword = server.getPassword();
		server.setPassword( password );
	}

	/**
	 * Restore prior memoryargs
	 */
	public void undo()
	{
		server.setPassword( oldPassword );
	}
}
