package org.columba.core.tagging;

import org.columba.core.tagging.api.ITag;

public class Tag implements ITag {
	
	private String id;
	private String name;
	
	public Tag(String newId) {
		this.id = newId;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

}
