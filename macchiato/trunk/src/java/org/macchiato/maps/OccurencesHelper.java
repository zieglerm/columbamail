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
import java.io.FileNotFoundException;

import org.macchiato.tokenizer.Tokenizer;
import org.macchiato.tokenizer.TokenizerFactory;

/**
 * 
 *
 * @author fdietz
 */
public class OccurencesHelper {

	public static OccurencesMap createMapFromFile(File file) throws FileNotFoundException{
		Tokenizer tokenizer= TokenizerFactory.createFileTokenizer(file);
		return OccurencesMapFactory.createOccurencesMap(tokenizer);
	}
	
	public static OccurencesMap createMapFromFile(File[] file) throws FileNotFoundException {
		OccurencesMap map = new OccurencesMapImpl();
		
		for ( int i=0; i<file.length; i++) {
			Tokenizer t = TokenizerFactory.createFileTokenizer(file[i]);
			OccurencesMapFactory.appendOccurencesMap(map, t);
		}
		
		return map;
	}
}
