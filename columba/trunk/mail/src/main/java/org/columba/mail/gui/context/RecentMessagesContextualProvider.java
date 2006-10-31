package org.columba.mail.gui.context;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.context.api.IContextProvider;
import org.columba.core.context.base.api.IStructureValue;
import org.columba.core.context.semantic.api.ISemanticContext;
import org.columba.core.search.api.ISearchResult;
import org.columba.mail.gui.search.ResultList;
import org.columba.mail.resourceloader.IconKeys;
import org.columba.mail.resourceloader.MailImageLoader;
import org.columba.mail.search.MailSearchProvider;

public class RecentMessagesContextualProvider implements IContextProvider {
	private ResourceBundle bundle;

	private ResultList list;

	private MailSearchProvider p;

	private List<ISearchResult> result = new Vector<ISearchResult>();

	private String emailAddress;

	public RecentMessagesContextualProvider() {
		bundle = ResourceBundle.getBundle("org.columba.mail.i18n.search");

		list = new ResultList();
	}

	public String getTechnicalName() {
		return "recent_messages_of_contact";
	}
	
	public String getName() {
		return bundle.getString("provider_related_title");
	}

	public String getDescription() {
		return bundle.getString("provider_related_title");
	}

	public ImageIcon getIcon() {
		return MailImageLoader.getSmallIcon(IconKeys.MESSAGE_READ);
	}

	public int getTotalResultCount() {
		return p.getTotalResultCount();
	}

	public void search(ISemanticContext context, int startIndex, int resultCount) {

		IStructureValue value = context.getValue();
		if (value == null)
			return;

		Iterator<IStructureValue> it = value.getChildIterator(
				ISemanticContext.CONTEXT_NODE_IDENTITY,
				ISemanticContext.CONTEXT_NAMESPACE_CORE);
		// can be only one
		IStructureValue identity = it.next();
		if (identity == null)
			return;

		emailAddress = identity.getString(
				ISemanticContext.CONTEXT_ATTR_EMAIL_ADDRESS,
				ISemanticContext.CONTEXT_NAMESPACE_CORE);

		if (emailAddress == null)
			return;

		p = new MailSearchProvider();
		List<ISearchResult> r = p.query(emailAddress,
				MailSearchProvider.CRITERIA_FROM_CONTAINS, false, 0, 5);

		result.addAll(r);
	}

	public void showResult() {
		list.addAll(result);
	}

	public JComponent getView() {
		return list;
	}

	public void clear() {

		list.clear();
	}

	public boolean isEnabledShowMoreLink() {
		return true;
	}

	public void showMoreResults(IFrameMediator mediator) {
		if (emailAddress == null)
			return;

		// show all search results
		p.showAllResults(mediator, emailAddress,
				MailSearchProvider.CRITERIA_FROM_CONTAINS);
	}
}
