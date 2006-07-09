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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A CheckBoxListListener is a listener which is used on a CheckBoxList for checking the checkboxes.
 *
 * @author redsolo
 */
class CheckBoxListController implements MouseListener, KeyListener {

    private CheckBoxList myList;

    private boolean readOnly;

    private List listeners = new ArrayList();

    /**
     * Creates the Listener to the specified List.
     * @param list the checkbox list to use.
     */
    public CheckBoxListController(CheckBoxList list) {
        myList = list;
        doCheck();
    }

    /**
     * Adds another CheckBoxListListener.
     * @param listener new listener.
     */
    public void addCheckBoxListListener(CheckBoxListListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the CheckBoxListListener.
     * @param listener the listener to remove.
     */
    public void removeCheckBoxListListener(CheckBoxListListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets if the checkboxes can be checked or not.
     * @param listReadOnly if the list is read only or not.
     */
    public void setReadOnly(boolean listReadOnly) {
        readOnly = listReadOnly;
    }

    /**
     * Checks the element at the selected index in the List
     */
    private void doCheck() {
        int index = myList.getSelectedIndex();

        if (index >= 0) {
            /*
             * if (!myReadState)
             */

            CheckBoxListItem anItem = (CheckBoxListItem) myList.getModel().getElementAt(index);
            anItem.invertSelected();

            if (listeners.size() > 0) {
                for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
                    CheckBoxListListener anotherListener = (CheckBoxListListener) iterator.next();
                    anotherListener.itemChanged(anItem.getValue(), anItem.isSelected());
                }
            }
            myList.repaint();
        }
    }

    /**
     * Checks an element when the mouse is clicked on it.
     * @param event the mouse event in the list.
     */
    public void mouseClicked(MouseEvent event) {
        doCheck();
    }

    /**
     * Checks an element when the spave is entered on it.
     *
     * @param event the key event
     */
    public void keyPressed(KeyEvent event) {
        if (event.getKeyChar() == ' ') {
            doCheck();
        }
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void mouseExited(MouseEvent event) {
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void mouseEntered(MouseEvent event) {
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void mousePressed(MouseEvent event) {
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void mouseReleased(MouseEvent event) {
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void keyTyped(KeyEvent event) {
    }

    /**
     * Dummy implementation. {@inheritDoc}
     */
    public void keyReleased(KeyEvent event) {
    }
}
