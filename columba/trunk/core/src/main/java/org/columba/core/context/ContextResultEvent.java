package org.columba.core.context;

import java.util.EventObject;

import org.columba.core.context.api.IContextResultEvent;

public class ContextResultEvent extends EventObject implements
		IContextResultEvent {

	
	private String providerName;
	
	public ContextResultEvent(Object source) {
		super(source);
	}
	
	public ContextResultEvent(Object source, String providerName) {
		super(source);
		
		this.providerName = providerName;
	}
	
	public String getProviderName() {
		return providerName;
	}

}
