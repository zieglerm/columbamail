/**
 * This is an implementation of the IViewableText for the MessageController.
 * The functions do the same as in the JTextPane class. 
 * @author Dmytro Podalyuk
 */
package org.columba.core.util;

import org.columba.mail.gui.message.MessageController;

public class MessageViewerText implements IViewableText {
	MessageController controller;
	public MessageViewerText(MessageController controller){
		this.controller = controller;
	}

	public void setCaretPosition(int position){
		controller.setCaretPosition(position);
	}

	public void moveCaretPosition(int position){
		controller.moveCaretPosition(position);
	}

	public String getText(){
		return controller.getText();
	}

	public void grabFocus() {
		controller.grabFocus();
	}

}
