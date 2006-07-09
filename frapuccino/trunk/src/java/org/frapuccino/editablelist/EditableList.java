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
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.frapuccino.common.DefaultStringRenderer;

/**
 * Editable list component using a JTable instead of a JList.
 *
 * @author fdietz
 */
public class EditableList extends JTable {

	private ListTableModel model;

	public EditableList() {
		super();

		// no grid lines
		setShowGrid(false);

		setIntercellSpacing(new Dimension(0, 0));

		// single selection
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setDefaultRenderer(String.class, new DefaultStringRenderer());

		// do not show header
		setTableHeader(null);

		// fill with default data
		model= new EditableListTableModel();
		model.addElement("default data 1");
		model.addElement("default data 2");
		model.addElement("default data 3");

		setModel(model);

	}

}
