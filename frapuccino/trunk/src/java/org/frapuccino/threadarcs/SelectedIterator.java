package org.frapuccino.threadarcs;
import java.util.Iterator;
import java.util.List;
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

public class SelectedIterator implements Iterator {

	private ThreadArcsModel model;
	private List list;
	
	private int pos;
	
	public SelectedIterator(ThreadArcsModel model) {
		this.model = model;
		list = model.getNodes();
		
		pos = 0;
		findNext();
	}
	
	private void findNext() {
		while(pos < list.size() ) {
			IThreadArcsNode node=(IThreadArcsNode)list.get(pos);
			// Check if the node is selected
			if( model.getSelected() != null && (node == model.getSelected() || node.getParent() == model.getSelected() || node == model.getSelected().getParent())) {
				return;
			} else {
				pos++;
			}
		}
		
		// there are no more unselected nodes
		pos = -1;
	}
	
	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return pos != -1;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		Object result = list.get(pos);
		pos++;
		findNext();
		
		return result;
	}

}
