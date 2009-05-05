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
package org.frapuccino.swing;

import java.util.Comparator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author redsolo
 */
public class SortedTreeModelDecoratorTest {

    private DefaultMutableTreeNode orgRootNode;
    private DefaultTreeModel orgModel;

    private SortedTreeModelDecorator sortedModel;

    /** {@inheritDoc} */
    @Before
    public void setUp() throws Exception {
        orgRootNode = new DefaultMutableTreeNode();
        orgModel = new DefaultTreeModel(orgRootNode);

        DefaultMutableTreeNode cChild = new DefaultMutableTreeNode("CC");
        cChild.add(new DefaultMutableTreeNode("9"));
        cChild.add(new DefaultMutableTreeNode("7"));
        cChild.add(new DefaultMutableTreeNode("3"));
        cChild.add(new DefaultMutableTreeNode("8"));

        orgModel.insertNodeInto(cChild, orgRootNode, 0);
        orgModel.insertNodeInto(new DefaultMutableTreeNode("AA"), orgRootNode, 1);
        orgModel.insertNodeInto(new DefaultMutableTreeNode("HH"), orgRootNode, 2);
        orgModel.insertNodeInto(new DefaultMutableTreeNode("GG"), orgRootNode, 3);
        orgModel.insertNodeInto(new DefaultMutableTreeNode("KK"), orgRootNode, 4);
        sortedModel = new SortedTreeModelDecorator(orgModel);
    }

    /**
     * Test to sort a simple one level tree.
     */
    @Test
    public void testGetChild() {
        Assert.assertSame("Expected the same object at index 0 in the sorted list", orgModel.getChild(orgRootNode, 1), sortedModel.getChild(orgRootNode, 0));
        Assert.assertSame("Expected the same object at index 1 in the sorted list", orgModel.getChild(orgRootNode, 0), sortedModel.getChild(orgRootNode, 1));
        Assert.assertSame("Expected the same object at index 3 in the sorted list", orgModel.getChild(orgRootNode, 3), sortedModel.getChild(orgRootNode, 2));
        Assert.assertSame("Expected the same object at index 2 in the sorted list", orgModel.getChild(orgRootNode, 2), sortedModel.getChild(orgRootNode, 3));
    }

    /**
     * Test to sort a simple two level tree.
     */
    @Test
    public void testSort() {
        Object orgCChild = orgModel.getChild(orgRootNode, 0);
        Object expCChild = sortedModel.getChild(orgRootNode, 1);
        Assert.assertSame("The b child was not the same object", orgCChild, expCChild);
        Object cChild = expCChild;
        Assert.assertSame("Expected the same object at index 0 in the sorted list", orgModel.getChild(cChild, 2), sortedModel.getChild(cChild, 0));
        Assert.assertSame("Expected the same object at index 1 in the sorted list", orgModel.getChild(cChild, 1), sortedModel.getChild(cChild, 1));
        Assert.assertSame("Expected the same object at index 2 in the sorted list", orgModel.getChild(cChild, 3), sortedModel.getChild(cChild, 2));
        Assert.assertSame("Expected the same object at index 3 in the sorted list", orgModel.getChild(cChild, 0), sortedModel.getChild(cChild, 3));
    }

    /**
     * Tests the isLeaf() method.
     */
    @Test
    public void testIsLeaf() {
        Object orgCChild = orgModel.getChild(orgRootNode, 0);
        Assert.assertEquals("The B child is not a child as it is in the original model.", orgModel.isLeaf(orgCChild), sortedModel.isLeaf(orgCChild));
    }

    /**
     * Tests the getChildCount() method.
     */
    @Test
    public void testGetChildCount() {
        Assert.assertEquals("The number of childs of the root is not correct", 5, sortedModel.getChildCount(orgRootNode));
        Assert.assertEquals("The number of childs of the root is not correct", orgModel.getChildCount(orgRootNode), sortedModel.getChildCount(orgRootNode));
        Object orgCChild = orgModel.getChild(orgRootNode, 0);
        Assert.assertEquals("The number of childs of the B child is not correct", 4, sortedModel.getChildCount(orgCChild));
        Assert.assertEquals("The number of childs of the B child is not correct", orgModel.getChildCount(orgCChild), sortedModel.getChildCount(orgCChild));
    }

    /**
     * Tests the getIndexOfChild() method.
     */
    @Test
    public void testGetIndexOfChild() {
        Assert.assertEquals("The child should be the middle child in the sorted list.", 1,
                sortedModel.getIndexOfChild(orgRootNode, orgModel.getChild(orgRootNode, 0)));
        Assert.assertEquals("The child should be the first child in the sorted list.", 0,
                sortedModel.getIndexOfChild(orgRootNode, orgModel.getChild(orgRootNode, 1)));
        Assert.assertEquals("The child should be the last child in the sorted list.", 3,
                sortedModel.getIndexOfChild(orgRootNode, orgModel.getChild(orgRootNode, 2)));
        Assert.assertEquals("The child should be the last child in the sorted list.", 2,
                sortedModel.getIndexOfChild(orgRootNode, orgModel.getChild(orgRootNode, 3)));
    }

    /**
     * Tests the addTreeModeListener() method.
     */
    @Test
    public void testAddListener() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);

        orgModel.insertNodeInto(orgRootNode, new DefaultMutableTreeNode("aaa"), 0);
        Assert.assertNotNull("The insert event wasnt fired correctly", listener.treeNodesInsertedEvent);
        listener.clearEvents();

        /*orgModel.removeNodeFromParent(orgRootNode.getFirstLeaf());
        assertNotNull("The remove event wasnt fired correctly", listener.treeNodesRemovedEvent);
        listener.clearEvents();*/

        orgModel.setRoot(new DefaultMutableTreeNode("NEW ROOT"));
        Assert.assertNotNull("The tree structure change event wasnt fired correctly", listener.treeStructureChangedEvent);
    }

    /**
     * Tests the removeTreeModelListener() method.
     */
    @Test
    public void testRemoveListener() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);

        orgModel.insertNodeInto(orgRootNode, new DefaultMutableTreeNode("aaa"), 0);
        Assert.assertNotNull("The insert event wasnt fired correctly", listener.treeNodesInsertedEvent);
        listener.clearEvents();
        sortedModel.removeTreeModelListener(listener);

        orgModel.insertNodeInto(orgRootNode, new DefaultMutableTreeNode("aaa"), 0);
        Assert.assertNull("The listener received the inserted event", listener.treeNodesInsertedEvent);
        listener.clearEvents();

        /*orgModel.removeNodeFromParent(orgRootNode.getFirstLeaf());
        assertNull("The listener received the removed event", listener.treeNodesRemovedEvent);
        listener.clearEvents();*/

        orgModel.setRoot(new DefaultMutableTreeNode("NEW ROOT"));
        Assert.assertNull("The listener received the tree structure change event", listener.treeStructureChangedEvent);
    }

    /**
     * Tests that the tree nodes inserted event is thrown and with the correct indexes.
     */
    @Test
    public void testNodesInsertedEvent() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("DD");
        orgModel.insertNodeInto(newNode, orgRootNode, 1);

        assertTreeEvent(listener.treeNodesInsertedEvent, orgRootNode, 1, "inserted");
        Object[] paths = listener.treeNodesInsertedEvent.getPath();
        int[] newChildIndexes = listener.treeNodesInsertedEvent.getChildIndices();
        Assert.assertSame("The event did not occur with the root node as the parent", orgRootNode, paths[0]);
        Assert.assertEquals("The number of new childs was not correct.", 1, newChildIndexes.length);
        Assert.assertEquals("The new node's index was not sorted in the model.", 2, newChildIndexes[0]);
        Assert.assertSame("The model has a different object for the event's index", newNode, sortedModel.getChild(orgRootNode, newChildIndexes[0]));

        listener.clearEvents();
        MutableTreeNode orgCChild = (MutableTreeNode) orgModel.getChild(orgRootNode, 0);
        newNode = new DefaultMutableTreeNode("5");
        orgModel.insertNodeInto(newNode, orgCChild, 0);
        paths = listener.treeNodesInsertedEvent.getPath();
        newChildIndexes = listener.treeNodesInsertedEvent.getChildIndices();
        Assert.assertSame("The event did not occur with the B node as the parent", orgCChild, paths[1]);
        Assert.assertEquals("The number of new childs was not correct.", 1, newChildIndexes.length);
        Assert.assertEquals("The new node's index was not sorted in the model.", 1, newChildIndexes[0]);
        Assert.assertSame("The model has a different object for the event's index", newNode, sortedModel.getChild(orgCChild, newChildIndexes[0]));
    }

    /**
     * Tests that the tree nodes removed event is thrown and with the correct indexes.
     */
    @Test
    public void testNodesRemovedEvent() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);

        MutableTreeNode orgHChild = (MutableTreeNode) orgModel.getChild(orgRootNode, 2);
        orgModel.removeNodeFromParent(orgHChild);

        TreeModelEvent event = listener.treeNodesRemovedEvent;
        assertTreeEvent(event, orgRootNode, 1, "removed");
        int[] removedIndexes = event.getChildIndices();
        Object[] removedChildren = event.getChildren();

        // Remove HH child ie, org index = 2, sorted index = 3
        // CC-AA-HH-GG-KK - original model before change (the order does not change)
        //  1  0  3  2  4 - sorted model before change

        Assert.assertEquals("The number of removed childs wasnt correct", 1, removedIndexes.length);
        Assert.assertEquals("The index of the child wasnt correct", 3, removedIndexes[0]);
        Assert.assertSame("The object from the event isnt the one that was removed", orgHChild, removedChildren[0]);
    }

    /**
     * Tests that the tree nodes changed event is thrown and with the correct indexes.
     * This tests a simple node changed, ie the string that represents the tree node in the UI
     * hasnt changed. ie, it keeps it place in the sorted children list.
     */
    @Test
    public void testNodesChangedEvent() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgCChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 0);

        orgModel.nodeChanged(orgCChild);
        Object[] paths = listener.treeNodeChangedEvent.getPath();
        int[] changedIndexes = listener.treeNodeChangedEvent.getChildIndices();
        Assert.assertEquals("The number changed nodes is not correct", 1, changedIndexes.length);
        Assert.assertEquals("The changed node's index was not the correct one", 1, changedIndexes[0]);
        Assert.assertSame("The event did not occur with the root node as the parent", orgRootNode, paths[0]);
        Assert.assertSame("The changed object from the event is not the one that was changed.", orgCChild, listener.treeNodeChangedEvent.getChildren()[0]);
    }

    /**
     * Tests that if a node changes its name (ie the model is re-sorted) that a correct change event is sent.
     * If the string that represents a tree node has changed, then the whole children list must
     * be sorted and this will enforce many childs to relocate into new indexes. This method tests this.
     */
    @Test
    public void testNodeNameChanged() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgCChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 0);

        orgCChild.setUserObject("ZZ");
        orgModel.nodeChanged(orgCChild);
        // This will re arrange the list to
        // CC-AA-HH-GG-KK - original model before change (the order does not change)
        //  1  0  3  2  4 - sorted model before change
        //  4  0  2  1  3 - sorted model after change => changed 1,2,3,4

        TreeModelEvent event = listener.treeNodeChangedEvent;
        assertTreeEvent(event, orgRootNode, 4, "changed");

        assertChildArray(
            new int[] {1, 2, 3, 4},
            new Object[]{
                orgModel.getChild(orgRootNode, 3),
                orgModel.getChild(orgRootNode, 2),
                orgModel.getChild(orgRootNode, 4),
                orgModel.getChild(orgRootNode, 0)
                },
            event);

        /*assertEquals("The first changed node has not the correct index", 1, changedIndexes[0]);
        assertSame("The first changed node is not the correct object", orgRootNode.getChildAt(3), changedChildren[0]); // GG
        assertEquals("The second changed node has not the correct index", 2, changedIndexes[1]);
        assertSame("The second changed node is not the correct object", orgRootNode.getChildAt(2), changedChildren[1]); // HH
        assertEquals("The third changed node has not the correct index", 3, changedIndexes[2]);
        assertSame("The third changed node is not the correct object", orgRootNode.getChildAt(4), changedChildren[2]); // KK
        assertEquals("The fourth changed node has not the correct index", 4, changedIndexes[3]);
        assertSame("The fourth changed node is not the correct object", orgRootNode.getChildAt(0), changedChildren[3]); // ZZ*/
    }

    /**
     * Tests that if a node changes its name (ie the model is re-sorted) that a correct change event is sent.
     * If the string that represents a tree node has changed, then the whole children list must
     * be sorted and this will enforce many childs to relocate into new indexes. This method tests this.
     */
    @Test
    public void testNodeNameChangedReversed() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgGChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 3);

        orgGChild.setUserObject("BB");
        orgModel.nodeChanged(orgGChild);
        // This will re arrange the list to
        // CC-AA-HH-GG-KK - original model before change (the order does not change)
        //  1  0  3  2  4 - sorted model before change
        //  2  0  3  1  4 - sorted model after change => changed 1,2

        TreeModelEvent event = listener.treeNodeChangedEvent;
        assertTreeEvent(event, orgRootNode, 2, "changed");

        assertChildArray(
            new int[] {1, 2},
            new Object[]{
                orgModel.getChild(orgRootNode, 3),
                orgModel.getChild(orgRootNode, 0)
                },
            event);
/*
        assertEquals("The first changed node has not the correct index", 1, changedIndexes[0]);
        assertSame("The first changed node is not the correct object", orgRootNode.getChildAt(3),
                sortedModel.getChild(getParent(event), 1)); //changedChildren[0]); // ' '
        assertEquals("The second changed node has not the correct index", 2, changedIndexes[1]);
        assertSame("The second changed node is not the correct object", orgRootNode.getChildAt(0), changedChildren[1]); // AA*/
        /*assertEquals("The third changed node has not the correct index", 3, changedIndexes[2]);
        assertSame("The third changed node is not the correct object", orgRootNode.getChildAt(0), changedChildren[2]); // BB*/
        /*assertEquals("The fourth changed node has not the correct index", 3, changedIndexes[3]);
        assertSame("The fourth changed node is not the correct object", orgRootNode.getChildAt(2), changedChildren[3]); // GG*/
    }

    /**
     * Test to change just the first node in the original list.
     */
    @Test
    public void testFirstNodeChange() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgCChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 0);

        orgCChild.setUserObject("11");
        orgModel.nodeChanged(orgCChild);
        // This will re arrange the list to
        // CC-AA-HH-GG-KK - original model before change (the order does not change)
        //  1  0  3  2  4 - sorted model before change
        //  0  1  3  2  4 - sorted model after change => changed 0,1

        TreeModelEvent event = listener.treeNodeChangedEvent;
        assertTreeEvent(event, orgRootNode, 2, "changed");

        assertChildArray(
            new int[] {0, 1},
            new Object[]{
                orgModel.getChild(orgRootNode, 0),
                orgModel.getChild(orgRootNode, 1)
                },
            event);
    }

    /**
     * Tests that the tree nodes inserted event is thrown and with the correct indexes.
     */
    @Test
    public void testTreeStructureChangedEvent() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgCChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 0);

        orgModel.nodeStructureChanged(orgCChild);
        Object[] paths = listener.treeStructureChangedEvent.getPath();
        Assert.assertNull("The index list should be null", listener.treeStructureChangedEvent.getChildIndices());
        Assert.assertNull("The children list should be null", listener.treeStructureChangedEvent.getChildren());
        Assert.assertSame("The event did not occur with the root node as the parent", orgRootNode, paths[0]);
        Assert.assertSame("The event did not occur with the root node as the parent", orgCChild, paths[1]);
    }

    /**
     * Tests that the tree model sends out a tree structure change and that the new comparator is used.
     */
    @Test
    public void testSetComparator() {
        Comparator reverseAlphabetic = new Comparator() {
            /** {@inheritDoc} */
            public int compare(Object o1, Object o2) {
                return -1 * o1.toString().compareTo(o2.toString());
            }
        };
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        Assert.assertSame("Expected the same object at index 1 in the sorted list", orgModel.getChild(orgRootNode, 0), sortedModel.getChild(orgRootNode, 1));
        Assert.assertSame("Expected the same object at index 3 in the sorted list", orgModel.getChild(orgRootNode, 3), sortedModel.getChild(orgRootNode, 2));

        sortedModel.setSortingComparator(reverseAlphabetic);

        Assert.assertNotNull("The tree structure event wasnt fired.", listener.treeStructureChangedEvent);
        Assert.assertSame("The structure changed event did not have the correct path", orgRootNode, listener.treeStructureChangedEvent.getPath()[0]);

        Assert.assertSame("Expected the same object at index 3 in the sorted list", orgModel.getChild(orgRootNode, 0), sortedModel.getChild(orgRootNode, 3));
        Assert.assertSame("Expected the same object at index 0 in the sorted list", orgModel.getChild(orgRootNode, 3), sortedModel.getChild(orgRootNode, 2));
    }

    /**
     * Test that the decorator isnt listening on the original if theres no listeners on the decorator.
     */
    @Test
    public void testEmptyListeners() {
        DummyTreeListener listener = new DummyTreeListener();
        sortedModel.addTreeModelListener(listener);
        DefaultMutableTreeNode orgCChild = (DefaultMutableTreeNode) orgModel.getChild(orgRootNode, 0);

        orgCChild.setUserObject("11");
        orgModel.nodeChanged(orgCChild);
        Assert.assertNotNull("The listener wasnt notified", listener.treeNodeChangedEvent);

        listener.treeNodeChangedEvent = null;
        sortedModel.removeTreeModelListener(listener);
        orgCChild.setUserObject("99");
        orgModel.nodeChanged(orgCChild);
        Assert.assertNull("The listener was notified even though it shouldnt be listening", listener.treeNodeChangedEvent);
    }

    /**
     * Asserts that the event has the expected index and object array.
     * @param expectedIndexArray an array of indexes.
     * @param expectedObjectArray an array of changed objects.
     * @param event the actual event.
     */
    private void assertChildArray(int[] expectedIndexArray, Object[] expectedObjectArray, TreeModelEvent event) {
        Assert.assertTrue("The testcase is not correct setup", expectedIndexArray.length == expectedObjectArray.length);
        Assert.assertTrue("The tree event differs in number of child indicies", expectedIndexArray.length == event.getChildIndices().length);
        Assert.assertTrue("The tree event differs in number of child objects", expectedObjectArray.length == event.getChildren().length);

        for (int i = 0; i < expectedIndexArray.length; i++) {
            Assert.assertEquals("Child at pos " + i + " has not the correct index", expectedIndexArray[i], event.getChildIndices()[i]);
            Object actualChildAtIndex = sortedModel.getChild(getParent(event), expectedIndexArray[i]);
            Assert.assertSame("The child at pos " + i + " is not the correct object", expectedObjectArray[i], actualChildAtIndex);
        }
    }

    /**
     * Asserts that the event and its variables are setup correctly.
     * @param event the event to check.
     * @param rootNode the root node of the node that sent of the event.
     * @param childCount the number of children that was expected to be affected.
     * @param type the type of event, is used in assertion messages.
     */
    private void assertTreeEvent(TreeModelEvent event, Object rootNode, int childCount, String type) {
        Assert.assertNotNull("The " + type + " node event is null", event);
        int[] changedIndexes = event.getChildIndices();
        Object[] changedChildren = event.getChildren();

        Assert.assertNotNull("The " + type + " indicies array was null.", changedIndexes);
        Assert.assertNotNull("The " + type + " children array was null.", changedChildren);
        Assert.assertEquals("The number of " + type + " nodes is not correct", childCount, changedIndexes.length);
        Assert.assertSame("The event did not occur with the root node as the parent", rootNode, event.getPath()[0]);
    }

    /**
     * Returns the parent of the event.
     * @param event the even to get the parent for.
     * @return the parent of the event.
     */
    Object getParent(TreeModelEvent event) {
        return event.getTreePath().getLastPathComponent();
    }

    /**
     * A dummy listener that stores all the latest events.
     * @author redsolo
     */
    private static class DummyTreeListener implements TreeModelListener {
        private TreeModelEvent treeNodeChangedEvent;
        private TreeModelEvent treeNodesInsertedEvent;
        private TreeModelEvent treeNodesRemovedEvent;
        private TreeModelEvent treeStructureChangedEvent;
        /** {@inheritDoc} */
        public void treeNodesChanged(TreeModelEvent e) {
            treeNodeChangedEvent = e;
        }

        /** {@inheritDoc} */
        public void treeNodesInserted(TreeModelEvent e) {
            treeNodesInsertedEvent = e;
        }

        /** {@inheritDoc} */
        public void treeNodesRemoved(TreeModelEvent e) {
            treeNodesRemovedEvent = e;
        }

        /** {@inheritDoc} */
        public void treeStructureChanged(TreeModelEvent e) {
            treeStructureChangedEvent = e;
        }

        /**
         * Clears all tree node events.
         */
        public void clearEvents() {
            treeNodeChangedEvent = null;
            treeNodesInsertedEvent = null;
            treeNodesRemovedEvent = null;
            treeStructureChangedEvent = null;
        }
    }
}
