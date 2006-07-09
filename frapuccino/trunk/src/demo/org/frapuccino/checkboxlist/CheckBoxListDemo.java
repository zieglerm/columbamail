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
package org.frapuccino.checkboxlist;

import javax.swing.JComponent;

import org.frapuccino.DemoComponent;
import org.frapuccino.checkboxlist.CheckBoxList;
import org.frapuccino.checkboxlist.CheckBoxListModel;


/**
 * @author redsolo
 */
public final class CheckBoxListDemo extends CheckBoxList implements DemoComponent {

    /**
     * Utility constructor
     */
    public CheckBoxListDemo() {
        CheckBoxListModel model = new CheckBoxListModel();
        model.addElement("Test 1", false);
        model.addElement("Test 2", true);
        model.addElement("Test 3", false);
        setModel(model);
    }

    /** {@inheritDoc} */
    public JComponent getComponent() {
        return this;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return "Checkabel list using a JList implementation.";
    }

    /** {@inheritDoc} */
    public String getDemoName() {
        return "Checkable list v2";
    }

}
