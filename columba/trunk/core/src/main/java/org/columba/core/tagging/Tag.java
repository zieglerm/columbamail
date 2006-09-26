package org.columba.core.tagging;

import java.util.Hashtable;

import org.columba.core.tagging.api.ITag;

public class Tag implements ITag {
	
	private String id;
	private Hashtable properties = new Hashtable();
	
	
	public Tag(String newId) {
		this.id = newId;
	}

	public String getId() {
		return id;
	}

	public String getProperty(String name) {
		return (String) properties.get(name);
	}

	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public Hashtable getProperties() {
		return properties;
	}

}
