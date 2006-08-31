package org.columba.core.tagging.api;

import java.util.Iterator;

public interface ITagManager {

	ITag getTag(String id);
	Iterator<ITag> getAllTags();
	ITag addTag(String name);
	void removeTag(String id);

}
