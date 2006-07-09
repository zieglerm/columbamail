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
package org.frapuccino;

import javax.swing.JComponent;


/**
 * Demos should implement this interface.
 *
 * @author redsolo
 */
public interface DemoComponent {

    /**
     * Returns a demo JComponent for this demo.
     * @return a JComponent that demonstrates this UI widget.
     */
    JComponent getComponent();

    /**
     * Returns a long description about the widget.
     * The text can have HTML tags within it, but it should not start with
     * the <html><body> and end with </body></html> tags.
     * @return a long description about the widget.
     */
    String getDescription();

    /**
     * Returns a name for this widget demo.
     * @return a name for this widget demo.
     */
    String getDemoName();
}
