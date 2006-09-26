package org.columba.core.tagging.api;

import java.util.Hashtable;

public interface ITag {
	
	String getId();
	
	String getProperty(String name);
	void setProperty(String name, String value);
	Hashtable getProperties();
	
}
