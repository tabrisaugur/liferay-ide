/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.ide.eclipse.server.ui.cmd;

import com.liferay.ide.eclipse.server.remote.ILiferayServerWorkingCopy;

public class SetUsernameCommand extends LiferayServerCommand
{

	protected String oldUsername;
	protected String username;

	public SetUsernameCommand( ILiferayServerWorkingCopy server, String username )
	{
		super( server, "Set Username" );
		this.username = username;
	}

	/**
	 * Execute setting the memory args
	 */
	public void execute() {
		oldUsername = server.getUsername();
		server.setUsername( username );
	}

	/**
	 * Restore prior memoryargs
	 */
	public void undo() {
		server.setUsername( oldUsername );
	}
}