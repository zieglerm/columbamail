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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Intern utility class to handle Stream operations.
 * 
 * @author waffel
 * 
 */
public final class StreamUtils {
	/**
	 * <code>BUFFERSIZE</code> for copy operation. The size is set top
	 * <code>8000</code>.
	 */
	private static final int BUFFERSIZE = 8000;

	/**
	 * <code>_8BitCharset</code> charset for <em>ISO-8859-1</em>.
	 */
	private static final Charset THE8BITCHARSET = Charset.forName("ISO-8859-1");

	/**
	 * Hidden constructor for utility class.
	 */
	private StreamUtils() {
		// hiding constructor
	}

	/**
	 * Copied all bytes from an InputStream to an OutputStream. The Bufsize
	 * should be 8000 bytes or 16000 bytes. This is platform dependend. A higher
	 * number of bytes to read on block, blocks the operation for a greater
	 * time.
	 * 
	 * @param theInputStream
	 *            InputStream from wihch the bytes are to copied.
	 * @param theOutputStream
	 *            OutputStream in which the bytes are copied.
	 * @param theInputBufSize
	 *            The Buffer size. How many bytes on block are should be copied.
	 * @return Number of bytes which are copied.
	 * @throws IOException
	 *             If the Streams are unavailable.
	 */
	public static long streamCopy(final InputStream theInputStream,
			final OutputStream theOutputStream, final int theInputBufSize)
			throws IOException {
		byte[] aBuffer = new byte[theInputBufSize];
		int inputBytesRead;
		long bytesCopied = 0;

		while ((inputBytesRead = theInputStream.read(aBuffer)) > 0) {
			theOutputStream.write(aBuffer, 0, inputBytesRead);
			bytesCopied += inputBytesRead;
			// System.out.println("copy "+_lBytesCopied);
		}

		return bytesCopied;
	}

	/**
	 * Copied all bytes from an InputStream to an OutputStream. The Bufsize is
	 * set to 8000 bytes.
	 * 
	 * @param inputStream
	 *            InputStream from wihch the bytes are to copied.
	 * @param outputStream
	 *            OutputStream in which the bytes are copied.
	 * @return Number of bytes which are copied.
	 * @throws IOException
	 *             If the Streams are unavailable.
	 */
	public static long streamCopy(final InputStream inputStream,
			final OutputStream outputStream) throws IOException {
		return streamCopy(inputStream, outputStream, BUFFERSIZE);
	}

	/**
	 * Reads a InputStream into a StringBuffer. This method is 8bit safe.
	 * 
	 * @param in
	 *            the InputStream to read from
	 * @return the interpreted InputStream
	 * @throws IOException
	 *             If a read problem from the given input stream occurs.
	 */
	public static StringBuffer readInString(final InputStream in)
			throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(BUFFERSIZE);
		StringBuffer result = new StringBuffer();
		int read = in.read(buffer.array());

		while (read > 0) {
			buffer.limit(read);
			result.append(THE8BITCHARSET.decode(buffer));

			buffer.clear();
			read = in.read(buffer.array());
		}

		return result;
	}

	/**
	 * Copies all bytes from the given InputStream in a intern
	 * ByteArrayOutputStream and returnes a new InputStream with all bytes from
	 * the ByteArrayOutputStream. The data are real copied so this methods
	 * "clones" the given Inputstream and gives back a new InputStream with same
	 * Data.
	 * 
	 * @param from
	 *            InputStream from which all datas are to copy
	 * @return a new InputStream with all data from the given InputStream
	 * @throws IOException
	 *             If a read problem from the given input stream occurs.
	 */
	public static InputStream streamClone(final InputStream from)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		streamCopy(from, out);

		return new ByteArrayInputStream(out.toByteArray());
	}
}
