package org.columba.mail.search;

import java.net.URI;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.plugin.PluginLoadingFailedException;
import org.columba.core.command.CommandProcessor;
import org.columba.core.filter.FilterCriteria;
import org.columba.core.gui.frame.FrameManager;
import org.columba.core.gui.search.api.IResultPanel;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.search.SearchCriteria;
import org.columba.core.search.api.ISearchCriteria;
import org.columba.core.search.api.ISearchProvider;
import org.columba.core.search.api.ISearchResult;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.filter.MailFilterFactory;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.search.BasicResultPanel;
import org.columba.mail.gui.table.command.ViewHeaderListCommand;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.message.IHeaderList;
import org.columba.mail.resourceloader.IconKeys;
import org.columba.mail.resourceloader.MailImageLoader;
import org.columba.ristretto.message.Address;
import org.columba.ristretto.message.Flags;


/**
 * Search provider uses virtual folder to search over all existing mail folders.
 * 
 * @author frd
 */
public class MailSearchProvider implements ISearchProvider {
	private static final String CRITERIA_BODY_CONTAINS = "body_contains";

	public static final String CRITERIA_FROM_CONTAINS = "from_contains";

	private static final String CRITERIA_SIZE_GREATER_THAN = "size_greater_than";

	private static final String CRITERIA_SIZE_SMALLER_THAN = "size_smaller_than";

	private static final String CRITERIA_SUBJECT_CONTAINS = "subject_contains";

	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.search.MailSearchProvider");

	private Hashtable<Integer, IMailbox> folderTable = new Hashtable<Integer, IMailbox>();

	private Vector<SearchIndex> indizes = new Vector<SearchIndex>();

	private Map<String, VirtualFolder> searchFolders = new Hashtable<String, VirtualFolder>();

	private int totalResultCount = 0;

	private ResourceBundle bundle;

	public MailSearchProvider() {
		bundle = ResourceBundle.getBundle("org.columba.mail.i18n.search");

	}

	public String getTechnicalName() {
		return "MailSearchProvider";
	}

	public String getName() {
		return bundle.getString("provider_title");
	}

	public String getDescription() {
		return bundle.getString("provider_description");
	}

	public ImageIcon getIcon() {
		return MailImageLoader.getSmallIcon(IconKeys.MESSAGE_READ);
	}

	public List<ISearchCriteria> getAllCriteria(String searchTerm) {
		List<ISearchCriteria> list = new Vector<ISearchCriteria>();

		// check if string is a number
		boolean numberFormat = false;
		try {
			int searchTermInt = Integer.parseInt(searchTerm);
			numberFormat = true;
		} catch (NumberFormatException e) {
		}

		list.add(getCriteria(MailSearchProvider.CRITERIA_BODY_CONTAINS,
				searchTerm));
		list.add(getCriteria(MailSearchProvider.CRITERIA_SUBJECT_CONTAINS,
				searchTerm));
		list.add(getCriteria(MailSearchProvider.CRITERIA_FROM_CONTAINS,
				searchTerm));
		if (numberFormat)
			list.add(getCriteria(MailSearchProvider.CRITERIA_SIZE_GREATER_THAN,
					searchTerm));
		if (numberFormat)
			list.add(getCriteria(MailSearchProvider.CRITERIA_SIZE_SMALLER_THAN,
					searchTerm));

		return list;
	}

	public IResultPanel getResultPanel(String searchCriteriaTechnicalName) {
		return new BasicResultPanel(getTechnicalName(),
				searchCriteriaTechnicalName);
	}

	public ISearchCriteria getCriteria(String technicalName, String searchTerm) {

		String title = MessageFormat.format(bundle.getString(technicalName
				+ "_title"), new Object[] { searchTerm });
		String description = MessageFormat.format(bundle
				.getString(technicalName + "_description"),
				new Object[] { searchTerm });

		return new SearchCriteria(technicalName, title, description);
	}

	public List<ISearchResult> query(String searchTerm,
			String searchCriteriaTechnicalName, boolean searchInside,
			int startIndex, int resultCount) {
		List<ISearchResult> result = new Vector<ISearchResult>();

		indizes = new Vector<SearchIndex>();

		// create search criteria

		FilterCriteria criteria = null;
		if (searchCriteriaTechnicalName
				.equals(MailSearchProvider.CRITERIA_BODY_CONTAINS)) {
			criteria = MailFilterFactory.createBodyContains(searchTerm);
		} else if (searchCriteriaTechnicalName
				.equals(MailSearchProvider.CRITERIA_SUBJECT_CONTAINS)) {
			criteria = MailFilterFactory.createSubjectContains(searchTerm);
		} else if (searchCriteriaTechnicalName
				.equals(MailSearchProvider.CRITERIA_FROM_CONTAINS)) {
			criteria = MailFilterFactory.createFromContains(searchTerm);
		} else if (searchCriteriaTechnicalName
				.equals(MailSearchProvider.CRITERIA_SIZE_GREATER_THAN)) {
			criteria = MailFilterFactory.createSizeIsBigger(Integer
					.parseInt(searchTerm));
		} else if (searchCriteriaTechnicalName
				.equals(MailSearchProvider.CRITERIA_SIZE_SMALLER_THAN)) {
			criteria = MailFilterFactory.createSizeIsSmaller(Integer
					.parseInt(searchTerm));
		} else
			throw new IllegalArgumentException("no criteria <"
					+ searchCriteriaTechnicalName + "> found");

		// return empty result, in case the criteria doesn't match the search
		// term
		if (criteria == null)
			return result;
		try {

			VirtualFolder folder = null;
			// create virtual folder for each criteria
			IMailFolder rootFolder = (IMailFolder) FolderTreeModel.getInstance().getRoot();

			if (searchInside
					&& searchFolders.containsKey(searchCriteriaTechnicalName)) {
				VirtualFolder vFolder = searchFolders
						.get(searchCriteriaTechnicalName);
				folder = SearchFolderFactory.prepareSearchFolder(criteria,
						vFolder);
			} else {
				folder = SearchFolderFactory.prepareSearchFolder(criteria,
						rootFolder);
			}

			// do the search
			IHeaderList headerList = folder.getHeaderList();

			Object[] uids = headerList.getUids();

			for (int i = 0; i < uids.length; i++) {
				SearchIndex idx = new SearchIndex(folder, uids[i]);
				indizes.add(idx);
			}

			// retrieve the actual search result data
			List<ISearchResult> l = retrieveResultData(indizes, startIndex,
					resultCount);
			result.addAll(l);

			// sort all the results
			Collections.sort(result, new MyComparator());

			// remember search folder for "show total results" action
			searchFolders.put(searchCriteriaTechnicalName, folder);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// memorize total result count
		totalResultCount = indizes.size();

		return result;
	}

	private List<ISearchResult> retrieveResultData(Vector<SearchIndex> indizes,
			int startIndex, int resultCount) throws Exception {
		// ensure we are in existing result range
		int count = (startIndex + resultCount <= indizes.size()) ? resultCount
				: indizes.size();
		List<ISearchResult> result = new Vector<ISearchResult>();
		// gather result results
		for (int i = startIndex; i < count; i++) {
			SearchIndex idx = indizes.get(i);
			IMailbox folder = idx.folder;
			Object messageId = idx.messageId;

			// TODO @author fdietz: ensure that we don't fetch individual
			// headers
			// to reduce client/server roundtrips

			String title = (String) folder.getAttribute(messageId,
					"columba.subject");
			Address from = (Address) folder.getAttribute(messageId,
					"columba.from");
			Date date = (Date) folder.getAttribute(messageId, "columba.date");
			String description = from.toString() + " " + date;
			URI uri = SearchResultBuilder.createURI(folder.getId(), messageId);

			ImageIcon statusIcon = null;
			Flags flags = folder.getFlags(messageId);
			if (flags.getDeleted()) {
				statusIcon = ImageLoader.getSmallIcon("user-trash.png");

			} else if (flags.getAnswered()) {
				statusIcon = MailImageLoader
						.getSmallIcon("message-mail-replied.png");
			} else if (flags.getDraft()) {
				statusIcon = MailImageLoader.getSmallIcon("edit.png");
			} else if (!flags.getSeen()) {
				statusIcon = MailImageLoader
						.getSmallIcon("message-mail-unread.png");
			} else if (flags.getSeen()) {
				statusIcon = MailImageLoader
						.getSmallIcon("message-mail-read.png");
			}

			String dateString = new DateHelper().format(date);
			
			result.add(new MailSearchResult(title, description, uri,
					dateString, date, from, statusIcon, flags.getFlagged()));
		}
		return result;
	}

	class DateHelper {
		SimpleDateFormat dfWeek = new SimpleDateFormat("EEE HH:mm", Locale
				.getDefault());

		// use local date settings
		DateFormat dfCommon = DateFormat.getDateInstance();

		static final long OneDay = 24 * 60 * 60 * 1000;

		TimeZone localTimeZone = TimeZone.getDefault();

		private int getLocalDaysDiff(long t) {
			return (int) (((System.currentTimeMillis() + localTimeZone
					.getRawOffset()) - (((t + localTimeZone.getRawOffset()) / OneDay) * OneDay)) / OneDay);
		}

		public String format(Date date) {
			String dateString = null;

			int diff = getLocalDaysDiff(date.getTime());

			// if ( today
			if ((diff >= 0) && (diff < 7)) {
				dateString = dfWeek.format(date);
			} else {
				dateString = dfCommon.format(date);
			}
			return dateString;
		}

	}

	class SearchIndex {
		IMailbox folder;

		Object messageId;

		SearchIndex(IMailbox folder, Object messageId) {
			this.folder = folder;
			this.messageId = messageId;
		}
	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void showAllResults(IFrameMediator mediator, String searchTerm,
			String searchCriteriaTechnicalName) {

		VirtualFolder vFolder = searchFolders.get(searchCriteriaTechnicalName);
		if (vFolder == null)
			throw new IllegalArgumentException("vFolder for search critera <"
					+ searchCriteriaTechnicalName + "> not found");

		// ensure that we are currently in the mail component
		IFrameMediator newMediator = null;
		try {
			newMediator = FrameManager.getInstance().switchView(
					mediator.getContainer(), "ThreePaneMail");
		} catch (PluginLoadingFailedException e) {
			e.printStackTrace();
		}

		// select invisible virtual folder
		((TreeViewOwner) newMediator).getTreeController().setSelected(vFolder);

		// update message list
		CommandProcessor.getInstance().addOp(
				new ViewHeaderListCommand(newMediator,
						new MailFolderCommandReference(vFolder)));
	}

	/**
	 * Comparator for message result. Sortes results by Date. More recent
	 * results are shown first.
	 * 
	 * @author frd
	 */
	class MyComparator implements Comparator {
		MyComparator() {
		}

		public int compare(Object o1, Object o2) {
			MailSearchResult result = (MailSearchResult) o1;
			MailSearchResult result2 = (MailSearchResult) o2;

			Date date = result.getDate();
			Date date2 = result2.getDate();

			if (date == null && date2 == null)
				return 0;
			if (date != null && date2 == null)
				return -1;
			if (date == null && date2 != null)
				return 1;

			return -date.compareTo(date2);
		}

	}
}
