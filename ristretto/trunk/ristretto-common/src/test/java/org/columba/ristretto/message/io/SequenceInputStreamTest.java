/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Ristretto Mail API.
 *
 * The Initial Developers of the Original Code are
 * Timo Stich and Frederik Dietz.
 * Portions created by the Initial Developers are Copyright (C) 2004
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package org.columba.ristretto.message.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.columba.ristretto.io.SequenceInputStream;

import junit.framework.TestCase;

public class SequenceInputStreamTest extends TestCase {

	protected List createStreams() {
		List streams = new ArrayList();
		streams.add(new ByteArrayInputStream("This ".getBytes()));
		streams.add(new ByteArrayInputStream("is ".getBytes()));
		streams.add(new ByteArrayInputStream("a ".getBytes()));
		streams.add(new ByteArrayInputStream("test".getBytes()));

		return streams;
	}

	public void testConstructor() throws IOException {
		SequenceInputStream in =
			new SequenceInputStream(
				new ByteArrayInputStream("This ".getBytes()),
				new ByteArrayInputStream("is ".getBytes()));
		
		assertTrue(in.available() == 8);
	}

	public void testAvailable() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		assertTrue(in.available() == 14);

	}

	public void testRead1() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		assertTrue(in.read() == 'T');
		assertTrue(in.read() == 'h');
		assertTrue(in.read() == 'i');
		assertTrue(in.read() == 's');
		assertTrue(in.read() == ' ');
		assertTrue(in.read() == 'i');
		assertTrue(in.read() == 's');
		assertTrue(in.read() == ' ');
		assertTrue(in.read() == 'a');
	}

	public void testReadEOF1() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		for (int i = 0; i < 14; i++) {
			in.read();
		}

		assertTrue(in.read() == -1);
	}

	public void testRead2() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		byte[] buffer = new byte[14];
		assertTrue(in.read(buffer) == 14);
	}

	public void testEOF2() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		byte[] buffer = new byte[20];
		assertTrue(in.read(buffer) == 14);
	}

	public void testEOF3() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		byte[] buffer = new byte[14];
		assertTrue(in.read(buffer) == 14);
		assertTrue(in.read(buffer) == -1);
	}

	public void testSkip1() throws IOException {
		List streams = createStreams();
		SequenceInputStream in = new SequenceInputStream(streams);
		in.skip(10);
		assertTrue(in.read() == 't');
		assertTrue(in.read() == 'e');
		assertTrue(in.read() == 's');
		assertTrue(in.read() == 't');
	}
}
