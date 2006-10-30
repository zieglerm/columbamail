package org.columba.core.gui.tagging;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import org.columba.core.tagging.TagManager;
import org.columba.core.tagging.api.ITag;
import org.columba.core.tagging.api.ITagEvent;
import org.columba.core.tagging.api.ITagListener;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;

public class TagList extends JXList {

	private DefaultListModel listModel;

	public TagList() {
		super();

		// fill list model with tags
		listModel = new DefaultListModel();
		Iterator<ITag> it = TagManager.getInstance().getAllTags();
		while (it.hasNext()) {
			ITag tag = it.next();
			listModel.addElement(tag);
		}
		setModel(listModel);

		setCellRenderer(new MyListCellRenderer());

		setBorder(null);
		setHighlighters(new HighlighterPipeline(
				new Highlighter[] { new RolloverHighlighter(new Color(248, 248,
						248), Color.white) }));
		setRolloverEnabled(true);

		TagManager.getInstance().addTagListener(new MyTagListener());
	}

	public ITag getSelectedTag() {
		return (ITag) getSelectedValue();
	}

	public Iterator<ITag> getSelectedTags() {
		Object[] values = getSelectedValues();
		Vector<ITag> v = new Vector<ITag>();

		for (Object o : values) {
			v.add((ITag) o);
		}

		return v.iterator();
	}

	class MyListCellRenderer extends JPanel implements ListCellRenderer {

		private Border lineBorder = new HeaderSeparatorBorder(new Color(230,
				230, 230));

		private JLabel nameLabel = new JLabel();

		private JLabel descriptionLabel = new JLabel();

		private JLabel idLabel = new JLabel();

		MyListCellRenderer() {
			setLayout(new BorderLayout());

			add(nameLabel, BorderLayout.WEST);
			add(descriptionLabel, BorderLayout.CENTER);
			add(idLabel, BorderLayout.EAST);

			setBorder(BorderFactory.createCompoundBorder(lineBorder,
					BorderFactory.createEmptyBorder(2, 2, 2, 2)));

			setOpaque(true);

		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			ITag result = (ITag) value;

			idLabel.setText("(id = " + result.getId() + ")");
			idLabel.setForeground(new Color(100, 100, 100));
			nameLabel.setText(result.getProperty("name"));
			descriptionLabel.setText(result.getProperty("description"));

			return this;
		}

	}

	class HeaderSeparatorBorder extends AbstractBorder {

		protected Color color;

		public HeaderSeparatorBorder(Color color) {
			super();

			this.color = color;
		}

		/**
		 * Paints the border for the specified component with the specified
		 * position and size.
		 * 
		 * @param c
		 *            the component for which this border is being painted
		 * @param g
		 *            the paint graphics
		 * @param x
		 *            the x position of the painted border
		 * @param y
		 *            the y position of the painted border
		 * @param width
		 *            the width of the painted border
		 * @param height
		 *            the height of the painted border
		 */
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			Color oldColor = g.getColor();
			g.setColor(color);
			g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);

			g.setColor(oldColor);
		}

		/**
		 * Returns the insets of the border.
		 * 
		 * @param c
		 *            the component for which this border insets value applies
		 */
		public Insets getBorderInsets(Component c) {
			return new Insets(0, 0, 1, 0);
		}

		/**
		 * Reinitialize the insets parameter with this Border's current Insets.
		 * 
		 * @param c
		 *            the component for which this border insets value applies
		 * @param insets
		 *            the object to be reinitialized
		 */
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.left = insets.top = insets.right = insets.bottom = 1;
			return insets;
		}

	}

	class MyTagListener implements ITagListener {
		MyTagListener() {
		}

		public void tagChanged(ITagEvent event) {
			String tagId = event.getId();
			updateList();
		}

		public void tagAdded(ITagEvent event) {
			String tagId = event.getId();
			updateList();
		}

		public void tagDeleted(ITagEvent event) {
			String tagId = event.getId();
			updateList();
		}

		// real stupid recreation of whole list model
		// -> replace with id-based listmodel update
		private void updateList() {
			listModel = new DefaultListModel();
			Iterator<ITag> it = TagManager.getInstance().getAllTags();
			while (it.hasNext()) {
				ITag tag = it.next();
				listModel.addElement(tag);
			}
			setModel(listModel);
		}
	}
}
