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

/**
 * CheckBoxListItem is used by the CheckBoxList for storing information about the elements in the list, if they are
 * checked or not.
 *
 * @author redsolo
 */
public class CheckBoxListItem {

    private Object value;

    private boolean isSelectedFlag;

    /**
     * Creates a ListItem for the specified object and is marked according to the boolean.
     * @param object the object for this item.
     * @param selected if the item is selected from start or not.
     */
    public CheckBoxListItem(Object object, boolean selected) {
        value = object;
        isSelectedFlag = selected;
    }

    /**
     * Creates a ListItem for the specified object and is not checked.
     * @param object the object for this item.
     */
    public CheckBoxListItem(Object object) {
        this(object, false);
    }

    /**
     * Returns the value for the ListItem.
     * @return the value for the ListItem.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value for the ListItem.
     * @param object the new value.
     */
    public void setValue(Object object) {
        value = object;
    }

    /**
     * Set if the checkbox should be marked or not.
     * @param selected if it is selected or not.
     */
    public void setSelected(boolean selected) {
        isSelectedFlag = selected;
    }

    /**
     * Invert the checkbox status.
     */
    public void invertSelected() {
        isSelectedFlag = !isSelectedFlag;
    }

    /**
     * Checks if the ListItem is checked or not.
     * @return if the item the is checked or not.
     */
    public boolean isSelected() {
        return isSelectedFlag;
    }

    /**
     * Returns a string representation of the ListItem.
     * @return a String.
     */
    public String toString() {
        return value.toString();
    }
}
