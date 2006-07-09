package org.frapuccino.threadarcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

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

/**
 * 
 * Modell for the Thread Arcs Component.
 * 
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class ThreadArcsModel {

	private ArrayList nodes;
	private ArrayList modelListners;
	private ArrayList selectionListeners;
	
	private Comparator nodesComparator;
	
	private IThreadArcsNode selected;
	
	public ThreadArcsModel(Comparator nodesComparator) {
		nodes = new ArrayList();
		modelListners = new ArrayList();
		selectionListeners = new ArrayList();
		
		this.nodesComparator = nodesComparator;
	}
	
	/**
	 * Gets the root node.
	 * 
	 * @return the root node.
	 */
	public IThreadArcsNode getRoot() {
		return (IThreadArcsNode) nodes.get(0);
	}
	
	/**
	 * Gets a iterator on the nodes.
	 * 
	 * @return iterator on the nodes.
	 */
	public Iterator getNodesIterator() {
		return nodes.iterator();
	}
	
	public Iterator getSelectedIterator() {
		return new SelectedIterator(this);
	}
	
	public Iterator getUnselectedIterator() {
		return new UnselectedIterator(this);
	}

	/**
	 * Adds this node to the model.
	 * 
	 * @param node
	 */
	public void addNode(IThreadArcsNode node) {
		nodes.add(node);
		Collections.sort(nodes, nodesComparator);
		
		fireModelChanged();
	}
	
	/**
	 * Adds the listener.
	 * 
	 * @param listener
	 */
	public void addThreadArcsModelListener(IThreadArcsModelListener listener) {
		modelListners.add(listener);
	}
	
	
	/**
	 * Removes the listener.
	 * 
	 * @param listener
	 */
	public void removeThreadArcsModelListener(IThreadArcsModelListener listener) {
		modelListners.remove(listener);
	}
	
	
	private void fireModelChanged() {
		Iterator it = modelListners.iterator();
		
		while( it.hasNext()) {
			((IThreadArcsModelListener) it.next()).modelChanged();
		}
	}
	
	
	/**
	 * @return Returns the selected.
	 */
	public IThreadArcsNode getSelected() {
		return selected;
	}
	
	/**
	 * @param selected The selected to set.
	 */
	public void setSelected(IThreadArcsNode selected) {
		this.selected = selected;
		
		fireSelectionChanged();
	}
	
	/**
	 * 
	 */
	private void fireSelectionChanged() {
		Iterator it = selectionListeners.iterator();
		while( it.hasNext()) {
			((ISelectionListener)it.next()).selectionChanged(selected);
		}
			
	}

	public void addSelectionListener(ISelectionListener listener) {
		selectionListeners.add(listener);
	}

	public void removeSelectionListener(ISelectionListener listener) {
		selectionListeners.remove(listener);
	}

	/**
	 * @param i
	 * @return
	 */
	public IThreadArcsNode get(int i) {
		return (IThreadArcsNode) nodes.get(i);
	}

	/**
	 * @return Returns the nodes.
	 */
	ArrayList getNodes() {
		return nodes;
	}
	/**
	 * @return Returns the nodesComparator.
	 */
	Comparator getNodesComparator() {
		return nodesComparator;
	}
	/**
	 * @param nodesComparator The nodesComparator to set.
	 */
	void setNodesComparator(Comparator nodesComparator) {
		this.nodesComparator = nodesComparator;
		Collections.sort(nodes, nodesComparator);
		
		fireModelChanged();
	}
}
