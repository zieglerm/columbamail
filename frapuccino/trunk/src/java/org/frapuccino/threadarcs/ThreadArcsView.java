package org.frapuccino.threadarcs;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.UIManager;
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
 * View of the Thread Arcs Component.
 * 
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class ThreadArcsView extends JPanel implements ISelectionListener, MouseListener, IThreadArcsModelListener,Scrollable {

	private ThreadArcsModel model;
	
	private static int SPACING = Math.round(Toolkit.getDefaultToolkit().getScreenResolution() / 3.81f);
	public static int RADIUS = Math.round(Toolkit.getDefaultToolkit().getScreenResolution() / 25.4f);
	
	private  int verticalCenter;
	private int maxHeight;
	private List nodes;
	
	private int xpos;
	
	private IThreadArcsNode selectedParent;
	
	private Dimension minimumSize = new Dimension();
	private Dimension maximumSize = new Dimension();
	
	/**
	 * Constructs the ThreadArcsView.
	 * 
	 * @param model the underlying model of this view
	 */
	public ThreadArcsView(ThreadArcsModel model) {
		this.model = model;		
		
		// Create the view objects
		createViewObjects();
		
		// Update the Components Preferred and Minimum size
		updateComponentSizes();
		
		// Register listeners
		addMouseListener(this);
		model.addSelectionListener(this);
		model.addThreadArcsModelListener(this);
	}
	
	/**
	 * @param model
	 */
	private void createViewObjects() {
		xpos = RADIUS;
		nodes = new ArrayList();	
		
		Iterator it = model.getNodesIterator();
		while( it.hasNext() ) {
			nodes.add(new ThreadArcsViewNode(xpos, (IThreadArcsNode) it.next()));
			xpos += SPACING;
		}
	}

	private ThreadArcsViewNode getNode(IThreadArcsNode node) {
		Iterator it = nodes.iterator();
		while( it.hasNext() ) {
			ThreadArcsViewNode vNode = (ThreadArcsViewNode) it.next();
			if( vNode.getNode() == node) return vNode;
		}
		
		return null;
	}
	
	private IThreadArcsNode getNode(Point pos) {
		int yOffset = Math.abs(pos.y - verticalCenter ); 
		int xOffset = Math.abs( pos.x - Math.round(((float)pos.x  / ((float)SPACING))) * SPACING );
		
		if( (yOffset < 3 * RADIUS) &&
				(xOffset < 3 * RADIUS) ) {
			
			return model.get(Math.round(((float)pos.x  / ((float)SPACING))));
		} else {
			return null;
		}
	}
	
	
	/**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics arg0) {
		updateComponentSizes();
		
		Graphics2D g = (Graphics2D) arg0;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Get the Size of the Viewpoint to compute the max height and the center
		
		
		// Clear the background
		g.setColor(UIManager.getColor("List.background"));
		g.fill(new Rectangle( getSize()));		
		
		// Sort the nodes in selected and unselected
		// Drawing unselected first ensures, the selection
		// is always visible and not occuled

		// Draw the unselected nodes
		g.setColor(UIManager.getColor("List.foreground"));
		
		Iterator it = model.getUnselectedIterator();
		while( it.hasNext() ) {
			ThreadArcsViewNode node=getNode((IThreadArcsNode)it.next());			
			
			// draw the node
			g.fill(node.getShape(verticalCenter));

			if( node.getNode().getParent() != null ) {
				int yOffset = 1;
				
				// draw an arc to the parent
				if( node.getNode().getGenerationalDepth() % 2 == 0) {
					// draw the arc above
					yOffset = -yOffset;
				}
					
				ThreadArcsViewNode parent = getNode(node.getNode().getParent());

				paintArc(g, parent.getX(), node.getX(), verticalCenter- yOffset * RADIUS, yOffset);
			}
		}		
		
		// Draw the selected nodes
		
		it = model.getSelectedIterator();
		while( it.hasNext() ) {
			ThreadArcsViewNode node=getNode((IThreadArcsNode)it.next());			
			g.setColor(UIManager.getColor("List.selectionBackground"));

			// Draw a filled (normal) or outlined (selected) circle
			if( model.getSelected() != null && node.getNode() == model.getSelected()) {
				g.draw(node.getShape(verticalCenter));
			} else {
				g.fill(node.getShape(verticalCenter));
			}
			
			if( node.getNode().getParent() != null ) {
				int yOffset = 1;
				
				// draw an arc to the parent
				if( node.getNode().getGenerationalDepth() % 2 == 0) {
					// draw the arc above
					yOffset = -yOffset;
				}
					
				ThreadArcsViewNode parent = getNode(node.getNode().getParent());

				// Selected or normal color
				if( model.getSelected() != null && (parent.getNode() == model.getSelected() || node.getNode() == model.getSelected())) {
					g.setColor(UIManager.getColor("List.selectionBackground"));

				} else {
					g.setColor(UIManager.getColor("List.foreground"));							
				}

				paintArc(g, parent.getX(), node.getX(), verticalCenter- yOffset * RADIUS, yOffset);
			}
		}		
	}
	
	private void paintArc(Graphics2D g,int x1, int x2, int y, int side) {	
		int length = x2 - x1;
		int radius = length / 2;
		
		if( radius <= maxHeight) {
			// whole arc
			g.drawArc(x1,y-radius, length, length, 180, -side * 180);
					
		} else {
			// first arc
			g.drawArc(x1,y-maxHeight,2*maxHeight,2*maxHeight,180,-side * 90);
			
			// middle line
			g.drawLine(x1 + maxHeight, y - side * maxHeight, x2 - maxHeight, y - side * maxHeight);
			
			// end arc
			g.drawArc(x2-2*maxHeight,y- maxHeight,2*maxHeight,2*maxHeight,0,side * 90);
		}
		
	}

	private void updateComponentSizes() {
		Rectangle visible = new Rectangle(getSize());
		verticalCenter = (int) visible.getCenterY();
		maxHeight = (visible.height - 2* RADIUS) / 2;
		
		minimumSize.height = 3 * SPACING;
		minimumSize.width = (nodes.size()-1) * SPACING + 2 * RADIUS;
		
		maximumSize.height = getMaximumHeight();
		maximumSize.width = (nodes.size()-1) * SPACING + 2 * RADIUS;
	}
	
	/**
	 * @return
	 */
	private int getMaximumHeight() {
		Iterator it = nodes.iterator();
		int max = 3 * SPACING;
		
		while( it.hasNext() ) {
			ThreadArcsViewNode node = (ThreadArcsViewNode)it.next();
			
			if( node.getNode().getParent() != null ) {
				ThreadArcsViewNode parent = getNode(node.getNode().getParent());
				max = Math.max(max, (node.getX()-parent.getX()) + 2 * RADIUS);
			}
		}
		
		return max;
	}

	/**
	 * @see ISelectionListener#selectionChanged(IThreadArcsNode)
	 */
	public void selectionChanged(IThreadArcsNode selected) {
		selectedParent = selected.getParent();
		
		this.repaint();
	}

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		IThreadArcsNode clicked = getNode( arg0.getPoint());
		
		if( clicked != null) {
			model.setSelected(clicked);
		}
		
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
	}
	
	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return maximumSize;
	}
	
	
	/**
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize() {
		return minimumSize;
	}
	
	/**
	 * @see IThreadArcsModelListener#modelChanged()
	 */
	public void modelChanged() {
		createViewObjects();
		updateComponentSizes();
		repaint();
		invalidate();
		if( getParent() !=null) {
			getParent().validate();
		}
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return true;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/**
	 * @see java.awt.Component#validate()
	 */
	public void validate() {
		updateComponentSizes();
		super.validate();
	}

	/**
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		Dimension size = new Dimension(minimumSize.width, Math.min( this.getParent().getHeight(), maximumSize.height));
		
		return size;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return SPACING;
	}

	/**
	 * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return RADIUS;
	}
	/**
	 * @see java.awt.Component#getMaximumSize()
	 */
	public Dimension getMaximumSize() {
		return maximumSize;
	}
	
	
}
