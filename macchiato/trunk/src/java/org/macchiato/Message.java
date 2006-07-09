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
package org.macchiato;

import java.io.InputStream;
import java.util.List;

/**
 * 
 *
 * @author fdietz
 */
public class Message {

	public final static String[] HEADERFIELDS=
		{ "Subject", "From", "To", "Cc", "Received" };

	private List list;

	private InputStream istream;
	
	private byte[] md5sum;
	

	public Message(InputStream istream, List list, byte[] md5sum) {
		this.istream= istream;
		this.list= list;
		this.md5sum = md5sum;
	}

	public Message(InputStream istream) {
		this.istream= istream;

	}

	/**
	 * @return
	 */
	public InputStream getInputstream() {
		return istream;
	}

	/**
	 * @return
	 */
	public List getList() {
		return list;
	}

	/**
	 * @return
	 */
	public byte[] getMd5sum() {
		return md5sum;
	}

	/**
	 * @param bs
	 */
	public void setMd5sum(byte[] bs) {
		md5sum= bs;
	}

}
