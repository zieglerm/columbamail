package org.columba.addressbook.gui.context;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.columba.addressbook.model.IContactModel;
import org.columba.addressbook.model.PhoneModel;
import org.columba.core.gui.base.RoundedBorder;
import org.columba.core.resourceloader.ImageLoader;
import org.jdesktop.swingx.JXHyperlink;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class ContactDetailPanel extends JPanel {

	private JLabel pictureLabel;

	private JLabel label1;

	private JXHyperlink label2;

	private JXHyperlink label5;

	private JLabel label9;

	private JLabel label10;

	private JLabel label12;

	private JLabel label13;

	private JLabel label4;

	private JLabel label11;

	private JLabel label7;

	private JLabel label8;

	private IContactModel model;

	public ContactDetailPanel(IContactModel model) {

		setBackground(Color.white);

		setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(new Color(
				220, 220, 220)), BorderFactory
				.createEmptyBorder(10, 10, 10, 10) ));

		initComponents();

		pictureLabel.setIcon(ImageLoader.getMiscIcon("malehead.png"));
		pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		pictureLabel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(new Color(
				220, 220, 220)), BorderFactory
				.createEmptyBorder(10, 10, 10, 10) ));
		
		label1.setText(model.getFormattedName());
		label1.setFont(label1.getFont().deriveFont(Font.BOLD));
		label2.setText(model.getPreferredEmail());
		label5.setText(model.getHomePage());
		label9.setText("Birthday:");
		if (model.getBirthday() != null)
			label10.setText(model.getBirthday().toLocaleString());

		label12.setText("Phone Home:");
		label4.setText("Phone Work:");
		Iterator it = model.getPhoneIterator();

		while (it.hasNext()) {
			PhoneModel phoneModel = (PhoneModel) it.next();
			if (phoneModel.getType() == PhoneModel.TYPE_HOME_PHONE)
				label13.setText(phoneModel.getNumber());
			if (phoneModel.getType() == PhoneModel.TYPE_BUSINESS_PHONE)
				label11.setText(phoneModel.getNumber());
		}

		if (model.getPreferredInstantMessaging() != null)
			label7.setText(model.getPreferredInstantMessaging() + ":");
		else
			label7.setText("No IM available");

		// TODO: real IM status here
		label8.setText("Status");
	}

	private void initComponents() {
		pictureLabel = new JLabel();
		label1 = new JLabel();
		label2 = new JXHyperlink();
		label5 = new JXHyperlink();
		label9 = new JLabel();
		label10 = new JLabel();
		label12 = new JLabel();
		label13 = new JLabel();
		label4 = new JLabel();
		label11 = new JLabel();
		label7 = new JLabel();
		label8 = new JLabel();

		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC }));
		add(pictureLabel, cc.xywh(1, 1, 1, 7, CellConstraints.FILL,
				CellConstraints.FILL));

		add(label1, cc.xy(3, 1));

		add(label2, cc.xy(3, 3));

		add(label5, cc.xy(3, 5));

		add(label9, cc.xywh(1, 9, 1, 1, CellConstraints.RIGHT,
				CellConstraints.DEFAULT));

		add(label10, cc.xywh(3, 9, 1, 1, CellConstraints.FILL,
				CellConstraints.DEFAULT));

		add(label12, cc.xywh(1, 11, 1, 1, CellConstraints.RIGHT,
				CellConstraints.DEFAULT));

		add(label13, cc.xywh(3, 11, 1, 1, CellConstraints.FILL,
				CellConstraints.DEFAULT));

		add(label4, cc.xywh(1, 13, 1, 1, CellConstraints.RIGHT,
				CellConstraints.DEFAULT));

		add(label11, cc.xywh(3, 13, 1, 1, CellConstraints.FILL,
				CellConstraints.DEFAULT));

		add(label7, cc.xywh(1, 15, 1, 1, CellConstraints.RIGHT,
				CellConstraints.DEFAULT));

		add(label8, cc.xywh(3, 15, 1, 1, CellConstraints.FILL,
				CellConstraints.DEFAULT));
	}

}
