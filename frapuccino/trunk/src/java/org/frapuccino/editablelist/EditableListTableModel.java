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

package org.frapuccino.editablelist;

import org.frapuccino.log.FrappucinoLogger;

/**
 * 
 *
 * @author fdietz
 */
public class EditableListTableModel extends ListTableModel {

	/**
	 * 
	 */
	public EditableListTableModel() {
		super();

	}

	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {

		return true;
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object value, int row, int column) {

		FrappucinoLogger.log.info("old=" + getValueAt(row, 0));

		setElement(row, (String) value);

		FrappucinoLogger.log.info("new=" + value);
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int column) {
		return getValueAt(0,column).getClass();
	}

}
