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

import javax.swing.DefaultListModel;

/**
 * A JList with a checkbox for each row in the List
 *
 * @author redsolo
 */
public class CheckBoxListModel extends DefaultListModel {

    /**
     * Adds an object to the list and the checkbox is not marked.
     * @param element adds the element as a non checked list item.
     */
    public void addElement(Object element) {
        add(size(), element, false);
    }

    /**
     * Adds an object to the list and is check according to the specified checkbox.
     * @param element adds the element.
     * @param isSelected if the element is selected or not.
     */
    public void addElement(Object element, boolean isSelected) {
        add(size(), element, isSelected);
    }

    /**
     * Adds an object to the list at the specified index in the ListModel.
     * @param index the index.
     * @param element adds the element as a non checked list item.
     */
    public void add(int index, Object element) {
        super.add(index, new CheckBoxListItem(element, false));
    }

    /**
     * Adds an object to the list at the specified index in the listmodel and is check according to the specified
     * checkbox .
     * @param index the index.
     * @param element adds the element as a non checked list item.
     * @param isSelected if the element is selected or not.
     */
    public void add(int index, Object element, boolean isSelected) {
        super.add(index, new CheckBoxListItem(element, isSelected));
    }

    /**
     * Returns if the checkbox at the selection is marked or not.
     * @param index the index in the list.
     * @return boolean if it is selected or not.
     */
    public boolean elementAtSelection(int index) {
        return ((CheckBoxListItem) super.elementAt(index)).isSelected();
    }

    /**
     * Returns the value of the index in the ListModel.
     * @param index the index in the list.
     * @return the object at the specified index.
     */
    public Object elementAtValue(int index) {
        return ((CheckBoxListItem) super.elementAt(index)).getValue();
    }

    /**
     * Returns the CheckBoxListItem at the specified index in the ListModel.
     * @param index the index in the list.
     * @return the list item.
     */
    public CheckBoxListItem elementAtItem(int index) {
        return ((CheckBoxListItem) super.elementAt(index));
    }
}
