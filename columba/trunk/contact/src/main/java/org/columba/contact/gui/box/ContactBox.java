package org.columba.contact.gui.box;

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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import org.columba.addressbook.folder.IContactFolder;
import org.columba.addressbook.gui.dialog.contact.ContactEditorDialog;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.model.IContactModel;
import org.columba.addressbook.model.IContactModelPartial;
import org.columba.api.gui.frame.IContainer;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.gui.frame.api.IComponentBox;
import org.columba.core.logging.Logging;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class ContactBox implements IComponentBox {

	private JPanel panel;

	private ContactList list;

	private JTextField textField;

	private JLabel label;

	private IContactFolder selectedFolder;
	
	public ContactBox() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		label = new JLabel("Quick Find:");
		textField = new JTextField(10);

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(2,4,2,4));
		FormLayout layout = new FormLayout(
				"pref, 2dlu, fill:default:grow",
				// 2 columns
				"fill:default:grow");

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, p);

		builder.append(label);
		builder.append(textField);
		
		panel.add(p, BorderLayout.NORTH);

		AddressbookTreeModel model = AddressbookTreeModel.getInstance();
		selectedFolder = (IContactFolder) model.getFolder("101");

		list = new ContactList();
		list.addAll(selectedFolder.getHeaderItemList());

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(null);
		panel.add(scrollPane, BorderLayout.CENTER);

	}

	public String getDescription() {
		return "Contact List";
	}

	public ImageIcon getIcon() {
		return null;
	}

	public String getName() {
		return "Contact List";
	}

	public String getTechnicalName() {
		return "contact_list";
	}

	public JComponent getView() {
		return panel;
	}

	class ContactList extends JXList {

		private DefaultListModel listModel;

		public ContactList() {
			super();

			listModel = new DefaultListModel();
			setModel(listModel);
			setCellRenderer(new MyListCellRenderer());

			setBorder(null);
			setHighlighters(new HighlighterPipeline(
					new Highlighter[] { new RolloverHighlighter(new Color(248,
							248, 248), Color.white) }));
			setRolloverEnabled(true);

			addMouseListener(new DoubleClickListener() {

				@Override
				public void doubleClick(MouseEvent event) {
					IContactModelPartial selected = (IContactModelPartial) getSelectedValue();
					
					IContactModel card = null;
					try {
						card = (IContactModel) selectedFolder.get(selected.getId());
					} catch (Exception e) {
						if (Logging.DEBUG)
							e.printStackTrace();
						ErrorDialog.createDialog(e.getMessage(), e);
					}

					IContainer[] container = FrameManager.getInstance().getOpenFrames();
					if (container == null || container.length == 0)
						throw new RuntimeException("No frames available");

					IFrameMediator frameMediator = container[0].getFrameMediator();
					
					ContactEditorDialog dialog = new ContactEditorDialog(
							frameMediator.getView().getFrame(), card);

					if (dialog.getResult()) {

						try {
							// modify card properties in folder
							selectedFolder.modify(selected.getId(), dialog.getDestModel());
						} catch (Exception e1) {
							if (Logging.DEBUG)
								e1.printStackTrace();

							ErrorDialog.createDialog(e1.getMessage(), e1);
						}

					}
				}
			});

		}

		public void addAll(List<IContactModelPartial> list) {
			Iterator<IContactModelPartial> it = list.iterator();
			while (it.hasNext()) {
				listModel.addElement(it.next());
			}
		}

		public void add(IContactModelPartial result) {
			listModel.addElement(result);
		}

		public void clear() {
			listModel.clear();
		}

		class MyListCellRenderer extends JPanel implements ListCellRenderer {

			private JLabel iconLabel = new JLabel();

			private JLabel titleLabel = new JLabel();

			private JLabel descriptionLabel = new JLabel();

			private JPanel centerPanel;

			private Border lineBorder = new HeaderSeparatorBorder(new Color(
					230, 230, 230));

			MyListCellRenderer() {
				setLayout(new BorderLayout());

				centerPanel = new JPanel();
				centerPanel.setLayout(new BorderLayout());

				centerPanel.add(titleLabel, BorderLayout.NORTH);
				centerPanel.add(descriptionLabel, BorderLayout.CENTER);
				add(iconLabel, BorderLayout.WEST);
				add(centerPanel, BorderLayout.CENTER);

				setBorder(BorderFactory.createCompoundBorder(lineBorder,
						BorderFactory.createEmptyBorder(2, 2, 2, 2)));
				iconLabel
						.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

				centerPanel.setOpaque(false);
				setOpaque(true);

			}

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				if (isSelected) {
					// setBackground(list.getSelectionBackground());
					// setForeground(list.getSelectionForeground());

				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());

				}

				IContactModelPartial result = (IContactModelPartial) value;

				titleLabel.setText(result.getName());
				iconLabel.setIcon(ImageLoader.getSmallIcon(IconKeys.USER));
				descriptionLabel.setText(result.getAddress());

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
			 *            the component for which this border insets value
			 *            applies
			 */
			public Insets getBorderInsets(Component c) {
				return new Insets(0, 0, 1, 0);
			}

			/**
			 * Reinitialize the insets parameter with this Border's current
			 * Insets.
			 * 
			 * @param c
			 *            the component for which this border insets value
			 *            applies
			 * @param insets
			 *            the object to be reinitialized
			 */
			public Insets getBorderInsets(Component c, Insets insets) {
				insets.left = insets.top = insets.right = insets.bottom = 1;
				return insets;
			}

		}
	}

}
