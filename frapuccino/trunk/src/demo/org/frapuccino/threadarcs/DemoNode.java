package org.frapuccino.threadarcs;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.frapuccino.threadarcs.IThreadArcsNode;

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

public class DemoNode implements IThreadArcsNode {

	private static int next = 0;
	
	private DemoNode parent;
	private Map attributes;
	
	
	/**
	 * 
	 * Constructs the TestNode.
	 * 
	 * @param parent
	 */
	public DemoNode(DemoNode parent) {
		this.parent = parent;
		attributes = new Hashtable();
		attributes.put("date", new Date());
	}
	
	/**
	 * @see IThreadArcsNode#getGenerationalDepth()
	 */
	public int getGenerationalDepth() {
		if( parent == null ) {
			return 0;
		} else {
			return parent.getGenerationalDepth() + 1;
		}
	}

	/**
	 * @see IThreadArcsNode#getParent()
	 */
	public IThreadArcsNode getParent() {
		return parent;
	}

	/**
	 * @see IThreadArcsNode#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}
}
