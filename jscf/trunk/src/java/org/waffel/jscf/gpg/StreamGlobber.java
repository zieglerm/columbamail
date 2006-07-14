/*
 * The contents of this file are subject to the Mozilla Public License 
 * Version 1.1 (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
 * for the specific language governing rights and
 * limitations under the License.
 *
 * The Original Code is "Java Security Component Framework"
 *
 * The Initial Developer of the Original Code are Thomas Wabner, alias waffel.
 * Portions created by Thomas Wabner are Copyright (C) 2004. 
 * 
 * All Rights Reserved.
 * Created on 21.01.2004
 *
 */
package org.waffel.jscf.gpg;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author waffel
 * 
 */
public class StreamGlobber extends Thread {

	private InputStream inStream = null;

	private OutputStream outStream = null;

	private InputStream retStream = null;

	private static final int switchWall = 240000;

	private boolean useBuffer = false;

	private boolean useTempFile = false;

	private File tempFile = null;

	public StreamGlobber(InputStream inStream) {
		this.inStream = inStream;
		this.outStream = new ByteArrayOutputStream();
		this.useBuffer = true;
	}

	public StreamGlobber(InputStream inStream, OutputStream outStream) {
		this(inStream);
		this.outStream = outStream;
		this.useBuffer = false;
	}

	public void run() {
		try {
			BufferedInputStream bufInStream = new BufferedInputStream(
					this.inStream);
			byte[] _aBuffer = new byte[8000];
			int _iBytesRead;
			long _lBytesCopied = 0;

			while ((_iBytesRead = bufInStream.read(_aBuffer)) > 0) {
				this.outStream.write(_aBuffer, 0, _iBytesRead);
				_lBytesCopied += _iBytesRead;
				// System.out.println(this.name + "write2 " + _lBytesCopied+"
				// "+_iBytesRead);
				if (!this.useTempFile && (_lBytesCopied > switchWall)
						&& (this.useBuffer)) {
					this.switchToTemp();
					this.useTempFile = true;
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void switchToTemp() {
		// System.out.println(this.getId() + " switching");
		try {
			this.tempFile = File.createTempFile("jscf", null);
			this.tempFile.deleteOnExit();
			// writing output stream to file
			FileOutputStream fout = new FileOutputStream(this.tempFile);
			// System.out.println(this.getId() + " file descriptor: " +
			// fout.getFD());
			fout.write(((ByteArrayOutputStream) this.outStream).toByteArray());
			fout.flush();
			this.outStream.close();
			this.outStream = fout;
			// System.out.println("fout is now " + fout.getFD());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public InputStream getRetStream() {
		try {
			if (this.outStream instanceof ByteArrayOutputStream) {
				this.retStream = new ByteArrayInputStream(
						((ByteArrayOutputStream) this.outStream).toByteArray());
			} else if (this.outStream instanceof FileOutputStream) {
				FileInputStream fin = new FileInputStream(this.tempFile);
				this.retStream = fin;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.retStream;
	}
}