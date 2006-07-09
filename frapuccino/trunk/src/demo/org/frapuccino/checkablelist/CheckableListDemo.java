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

package org.frapuccino.checkablelist;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.frapuccino.DemoComponent;
import org.frapuccino.common.FrameBuilder;

/**
 * @author fdietz
 */
public class CheckableListDemo implements DemoComponent {

    /**
     * Displays the demo.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        new CheckableListDemo().testDefault();
    }

    public void testDefault() {
        CheckableList list = new CheckableList();

        JFrame frame = FrameBuilder.createFrame(list);
        frame.setVisible(true);
    }

    /** {@inheritDoc} */
    public JComponent getComponent() {
        return new CheckableList();
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return "Checkable list using a JTable implementation.";
    }

    /** {@inheritDoc} */
    public String getDemoName() {
        return "Checkable list";
    }
}
