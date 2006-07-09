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
package org.macchiato.maps;

import java.io.File;
import java.util.Iterator;

import org.macchiato.log.MacchiatoLogger;
import org.macchiato.serialize.ObjectReader;
import org.macchiato.serialize.ObjectWriter;
import org.macchiato.tokenizer.Token;

/**
 * 
 *
 * @author fdietz
 */
public class ProbabilityMapIO {

	/**
	 * Save probability map to file.
	 * 
	 * @param map		probability map
	 * @param file		file
	 */
	public static void save(ProbabilityMap map, File file) throws Exception {
		ObjectWriter writer= new ObjectWriter(file);

		// save total number of tokens
		writer.writeObject(new Integer(map.count()));

		Iterator it= map.iterator();
		while (it.hasNext()) {
			Token token= (Token) it.next();
			float p= map.getProbability(token);
			MacchiatoLogger.log.info(
				" write token[" + token.getString() + "]=" + p);

			writer.writeObject(token.getString());
			writer.writeObject(new Float(p));
		}

		writer.close();
	}

	/**
	 * Load probabilities from file. Use given map.
	 * 
	 * @param map			probability map
	 * @param file			file
	 * @throws Exception
	 */
	public static void load(ProbabilityMap map, File file) throws Exception {
		ObjectReader reader= new ObjectReader(file);

		// read total number of tokens
		int count= ((Integer) reader.readObject()).intValue();

		Object o= null;
		for (int i= 0; i < count; i++) {

			o= reader.readObject();
			Token token= new Token((String) o);
			Float p= (Float) reader.readObject();
			MacchiatoLogger.log.info(
				"read token[" + token.getString() + "]=" + p);

			map.addToken(token, p.floatValue());
		}

		reader.close();
	}
}
