package org.columba.mail.gui.tagging;

import java.util.Observable;

public class ExtendedObservable extends Observable {
	
	public void setChanged(boolean b) {
		if (b)
			this.setChanged();
	}

}
