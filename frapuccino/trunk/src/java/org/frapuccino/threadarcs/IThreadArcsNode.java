package org.frapuccino.threadarcs;


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

/**
 * Interface that must be implemented by a node of
 * the ThreadArcs View.
 * 
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public interface IThreadArcsNode {
	
	/**
	 * Gets the generational depth of the node. This is
	 * its distance in steps to the root.
	 * 
	 * @return the generational depth of this node
	 */
	public int getGenerationalDepth();

	/**
	 * Gets the parent of this node.
	 * 
	 * @return parent of the node.
	 */
	public IThreadArcsNode getParent();
	
	
	/**
	 * Gets the attribute of this node. This is e.g. the date
	 * or the sender.
	 * 
	 * @param name the name of the attribute
	 * @return the attribute or null if not set
	 */
	public Object getAttribute(String name);
}
