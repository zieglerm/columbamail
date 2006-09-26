package org.columba.core.tagging.api;

import java.util.Hashtable;
import java.util.Iterator;

public interface ITagManager {

	ITag getTag(String id);
	Iterator<ITag> getAllTags();
	ITag addTag(String name);
	void removeTag(String id);
	
	void setProperty(ITag tag, String name, String value);
	String getProperty(ITag tag, String name);
	Hashtable getProperties(ITag tag);

}
