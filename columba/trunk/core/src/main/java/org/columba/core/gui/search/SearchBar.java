package org.columba.core.gui.search;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.columba.core.gui.search.api.ISearchPanel;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.search.ResultListenerAdapter;
import org.columba.core.search.SearchHistoryList;
import org.columba.core.search.api.IResultEvent;
import org.columba.core.search.api.ISearchCriteria;
import org.columba.core.search.api.ISearchManager;
import org.columba.core.search.api.ISearchProvider;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class SearchBar extends JPanel implements  PopupMenuListener {

	private IconTextField textField;

	private ImageIcon icon = ImageLoader.getSmallIcon(IconKeys.EDIT_FIND);

	private JButton searchButton;

	private JCheckBox searchInsideCheckBox;

	private ActionListener listener;

	private ISearchPanel searchPanel;

	public SearchBar(ISearchPanel searchPanel, boolean showSearchButton, boolean showSearchInsideButton) {
		super();

		this.searchPanel = searchPanel;

		textField = new IconTextField(icon, 20);
		textField.addPopupMenuListener(this);

		searchButton = new JButton("Search");
		searchButton.setMnemonic('s');

		searchInsideCheckBox = new JCheckBox("Search Inside");
		searchInsideCheckBox
				.setToolTipText("Search Inside Previous Search Results");
		searchInsideCheckBox.setMnemonic('i');
		searchInsideCheckBox.setSelected(false);

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchButton.setEnabled(false);
				SearchBar.this.searchPanel.searchAll(textField.getText(),
						searchInsideCheckBox.isSelected());
			}
		});

		// searchInsideCheckBox.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// SearchBar.this.searchPanel.searchAll(textField.getText(), true);
		// }
		// });

		FormLayout layout = new FormLayout(
				"fill:default:grow, 3dlu, pref, 3dlu, pref",
				// 2 columns
				"");

		// create a form builder
		DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);

		builder.append(textField);
		if (showSearchButton) 
			builder.append(searchButton);
		
		if (showSearchInsideButton)
			builder.append(searchInsideCheckBox);

		textField.addKeyListener(new MyKeyListener());

		searchPanel.getSearchManager()
				.addResultListener(new MyResultListener());
	}

	public void addActionListener(ActionListener listener) {
		this.listener = listener;

		searchButton.addActionListener(listener);
	}

	class MyKeyListener extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			char ch = e.getKeyChar();

			if (ch == KeyEvent.VK_ENTER) {
				searchButton.setEnabled(false);
				searchPanel.searchAll(textField.getText(), searchInsideCheckBox
						.isSelected());
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				textField.showPopup();
			}
		}
		
	}
	

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		// update popup menu based on searchterm
		updatePopupMenu(textField.getPopupMenu(), textField.getText());
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
	}

	private void updatePopupMenu(JPopupMenu menu, String searchTerm) {
		menu.removeAll();

		// add menuitem to search across all components
		JMenuItem m2 = new JMenuItem("Search All");
		m2.setToolTipText("Search across all components");
		m2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchPanel.searchAll(textField.getText(), searchInsideCheckBox
						.isSelected());
			}
		});
		menu.add(m2);
		menu.addSeparator();

		ISearchManager manager = searchPanel.getSearchManager();
		Iterator<ISearchProvider> it = manager.getAllProviders();
		while (it.hasNext()) {
			final ISearchProvider p = it.next();

			// create a single menu item for all the search criteria
			// of this provider
			JMenuItem m = new JMenuItem(p.getName());
			m.setToolTipText(p.getDescription());
			m.setIcon(p.getIcon());
			m.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					searchPanel.searchInProvider(textField.getText(), p
							.getTechnicalName());
				}
			});
			menu.add(m);

			// create all individual search criteria for this provider
			List<ISearchCriteria> v = p.getAllCriteria(searchTerm);
			Iterator<ISearchCriteria> it2 = v.iterator();
			while (it2.hasNext()) {
				final ISearchCriteria c = it2.next();
				if (c == null)
					continue;

				m = new JMenuItem(c.getTitle());
				m.setToolTipText(c.getDescription());
				m.setIcon(p.getIcon());
				m.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						searchPanel.searchInCriteria(textField.getText(), p
								.getTechnicalName(), c.getTechnicalName());
					}
				});
				menu.add(m);
			}

			if (it.hasNext())
				menu.addSeparator();
		}

		// create search history

		Map<String, ISearchProvider> historyMap = SearchHistoryList
				.getInstance().getHistoryMap();

		// if (historyMap.size() > 0) {
		// Iterator<String> it3 = historyMap.keySet().iterator();
		// while (it3.hasNext()) {
		// String term = it3.next();
		// ISearchProvider p = historyMap.get(term);
		// ISearchCriteria c = p.getAllCriteria(term);
		// if (c == null)
		// continue;
		//
		// JMenuItem m = new JMenuItem(c.getTitle());
		// m.setToolTipText(c.getDescription());
		// m.setIcon(c.getIcon());
		// m.setActionCommand(p.getName());
		// m.addActionListener(listener);
		// menu.add(m);
		// }
		//
		// menu.insert(new JSeparator(), menu.getComponentCount() -
		// historyMap.size());
		// }
	}

	public void install(JMenuBar menubar) {
		if (menubar == null)
			throw new IllegalArgumentException("menubar == null");

		Component box = Box.createHorizontalGlue();
		menubar.add(box);

		menubar.add(this);
	}

	public void install(JToolBar toolbar) {
		if (toolbar == null)
			throw new IllegalArgumentException("toolbar");

		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEmptyBorder(3, 10, 2, 0));
		p.setLayout(new BorderLayout());
		p.add(this, BorderLayout.CENTER);

		toolbar.add(p);

	}

	public String getSearchTerm() {
		return textField.getText();
	}

	class MyResultListener extends ResultListenerAdapter {

		MyResultListener() {}

		@Override
		public void finished(IResultEvent event) {
			// search is finished
			// -> enable search button again
			searchButton.setEnabled(true);
		}

	}

}
