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
package org.frapuccino.log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 *
 * @author fdietz
 */
public class FrappucinoLogger {
	public static final Logger log;

	/**
	 * This is initialized at first.
	 */
	static {
		log= Logger.getLogger("org.frapuccino");
		log.setLevel(Level.ALL);
	}

	/**
	 * Set parent handling. Handler inherits all
	 * properties of parent handler.
	 * 
	 * @param parent
	 */
	public static void setParentLogger(Logger parent) {
		log.setParent(parent);
		log.setUseParentHandlers(true);
		log.setLevel(parent.getLevel());
	}
}
