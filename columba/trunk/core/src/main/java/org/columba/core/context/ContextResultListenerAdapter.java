package org.columba.core.context;

import org.columba.core.context.api.IContextResultEvent;
import org.columba.core.context.api.IContextResultListener;

public abstract class ContextResultListenerAdapter implements IContextResultListener {

	
	public void finished(IContextResultEvent event) {
	}

	public void resultArrived(IContextResultEvent event) {
	}

}
