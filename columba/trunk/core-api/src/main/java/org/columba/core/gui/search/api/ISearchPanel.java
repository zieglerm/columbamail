package org.columba.core.gui.search.api;

import javax.swing.JComponent;

import org.columba.core.search.api.ISearchManager;

public interface ISearchPanel {

	public void searchAll(String searchTerm,  boolean searchInside);

	public void searchInProvider(String searchTerm, String providerName);

	public void searchInCriteria(String searchTerm, String providerName,
			String criteriaName);
	
	public ISearchManager getSearchManager();
	
	public JComponent getView();

}