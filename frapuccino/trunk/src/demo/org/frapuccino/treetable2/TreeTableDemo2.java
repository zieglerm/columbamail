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

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.frapuccino.DemoComponent;
import org.frapuccino.treetable2.JTreeTable;

/**
 * @author fdietz
 *  
 */
public class TreeTableDemo2 implements DemoComponent {

	private JTreeTable treeTable;

	private DemoModel model;

	/**
	 *  
	 */
	public TreeTableDemo2() {
		super();

		createModel();

		treeTable = new JTreeTable(model);
	}

	/**
	 *  
	 */
	private void createModel() {
		DemoNode root = new DemoNode();
		root.setName("root");
		root.setSize(10);

		for (int i = 0; i < 100; i++) {
			DemoNode child = new DemoNode("child" + i);
			child.setSize(i);
			root.add(child);

			for (int j = 0; j < 1000; j++) {
				DemoNode n = new DemoNode("subchild" + j);
				n.setSize(j);
				child.add(n);
			}
		}

		model = new DemoModel(root);
	}

	/**
	 * @see org.frapuccino.DemoComponent#getComponent()
	 */
	public JComponent getComponent() {
		return new JScrollPane(treeTable);
	}

	/**
	 * @see org.frapuccino.DemoComponent#getDescription()
	 */
	public String getDescription() {

		return "JTreeTable 2 Demo";
	}

	/**
	 * @see org.frapuccino.DemoComponent#getDemoName()
	 */
	public String getDemoName() {

		return "JTreeTable 2 Demo";
	}

}