package org.frapuccino.threadarcs;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.frapuccino.DemoComponent;

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

public class ThreadArcsDemo implements DemoComponent {

	public JComponent getComponent() {
		ThreadArcsModel model = new ThreadArcsModel(new DateComparator());
		DemoNode n2;
		DemoNode n = new DemoNode(null);
		DemoNode root = n;
		model.addNode(n);
		n = new DemoNode(n);
		model.addNode(n);
		n2= n;
		model.addNode(new DemoNode(n));
		model.addNode(new DemoNode(n));
		n = new DemoNode(n);
		model.addNode(n);
		n = new DemoNode(n);
		model.addNode(n);
		model.addNode(new DemoNode(n));
		model.addNode(new DemoNode(root));
		model.addNode(new DemoNode(root));
		model.addNode(new DemoNode(root));
		model.addNode(new DemoNode(root));
		model.addNode(new DemoNode(root));
		model.addNode(new DemoNode(n2));
		ThreadArcsView arcsPane = new ThreadArcsView(model);		
		
		return new JScrollPane(arcsPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	/**
	 * @see org.frapuccino.DemoComponent#getDescription()
	 */
	public String getDescription() {
		return "Implementation of the ThreadArcs Visualization as proposed by Bernard Kerr from IBM Research. (http://www.research.ibm.com/remail/threadarcs.html)";
	}

	/**
	 * @see org.frapuccino.DemoComponent#getDemoName()
	 */
	public String getDemoName() {
		return "ThreadArcs";
	}
}
