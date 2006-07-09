package org.frapuccino.threadarcs;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

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
 * Node UI Implemenation of the Thread Arcs Component.
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class ThreadArcsViewNode {

	private int x;
	
	private IThreadArcsNode node;
	
	public ThreadArcsViewNode(int x, IThreadArcsNode node) {
		this.x = x;
		this.node = node;
	}
	/**
	 * @return Returns the shape.
	 */
	public Shape getShape(int y) {
		return new Ellipse2D.Double(x-ThreadArcsView.RADIUS, y - ThreadArcsView.RADIUS, 2 * ThreadArcsView.RADIUS,2 * ThreadArcsView.RADIUS);
	}
	/**
	 * @return Returns the node.
	 */
	public IThreadArcsNode getNode() {
		return node;
	}
	
	/**
	 * @return Returns the x.
	 */
	public int getX() {
		return x;
	}
}
