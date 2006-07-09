// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.frapuccino.treetable2;

import org.frapuccino.treetable2.AbstractTreeTableModel;
import org.frapuccino.treetable2.TreeTableModel;

/**
 * @author fdietz
 *  
 */
public class DemoModel extends AbstractTreeTableModel {

	
	//	 Names of the columns.
	static protected String[] columnNames = { "Name", "Size" };

	// Types of the columns.
	static protected Class[] columnTypes = { TreeTableModel.class, Integer.class };

	/**
	 * @param root
	 */
	public DemoModel(Object root) {
		super(root);
		
	}

	/**
	 * @see org.frapuccino.treetable.TreeTableModel#getValueAt(java.lang.Object,
	 *      int)
	 */
	public Object getValueAt(Object n, int column) {
		DemoNode node = (DemoNode) n;
		
		if ( column == 0) return node.getName();
		if ( column == 1) return new Integer(node.getSize());
		
		return null;
	}

	/**
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object arg0) {
		DemoNode node = (DemoNode) arg0;
		
		return node.getChildCount();
	}

	/**
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object arg0, int arg1) {
		DemoNode node = (DemoNode) arg0;
		
		return node.getChildAt(arg1);
	}
	
	/**
	 * @see org.frapuccino.treetable2.TreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}
	/**
	 * @see org.frapuccino.treetable2.TreeTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return columnNames[column];
	}

	/**
	 * @see org.frapuccino.treetable2.TreeTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int column) {
		return columnTypes[column]; 
	}
}