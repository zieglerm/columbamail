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

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.frapuccino.DemoComponent;
import org.frapuccino.common.FrameBuilder;
import org.frapuccino.editablelist.EditableList;
import org.frapuccino.editablelist.EditableListTableModel;

/**
 * @author fdietz
 */
public class EditableListDemo extends EditableList implements DemoComponent {

    /**
     * Creates the demo.
     */
    public EditableListDemo() {
        EditableListTableModel model = new EditableListTableModel();
        model.addElement("test1");
        model.addElement("test2");
        model.addElement("test3");
        setModel(model);
    }

    /**
     * Displays the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {

        new EditableListDemo().testCustomData();
    }

    public void testDefault() {
        EditableList list = new EditableList();

        JFrame frame = FrameBuilder.createFrame(list);
        frame.setVisible(true);
        frame = FrameBuilder.createFrame(list);
        frame.setVisible(true);
    }

    public void testCustomData() {
        JFrame frame = FrameBuilder.createFrame(new EditableListDemo());
        frame.setVisible(true);
    }

    /** {@inheritDoc} */
    public JComponent getComponent() {
        return this;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return "A editable list";
    }

    /** {@inheritDoc} */
    public String getDemoName() {
        return "Editable list";
    }
}
