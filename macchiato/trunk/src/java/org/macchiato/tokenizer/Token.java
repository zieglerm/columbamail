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
package org.macchiato.tokenizer;

/**
 * Token wraps a String object.
 * <p>
 * It adds several convenience methods.
 * <p>
 * Note, that we use the underlying String object's hashcode
 * and equals() method, for comparing.
 * <p>
 * In my first implementation I started with just a String
 * class, instead of a Token class. But for further development
 * this should be more suitable. This Token class can 
 * become handy if we want to save more functionality of a
 * token. Think of saveing start/end position of the token in
 * a document. This would make it possible to actually show the
 * document to the user and mark the token, based on the 
 * positioning information.
 *
 * @author fdietz
 */
public class Token {

	/**
	 * Token marking the end of the stream
	 */
	public final static Token EOF = new Token("EOF");

	/**
	 * Return this token, if you want it to be skipped
	 */
	public final static Token SKIP= new Token("SKIP");

	private String data;
	
	public Token() {
		data = "";
		
	}

	public Token(String s) {
		this.data= s;
	}
	/**
	 * @return
	 */
	public String getString() {
		return data;
	}

	/**
	 * @param string
	 */
	public void setString(String string) {
		data= string;
	}

	public boolean isEOF() {
		if (this.equals(EOF))
			return true;

		return false;
	}

	public boolean isSKIP() {
		if (this.equals(SKIP))
			return true;
			
		if ( getString().equals("")) return true;
		

		return false;
	}

	/**
	 * A token is equal another token if both contain the same string.
	 */
	public boolean equals(Object arg0) {
		if ( getString().equals( ((Token)arg0).getString() ) ) return true;
		
		return false;
	}

	/**
	 * Token shares its hashCode with its underlying String.
	 * 
	 */
	public int hashCode() {
		
		return getString().hashCode();
	}

}
