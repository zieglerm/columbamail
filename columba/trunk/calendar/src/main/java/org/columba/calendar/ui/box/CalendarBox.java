package org.columba.calendar.ui.box;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.columba.calendar.base.api.ICalendarItem;
import org.columba.calendar.model.api.IComponent;
import org.columba.calendar.model.api.IComponentInfo;
import org.columba.calendar.model.api.IComponentInfoList;
import org.columba.calendar.model.api.IEvent;
import org.columba.calendar.model.api.IEventInfo;
import org.columba.calendar.store.CalendarStoreFactory;
import org.columba.calendar.store.api.ICalendarStore;
import org.columba.calendar.store.api.StoreException;
import org.columba.calendar.ui.comp.CalendarPicker;
import org.columba.calendar.ui.dialog.EditEventDialog;
import org.columba.core.gui.base.DoubleClickListener;
import org.columba.core.gui.frame.api.IComponentBox;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class CalendarBox extends JPanel implements IComponentBox {

	private JTextField textField;

	private JLabel label;

	private CalendarList list;

	private CalendarPicker calendarPicker;

	public CalendarBox() {

		setLayout(new BorderLayout());

		//setBackground(UIManager.getColor("TextField.background"));

		label = new JLabel("Quick Find:");
		label.setDisplayedMnemonic('F');
		//label.setBackground(UIManager.getColor("TextField.background"));
		textField = new JTextField(10);
		label.setLabelFor(textField);

		list = new CalendarList();
		list.setModel(new FilteringModel());
		List<IEventInfo> eventList = populateListModel("work");
		list.addAll(eventList);

		list.installJTextField(textField);

		calendarPicker = new CalendarPicker();
		calendarPicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				list.uninstallJTextField(textField);
				ICalendarItem item = (ICalendarItem) calendarPicker.getSelectedItem();
				String calendarId = item.getId();
				List<IEventInfo> eventList = populateListModel(calendarId);
				list.setModel(new FilteringModel());
				list.addAll(eventList);
				list.installJTextField(textField);
				textField.setText(textField.getText());
			}
		});

		list.addMouseListener(new DoubleClickListener() {

			@Override
			public void doubleClick(MouseEvent event) {
				IEventInfo selected = (IEventInfo) list.getSelectedValue();
				ICalendarStore store = CalendarStoreFactory.getInstance()
						.getLocaleStore();

				// retrieve event from store
				try {
					IEvent model = (IEvent) store.get(selected.getId());

					EditEventDialog dialog = new EditEventDialog(null, model);
					if (dialog.success()) {
						IEvent updatedModel = dialog.getModel();

						// update store
						store.modify(selected.getId(), updatedModel);
					}

				} catch (StoreException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();
				}

			}
		});

		JPanel p = new JPanel();
		//p.setBackground(UIManager.getColor("TextField.background"));
		p.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		FormLayout layout = new FormLayout(
				"pref, 2dlu, pref, 2dlu, fill:default:grow",
				// 2 columns
				"fill:default:grow");

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, p);

		builder.append(calendarPicker);
		builder.append(label);
		builder.append(textField);

		add(p, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
	}

	private List<IEventInfo> populateListModel(String calendarId) {
		ICalendarStore store = CalendarStoreFactory.getInstance()
				.getLocaleStore();

		IComponentInfoList infoList = store.getComponentInfoList(calendarId);
		List<IEventInfo> eventList = new ArrayList<IEventInfo>();
		Iterator<IComponentInfo> it = infoList.iterator();
		while (it.hasNext()) {
			IComponentInfo info = it.next();
			if (info.getType().equals(IComponent.TYPE.EVENT)) {
				eventList.add((IEventInfo) info);
			}
		}
		return eventList;
	}

	public String getDescription() {
		return "Calendar";
	}

	public ImageIcon getIcon() {
		return ImageLoader.getSmallIcon(IconKeys.CALENDAR);
	}

	public String getTechnicalName() {
		return "calendar_box";
	}

	public String getName() {
		return "Calendar";
	}

	public JComponent getView() {
		return this;
	}
}
