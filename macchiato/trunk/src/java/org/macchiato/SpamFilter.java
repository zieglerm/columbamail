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

import org.macchiato.maps.ProbabilityMap;


/**
 * Main wrapper for all spam functionality.
 *
 * @author fdietz
 */
public interface SpamFilter {

    /**
     * Calculate scoring value.
     * 
     * @param message		message
     * @param additional	additional handcrafted rules occurences map
     * @return				message spam score between 0.-1.0
     */
	float scoreMessage(Message message, ProbabilityMap additional);
	
	/**
	 * Train message as spam, meaning add all tokens
	 * to the database.
	 * <p>
	 * Increase bad message count.
	 * 
	 * @param message		message
	 */
	void trainMessageAsSpam(Message message);
	
	/**
	 * Train message as ham, meaning add all tokens
	 * to the database.
	 * <p>
	 * Increase good message count.
	 * 
	 * @param message		message
	 */
	void trainMessageAsHam(Message message);
	
	/**
	 * Train message as spam, meaning add all tokens
	 * to the database.
	 * <p>
	 * <b>Do not </b>increase bad message count, because this
	 * message was already learned.
	 * 
	 * @param message		message
	 */
	void correctMessageAsSpam(Message message);
	
	/**
	 * Train message as spam, meaning add all tokens
	 * to the database.
	 * <p>
	 * <b>Do not </b>increase good message count, because this
	 * message was already learned.
	 * 
	 * @param message		message
	 */
	void correctMessageAsHam(Message message);
	
}
