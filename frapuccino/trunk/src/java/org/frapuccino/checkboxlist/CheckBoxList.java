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

import java.awt.Component;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * CheckBoxList is a JList where the elements in the JList can be marked with checkboxes.
 *
 * @author redsolo
 */
public class CheckBoxList extends JList {

    private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private CheckBoxListCellRenderer myRenderer;

    private CheckBoxListController myListener;

    /**
     * Creates the CheckBoxList.
     */
    public CheckBoxList() {
        super(new CheckBoxListModel());
        initList();
    }

    /**
     * Creates the CheckBoxList and fills it with the specified objects.
     * @param dataArray array filled with data.
     */
    public CheckBoxList(Object[] dataArray) {
        super();

        CheckBoxListModel model = new CheckBoxListModel();
        for (int i = 0; i < dataArray.length; i++) {
            model.addElement(dataArray[i]);
        }
        setModel(model);

        initList();
    }

    /**
     * Creates the CheckBoxList and fills it with specified objects in the vector.
     * @param list copies the items in the list into this component.
     */
    public CheckBoxList(List list) {
        super();

        CheckBoxListModel model = new CheckBoxListModel();
        for (Iterator items = list.iterator(); items.hasNext();) {
            model.addElement(items.next());
        }
        setModel(model);

        initList();
    }

    /**
     * Creates the CheckBoxList with the ListModel.
     * @param model a model to use for the list.
     */
    public CheckBoxList(CheckBoxListModel model) {
        super(model);
        initList();
    }

    /**
     * Inits the list.
     */
    private void initList() {
        myRenderer = new CheckBoxListCellRenderer();
        setCellRenderer(myRenderer);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myListener = new CheckBoxListController(this);
        addMouseListener(myListener);
        addKeyListener(myListener);
    }

    /**
     * Sets the readstate of the list.
     * @param readOnly the list is readonly, ie can not alter the check boxes.
     */
    public void setReadOnly(boolean readOnly) {
        myListener.setReadOnly(readOnly);
    }

    /**
     * @param listener new listener
     */
    public void addCheckBoxListListener(CheckBoxListListener listener) {
        myListener.addCheckBoxListListener(listener);
    }

    /**
     * List cell renderer that wraps a JCheckBox
     * @author redsolo
     */
    class CheckBoxListCellRenderer implements ListCellRenderer {

        private JCheckBox checkbox;
        private boolean borderSet = false;

        /**
         * Creates the default renderer.
         */
        public CheckBoxListCellRenderer() {
            super();
            setOpaque(true);
            checkbox = new JCheckBox();
        }

        /** {@inheritDoc} */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            checkbox.setText(value.toString());

            if (isSelected) {
                checkbox.setForeground(list.getSelectionForeground());
                checkbox.setBackground(list.getSelectionBackground());
            } else {
                checkbox.setBackground(list.getBackground());
                checkbox.setForeground(list.getForeground());
            }

            checkbox.setSelected(((CheckBoxListItem) value).isSelected());

            checkbox.setFont(list.getFont());

            if (!borderSet) {
                checkbox.setBorder(UIManager.getBorder("List.focusCellHighLightBorder"));
                borderSet = true;
            }

            return checkbox;
        }
    }
}
