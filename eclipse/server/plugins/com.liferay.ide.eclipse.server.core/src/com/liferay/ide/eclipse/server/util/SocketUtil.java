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

package com.liferay.ide.eclipse.server.util;

import com.liferay.ide.eclipse.server.core.LiferayServerCorePlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author Greg Amerson
 */
public class SocketUtil {

	public static IStatus canConnect( String host, int port )
	{
		return canConnect(new Socket(), host, port);
	}

	public static IStatus canConnect( Socket socket, String host, int port )
	{
		IStatus status = null;

		InputStream in = null;

		try {
			InetSocketAddress address = new InetSocketAddress(host, Integer.valueOf(port));
			InetSocketAddress local = new InetSocketAddress(0);
			socket.bind(local);
			socket.connect(address);
			in = socket.getInputStream();
			status = Status.OK_STATUS;
		}
		catch (Exception e) {
			status = LiferayServerCorePlugin.createErrorStatus( "Could not connect." );
			// e.printStackTrace();
		}
		finally {
			if (socket != null) {
				try {
					socket.close();
				}
				catch (IOException e) {
					// best effort
				}
			}

			if (in != null) {
				try {
					in.close();
				}
				catch (Exception e) {
					// best effort
				}
			}
		}

		return status;
	}

}
