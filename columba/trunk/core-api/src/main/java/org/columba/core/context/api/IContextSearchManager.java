package org.columba.core.context.api;

import java.util.Iterator;



public interface IContextSearchManager {
	
	public void search();

	public IContextProvider getProvider(String technicalName);
	public void register(IContextProvider provider);
	public void unregister(IContextProvider provider);
	
	public Iterator<IContextProvider> getAllProviders();
	
	public void addResultListener(IContextResultListener listener);
	public void removeResultListener(IContextResultListener listener);
}
