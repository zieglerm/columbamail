//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.macchiato.db;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class creating MD5 sum from an inputstream.
 *
 * @author fdietz
 */
public final class MD5SumHelper {

	public static byte[] createMD5(final InputStream data)
		throws NoSuchAlgorithmException, IOException {
		MessageDigest md5= MessageDigest.getInstance("MD5");

		final byte buf[]= new byte[8192];
		int bytes= 0;
		while ((bytes= data.read(buf)) != -1) {
			md5.update(buf, 0, bytes);
		}

		return md5.digest();
	}
}
