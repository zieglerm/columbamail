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
package org.macchiato.tokenfilter.stopwords;

/**
 * Stopwords like Subject:, From:, To:, etc.
 *
 * @author fdietz
 */
public class RFC822StopWordProvider implements StopWordProvider {
	/**
	* Email headers which are usually not useful for further processing
	*/
	private final static String[] stopwords=
		{
			"Subject:",
			"From:",
			"To:",
			"Received:",
			"Date:",
			"Delivered-To:",
			"Content-Type:",
			"Return-Path:",
			"Lines:",
			"Message-ID:",
			"Content-Transfer-Encoding:",
			"X-Mailer:",
			"quoted-printable",
			"SMTP",
			"ESMTP",
			"invoked" };

	/**
		* @see org.macchiato.tokenfilter.StopWordProvider#getStopWords()
		*/
	public String[] getStopWords() {
		return stopwords;
	}

	/**
		* @see org.macchiato.tokenfilter.StopWordProvider#length()
		*/
	public int length() {
		return stopwords.length;
	}
}
