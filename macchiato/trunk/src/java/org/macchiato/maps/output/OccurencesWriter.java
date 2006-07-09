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
package org.macchiato.maps.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.macchiato.maps.OccurencesMap;
import org.macchiato.tokenizer.Token;

/**
 * Write occurences map to outputstream.
 * <p>
 * Uses a custom formatter object.
 *
 * @author fdietz
 */
public class OccurencesWriter {

	private OutputStream ostream;
	private OccurencesFormatter formatter;
	private Writer out;

	public OccurencesWriter(
		OutputStream ostream,
		OccurencesFormatter formatter) {
		this.ostream= ostream;
		this.formatter= formatter;

		out= new BufferedWriter(new OutputStreamWriter(ostream));
	}

	public void write(OccurencesMap map) throws IOException{
		Iterator it= map.iterator();
		while (it.hasNext()) {
			Token token= (Token) it.next();
			int value= map.getOccurences(token);
			out.write(formatter.format(token, value));
		}
	}
	
	public void close() throws IOException {
		out.close();
	}
}
