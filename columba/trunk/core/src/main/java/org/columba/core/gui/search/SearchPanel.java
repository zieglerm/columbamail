package org.columba.core.gui.search;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.columba.api.gui.frame.IDock;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.api.plugin.PluginException;
import org.columba.api.plugin.PluginHandlerNotFoundException;
import org.columba.core.context.ContextSearchManager;
import org.columba.core.context.api.IContextProvider;
import org.columba.core.context.api.IContextSearchManager;
import org.columba.core.gui.context.ContextDebugProvider;
import org.columba.core.gui.context.ContextResultBox;
import org.columba.core.gui.search.api.IResultPanel;
import org.columba.core.gui.search.api.ISearchPanel;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.PluginManager;
import org.columba.core.search.SearchManager;
import org.columba.core.search.api.ISearchCriteria;
import org.columba.core.search.api.ISearchManager;
import org.columba.core.search.api.ISearchProvider;

public class SearchPanel extends JPanel implements ISearchPanel {

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.search.gui.SearchPanel");

	private IFrameMediator frameMediator;

	// private SearchResultView searchResultView;

	private StackedBox box;

	private Hashtable<String, SearchResultBox> searchMap = new Hashtable<String, SearchResultBox>();

	private ISearchManager searchManager;

	private Map<String, ContextResultBox> contextMap = new Hashtable<String, ContextResultBox>();

	private IContextSearchManager contextSearchManager;

	public SearchPanel(IFrameMediator frameMediator) {
		super();

		this.frameMediator = frameMediator;

		this.searchManager = new SearchManager();
		initSearchProvider();

		contextSearchManager = new ContextSearchManager(frameMediator);
		initContextProvider();

		setBackground(UIManager.getColor("TextField.background"));

		box = new StackedBox();
		box.setBackground(UIManager.getColor("TextField.background"));
		JScrollPane pane = new JScrollPane(box);
		pane.setBorder(null);
		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);

	}

	private void initContextProvider() {

		// if in debug mode, register context debugger view
		if (Logging.DEBUG)
			contextSearchManager.register(new ContextDebugProvider());

		try {

			IExtensionHandler handler = PluginManager
					.getInstance()
					.getExtensionHandler(
							IExtensionHandlerKeys.ORG_COLUMBA_CORE_CONTEXT_PROVIDER);

			String[] ids = handler.getPluginIdList();
			for (int i = 0; i < ids.length; i++) {
				try {
					IExtension extension = handler.getExtension(ids[i]);

					IContextProvider provider = (IContextProvider) extension
							.instanciateExtension(null);
					contextSearchManager.register(provider);
				} catch (PluginException e) {
					LOG.severe("Error while loading plugin: " + e.getMessage());
					if (Logging.DEBUG)
						e.printStackTrace();
				}
			}

		} catch (PluginHandlerNotFoundException e) {
			LOG.severe("Error while loading plugin: " + e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		}
	}

	private void initSearchProvider() {
		List<ISearchProvider> list = new Vector<ISearchProvider>();
		try {

			IExtensionHandler handler = PluginManager.getInstance()
					.getExtensionHandler(
							IExtensionHandlerKeys.ORG_COLUMBA_CORE_SEARCH);

			String[] ids = handler.getPluginIdList();
			for (int i = 0; i < ids.length; i++) {
				try {
					IExtension extension = handler.getExtension(ids[i]);

					ISearchProvider provider = (ISearchProvider) extension
							.instanciateExtension(null);
					searchManager.registerProvider(provider);
				} catch (PluginException e) {
					LOG.severe("Error while loading plugin: " + e.getMessage());
					if (Logging.DEBUG)
						e.printStackTrace();
				}
			}

		} catch (PluginHandlerNotFoundException e) {
			LOG.severe("Error while loading plugin: " + e.getMessage());
			if (Logging.DEBUG)
				e.printStackTrace();
		}

	}

	// search individual provider and individual criteria
	public void searchInCriteria(String searchTerm, String providerName,
			String criteriaName) {

		showSearchDockingView();

		ISearchManager manager = searchManager;

		// start a new search -> clear all previous search results
		manager.clearSearch(searchTerm);
		manager.reset();

		createStackedBox(searchTerm, providerName, criteriaName);

		// TODO @author fdietz: no paging used currently
		// show only first 5 results
		manager.executeSearch(searchTerm, providerName, criteriaName, 0, 5);
	}

	// search individual provider
	public void searchInProvider(String searchTerm, String providerName) {

		showSearchDockingView();

		ISearchManager manager = searchManager;

		// start a new search -> clear all previous search results
		manager.clearSearch(searchTerm);
		manager.reset();

		createStackedBox(searchTerm, providerName, null);

		// TODO @author fdietz: no paging used currently
		// show only first 5 results
		manager.executeSearch(searchTerm, providerName, 0, 5);
	}

	// search across all providers
	public void searchAll(String searchTerm, boolean searchInside) {

		showSearchDockingView();

		ISearchManager manager = searchManager;

		// start a new search -> clear all previous search results
		manager.clearSearch(searchTerm);
		manager.reset();

		createStackedBox(searchTerm, null, null);

		// TODO @author fdietz: no paging used currently
		// show only first 5 results
		manager.executeSearch(searchTerm, searchInside, 0, 5);
	}

	// create new stacked box
	private void createStackedBox(String searchTerm, String providerName,
			String criteriaName) {
		if (searchTerm == null)
			throw new IllegalArgumentException("searchTerm == null");

		box.removeAll();

		// search across all providers
		boolean providerAll = (providerName == null) ? true : false;
		// search all criteria in specific provider only
		boolean providerSearch = (providerName != null) ? true : false;
		// search in specific criteria
		boolean criteriaSearch = (criteriaName != null && providerName != null) ? true
				: false;

		ISearchManager manager = searchManager;

		if (criteriaSearch) {
			// query with only a single criteria

			ISearchProvider p = manager.getProvider(providerName);

			ISearchCriteria c = p.getCriteria(criteriaName, searchTerm);

			createResultPanel(p, c);

		} else if (providerSearch) {

			// query only a single provider

			ISearchProvider p = manager.getProvider(providerName);

			Iterator<ISearchCriteria> it2 = p.getAllCriteria(searchTerm)
					.iterator();
			while (it2.hasNext()) {
				ISearchCriteria c = it2.next();
				createResultPanel(p, c);
			}

		} else if (providerAll) {
			// query all criteria of all providers

			Iterator<ISearchProvider> it = manager.getAllProviders();
			while (it.hasNext()) {
				ISearchProvider p = it.next();
				if (p == null)
					continue;

				Iterator<ISearchCriteria> it2 = p.getAllCriteria(searchTerm)
						.iterator();
				while (it2.hasNext()) {
					ISearchCriteria c = it2.next();
					createResultPanel(p, c);
				}
			}
		}

		// repaint box
		validate();
		repaint();
	}

	private void createResultPanel(ISearchProvider p, ISearchCriteria c) {
		// retrieve result panel for search criteria
		// IResultPanel resultPanel =
		// loadResultPanelExtension(p.getTechnicalName(),
		// c.getTechnicalName());

		IResultPanel resultPanel = p.getResultPanel(c.getTechnicalName());

		// fall-back to default result panel (html based viewer)
		if (resultPanel == null)
			resultPanel = new GenericResultPanel(p.getTechnicalName(), c
					.getTechnicalName());

		// add result panel as listener for new search results
		searchManager.addResultListener(resultPanel);

		// create visual container for result panel
		SearchResultBox resultBox = new SearchResultBox(frameMediator, p, c,
				resultPanel);
		resultBox.installListener(searchManager);

		// add to search panel
		box.add(resultBox);
	}

	// show search docking view
	private void showSearchDockingView() {
		if (frameMediator instanceof IDock) {
			// show docking view
			((IDock) frameMediator).showDockable(IDock.DOCKING_VIEW_SEARCH);
		}

	}

	/**
	 * @see org.columba.core.gui.search.api.ISearchPanel#getView()
	 */
	public JComponent getView() {
		return this;
	}

	public ISearchManager getSearchManager() {
		return searchManager;
	}

	public IContextSearchManager getContextSearchManager() {
		return contextSearchManager;
	}

	public void searchInContext() {
		// init result view
		createContextStackedBox();

		// execute background search
		contextSearchManager.search();
	}

	private void createContextStackedBox() {

		searchMap.clear();

		box.removeAll();

		Iterator<IContextProvider> it = contextSearchManager.getAllProviders();
		while (it.hasNext()) {
			IContextProvider p = it.next();

			// clear previous search results
			p.clear();
			ContextResultBox resultBox = new ContextResultBox(frameMediator, p,
					contextSearchManager);
			box.addBox(resultBox);

			contextMap.put(p.getName(), resultBox);
		}

		// repaint box
		validate();
		repaint();
	}

}
