package org.columba.mail.gui.tagging;

import java.util.EventListener;

import org.columba.core.tagging.api.ITag;

/**
 * 
 * Interface for the TagList Events
 * 
 * @author hubms
 *
 */

public interface ITagListListener extends EventListener {
	
	public abstract void selectionChanged(ITag tag); 

}
