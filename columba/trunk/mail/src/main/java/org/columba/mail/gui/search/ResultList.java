package org.columba.mail.gui.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import org.columba.api.exception.ServiceNotFoundException;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.base.EmptyIcon;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.search.api.ISearchResult;
import org.columba.core.services.ServiceRegistry;
import org.columba.mail.facade.IDialogFacade;
import org.columba.mail.gui.message.viewer.HeaderSeparatorBorder;
import org.columba.mail.resourceloader.MailImageLoader;
import org.columba.mail.search.MailSearchResult;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;

public class ResultList extends JXList {

	private DefaultListModel listModel;

	public ResultList() {
		listModel = new DefaultListModel();
		setModel(listModel);
		setCellRenderer(new MyListCellRenderer());

		setBorder(null);
		setHighlighters(new HighlighterPipeline(
				new Highlighter[] { new RolloverHighlighter(new Color(248, 248,
						248), Color.white) }));
		setRolloverEnabled(true);

		addMouseListener(new DoubleClickListener() {

			@Override
			public void doubleClick(MouseEvent event) {
				ISearchResult result = (ISearchResult) getSelectedValue();

				try {
					IDialogFacade facade = (IDialogFacade) ServiceRegistry
							.getInstance().getService(IDialogFacade.class);
					facade.openMessage(result.getLocation());
				} catch (ServiceNotFoundException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void addAll(List<ISearchResult> result) {
		Iterator<ISearchResult> it = result.iterator();
		while (it.hasNext()) {
			listModel.addElement(it.next());
		}
	}

	public void add(ISearchResult result) {
		listModel.addElement(result);
	}

	public void clear() {
		listModel.clear();
	}

	class MyListCellRenderer extends JPanel implements ListCellRenderer {

		private JPanel centerPanel;

		private JPanel topPanel;

		private Border lineBorder = new HeaderSeparatorBorder(new Color(230,
				230, 230));

		private JLabel statusLabel = new JLabel();

		private JLabel fromLabel = new JLabel();

		private JLabel dateLabel = new JLabel();

		private JLabel subjectLabel = new JLabel();

		private JLabel flagLabel = new JLabel();

		private ImageIcon flagIcon = MailImageLoader.getSmallIcon("flag.png");

		MyListCellRenderer() {
			setLayout(new BorderLayout());

			topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout());
			topPanel.add(fromLabel, BorderLayout.CENTER);
			topPanel.add(dateLabel, BorderLayout.EAST);

			centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(topPanel, BorderLayout.NORTH);
			centerPanel.add(subjectLabel, BorderLayout.CENTER);

			add(statusLabel, BorderLayout.WEST);
			add(centerPanel, BorderLayout.CENTER);
			add(flagLabel, BorderLayout.EAST);

			setBorder(BorderFactory.createCompoundBorder(lineBorder,
					BorderFactory.createEmptyBorder(2, 2, 2, 2)));

			statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
			flagLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

			topPanel.setOpaque(false);
			centerPanel.setOpaque(false);
			setOpaque(true);

		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			if (isSelected) {
				// setBackground(list.getSelectionBackground());
				// setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			MailSearchResult result = (MailSearchResult) value;

			statusLabel.setIcon(result.getStatusIcon());
			subjectLabel.setText(result.getTitle());
			fromLabel.setText(result.getFrom().getShortAddress());
			dateLabel.setText(result.getStringDate());

			if (result.isFlagged())
				flagLabel.setIcon(flagIcon);
			else
				flagLabel.setIcon(new EmptyIcon());

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

}
