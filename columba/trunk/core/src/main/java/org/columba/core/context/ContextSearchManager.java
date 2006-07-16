package org.columba.core.context;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.command.Command;
import org.columba.core.command.CommandProcessor;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.context.api.IContextProvider;
import org.columba.core.context.api.IContextResultEvent;
import org.columba.core.context.api.IContextResultListener;
import org.columba.core.context.api.IContextSearchManager;

public class ContextSearchManager implements IContextSearchManager {

	private Map<String, IContextProvider> map = new Hashtable<String, IContextProvider>();

	private IFrameMediator frameMediator;

	protected EventListenerList listenerList = new EventListenerList();

	public ContextSearchManager(IFrameMediator frameMediator) {
		this.frameMediator = frameMediator;
	}

	public void register(IContextProvider provider) {
		map.put(provider.getTechnicalName(), provider);
	}

	public void unregister(IContextProvider provider) {
		map.remove(provider.getTechnicalName());
	}

	public IContextProvider getProvider(String technicalName) {
		return map.get(technicalName);
	}

	public Iterator<IContextProvider> getAllProviders() {
		return map.values().iterator();
	}

	public void addResultListener(IContextResultListener listener) {
		listenerList.add(IContextResultListener.class, listener);
	}

	public void removeResultListener(IContextResultListener listener) {
		listenerList.remove(IContextResultListener.class, listener);
	}

	public void search() {
		SearchCommand command = new SearchCommand(new DefaultCommandReference());
		CommandProcessor.getInstance().addOp(command);
	}

	private void fireFinished() {
		IContextResultEvent e = new ContextResultEvent(this);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IContextResultListener.class) {
				((IContextResultListener) listeners[i + 1]).finished(e);
			}
		}

	}

	private void fireResultArrived(String providerName) {
		IContextResultEvent e = new ContextResultEvent(this, providerName);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IContextResultListener.class) {
				((IContextResultListener) listeners[i + 1]).resultArrived(e);
			}
		}

	}

	class SearchCommand extends Command {

		public SearchCommand(ICommandReference reference) {
			super(reference);
		}

		@Override
		public void execute(IWorkerStatusController worker) throws Exception {
			Iterator<IContextProvider> it = getAllProviders();
			while (it.hasNext()) {
				final IContextProvider p = it.next();
				p.search(frameMediator.getSemanticContext(), 0, 5);

				// notify all listeners that have a new search result
				// ensure this is called in the EDT
				Runnable run = new Runnable() {
					public void run() {
						fireResultArrived(p.getTechnicalName());
					}
				};
				SwingUtilities.invokeLater(run);

			}

			// notify all listeners that search is finished
			fireFinished();
		}

		@Override
		public void updateGUI() throws Exception {
		}

	}

}
