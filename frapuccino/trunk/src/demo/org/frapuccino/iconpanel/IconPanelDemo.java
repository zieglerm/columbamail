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
package org.frapuccino.iconpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.TransferHandler;

import org.frapuccino.DemoComponent;
import org.frapuccino.awt.ObjectArrayTransferable;
import org.frapuccino.iconpanel.ClickableIcon;
import org.frapuccino.iconpanel.IconPanel;

/**
 * @author redsolo
 */
public final class IconPanelDemo extends JSplitPane implements DemoComponent {

    /**
     * Creates a demo in a frame.
     */
    public IconPanelDemo() {
        super(JSplitPane.HORIZONTAL_SPLIT, true);
        setResizeWeight(0.5);

        AbstractAction action = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "User double clicked on " + ((IconPanel) e.getSource()).getSelectedValue(), "Double click",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        };

        IconPanel iconPanel = new IconPanel();
        iconPanel.add(createIcon("2"), "2");
        iconPanel.add(createIcon("4"), "4");
        iconPanel.add(createIcon("5"), "5");
        iconPanel.setDragEnabled(true);
        iconPanel.setTransferHandler(new DummyTransferHandler());
        iconPanel.setMinimumSize(new Dimension(10, 10));
        iconPanel.setDoubleClickAction(action);
        setLeftComponent(iconPanel);

        iconPanel = new IconPanel();
        iconPanel.add(createIcon("A"), "A");
        iconPanel.add(createIcon("B"), "B");
        iconPanel.add(createIcon("C"), "C");
        iconPanel.setDragEnabled(true);
        iconPanel.setTransferHandler(new DummyTransferHandler());
        iconPanel.setMinimumSize(new Dimension(10, 10));
        iconPanel.setDoubleClickAction(action);
        setRightComponent(iconPanel);
    }

    /** {@inheritDoc} */
    public JComponent getComponent() {
        return this;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return "The IconPanel component displays multiple icons and texts under them. The component"
            + " supports drag and drop operation using the Swing DnD actions.";
    }

    /** {@inheritDoc} */
    public String getDemoName() {
        return "Icon Panel";
    }

    /**
     * Returns a text icon.
     *
     * @param text the text on the icon.
     * @return a text icon.
     */
    private Icon createIcon(String text) {
        return createIcon(text, 40, 40);
    }

    /**
     * Returns a text icon.
     *
     * @param text the text on the icon.
     * @param width width of icon.
     * @param height height of icon.
     * @return a text icon.
     */
    private Icon createIcon(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setBackground(Color.lightGray);
        graphics.clearRect(0, 0, width, height);

        graphics.setColor(Color.gray);
        graphics.drawRect(0, 0, width - 1, height - 1);

        graphics.setColor(Color.black);
        graphics.setFont(new Font("Arial", Font.BOLD, 26));

        int textWidth = graphics.getFontMetrics().stringWidth(text);

        graphics.drawString(text, (width - textWidth) / 2, height / 2);
        graphics.dispose();
        return new ImageIcon(image);
    }

    /**
     * Transferhandler for the demo.
     */
    private class DummyTransferHandler extends TransferHandler {

        /** {@inheritDoc} */
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        /** {@inheritDoc} */
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return (comp instanceof IconPanel);
        }

        /** {@inheritDoc} */
        protected Transferable createTransferable(JComponent c) {
            Object[] selected = ((IconPanel) c).getSelectedIcons();
            return new ObjectArrayTransferable(c, selected);
        }

        /** {@inheritDoc} */
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == TransferHandler.MOVE) {
                ObjectArrayTransferable object;
                try {
                    //Object[] array = (Object[]) t.getTransferData(ObjectArrayTransferable.FLAVOR);
                    ((IconPanel) source).removeSelected();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /** {@inheritDoc} */
        public boolean importData(JComponent comp, Transferable t) {
            boolean successfulImport = false;
            try {

                Object[] array = (Object[]) t.getTransferData(ObjectArrayTransferable.FLAVOR);
                for (int i = 0; i < array.length; i++) {
                    ClickableIcon icon = (ClickableIcon) array[i];
                    ((IconPanel) comp).add(createIcon(icon.getText()), icon.getText(), "Imported = " + icon.getText());
                }
                successfulImport = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return successfulImport;
        }
    }
}
