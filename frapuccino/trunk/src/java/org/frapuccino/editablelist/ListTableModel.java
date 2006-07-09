package org.frapuccino.editablelist;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

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
 * Table model using a single Vector to store its data.
 * <p>
 * This should be used for a single column table.
 *
 * @author fdietz
 */
public class ListTableModel extends AbstractTableModel {

	private Vector data;

	/**
	 * 
	 */
	public ListTableModel() {
		super();

		data= new Vector();

	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		// one column
		return 1;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column) {

		return data.get(row);
	}
	
	public void addElement(String str) {
		data.add(str);
	}
	
	public void setElement(int index, String str) {
		data.set(index, str);
	}
	
	public void printDebug() {
		for ( int i=0; i<data.size(); i++) {
			System.out.println("data["+i+"]="+data.get(i));
		}
	}

	

}
