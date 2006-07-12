/**
 * This is an implementation of the IEditableText for the ComposerController.
 * The functions do the same as in the JTextPane class. 
 * @author Dmytro Podalyuk
 */
package org.columba.core.util;

import org.columba.mail.gui.composer.ComposerController;

public class ComposerText implements IEditableText {

	ComposerController controller;
	public ComposerText(ComposerController controller){
		this.controller = controller;
	}

	public void setCaretPosition(int position){
		controller.getEditorController().getViewUIComponent().setCaretPosition(position);
	}

	public void moveCaretPosition(int position){
		controller.getEditorController().getViewUIComponent().moveCaretPosition(position);
	}

	public String getText(){
		return controller.getEditorController().getViewUIComponent().getText();
	}

	public void setText(String text){
		controller.getEditorController().getViewUIComponent().setText(text);
	}

	public void grabFocus() {
		controller.getEditorController().getViewUIComponent().grabFocus();
	}
}
