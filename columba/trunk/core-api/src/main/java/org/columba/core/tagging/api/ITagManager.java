package org.columba.core.tagging.api;

import java.util.Hashtable;
import java.util.Iterator;

import org.columba.api.exception.StoreException;

public interface ITagManager {

	ITag getTag(String id);
	Iterator<ITag> getAllTags();
	
	ITag addTag(String name) throws StoreException;
	void removeTag(String id) throws StoreException;
	void removeTagById(String id) throws StoreException;
	
	void setProperty(ITag tag, String name, String value) throws StoreException;
	String getProperty(ITag tag, String name);
	Hashtable getProperties(ITag tag);

	public void addTagListener(ITagListener l);
	public void removeTagListener(ITagListener l);
}
