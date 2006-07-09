/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Ristretto Mail API.
 *
 * The Initial Developers of the Original Code are
 * Timo Stich and Frederik Dietz.
 * Portions created by the Initial Developers are Copyright (C) 2004
 * All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */
package IMAPsync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.columba.ristretto.imap.IMAPException;
import org.columba.ristretto.imap.IMAPHeader;
import org.columba.ristretto.imap.IMAPProtocol;
import org.columba.ristretto.imap.ListInfo;
import org.columba.ristretto.imap.SequenceSet;
import org.columba.ristretto.log.RistrettoLogger;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.MailboxInfo;

public class IMAPsync {

	private static final Pattern SERVER_REGEXP = Pattern.compile("^([^@]+)@([^:]+):(.*)$"); 
	
	private static final String[] HEADERS = new String[] {"Date","Subject","From","Message-ID", "Message-Id"};
	
	/**
	 * 
	 * 
	 * @param args
	 * @throws IMAPException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, IMAPException {
		boolean dryrun = false;
		boolean listfolders = false;
		
		if( args.length < 2 ) {
			printUsage();
			System.exit(0);			
		}
		
		int startArg = 0;
		
		if( args[0].startsWith("-")) {
			startArg++;
			
			dryrun = args[0].contains("d");
			listfolders = args[0].contains("l");
			if( listfolders) System.out.println("Listing mailboxes only!");
			
			if( args[0].contains("v")) RistrettoLogger.setLogStream(System.out);
		}
		
		Matcher srcMatcher = SERVER_REGEXP.matcher(args[startArg]);
		Matcher destMatcher = SERVER_REGEXP.matcher(args[startArg+1]);		
		
		if( !srcMatcher.matches() || !destMatcher.matches()) {
			printUsage();
			System.exit(0);
		}
		
		// Connecting to source
		
		System.out.println("Connecting to " + srcMatcher.group(2));
		IMAPProtocol source = new IMAPProtocol(srcMatcher.group(2), IMAPProtocol.DEFAULT_PORT);
		source.openPort();
		System.out.println("Authenticating " + srcMatcher.group(1));
		String passwd;
		passwd = PasswordField.readPassword("Enter password: ");
		source.login(srcMatcher.group(1), passwd.toCharArray());
		
		if( listfolders ) {
			ListInfo[] mailboxes = source.lsub(srcMatcher.group(3),"*");
			for( ListInfo l : mailboxes) {
				System.out.println(l.getName());
			}
		} else {
			System.out.println("Selecting " + srcMatcher.group(3));
			MailboxInfo srcInfo = source.select(srcMatcher.group(3));

			System.out.println(srcInfo.getExists() + " messages");
		}
		
		
		// Connecting to dest
		
		System.out.println("Connecting to " + destMatcher.group(2));
		IMAPProtocol dest = new IMAPProtocol(destMatcher.group(2), IMAPProtocol.DEFAULT_PORT);
		dest.openPort();
		System.out.println("Authenticating " + destMatcher.group(1));

		passwd = PasswordField.readPassword("Enter password: ");
		dest.login(destMatcher.group(1), passwd.toCharArray());
		
		if( listfolders ) {
			ListInfo[] mailboxes = dest.lsub(destMatcher.group(3),"*");
			for( ListInfo l : mailboxes) {
				System.out.println(l.getName());
			}
			
			source.logout();
			dest.logout();
			System.exit(0);
			
		} else {
			System.out.println("Selecting " + destMatcher.group(3));
			MailboxInfo destInfo = dest.select(destMatcher.group(3));
			
			System.out.println(destInfo.getExists() + " messages");
		}
		
		// Fetching message lists		
		System.out.println("Fetching messagelist from source...");
		IMAPHeader[] srcHeader = source.uidFetchHeaderFields(SequenceSet.getAll(), HEADERS);
		
		System.out.println("Fetching messagelist from dest...");
		IMAPHeader[] destHeader = dest.uidFetchHeaderFields(SequenceSet.getAll(), HEADERS);
		
		// Computing the diffs
		
		ArrayList<IMAPHeader> srcList = new ArrayList<IMAPHeader>(Arrays.asList(srcHeader)); 
		ArrayList<IMAPHeader> destList = new ArrayList<IMAPHeader>(Arrays.asList(destHeader)); 
		
		ListTools.substract(srcList, destList, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				// Compare to IMAPHeaders
				BasicHeader a = new BasicHeader(((IMAPHeader) arg0).getHeader());
				BasicHeader b = new BasicHeader(((IMAPHeader) arg1).getHeader());
				
				// Test message ids
				if( a.getMessageID() != null && b.getMessageID() != null && a.getMessageID().equals(b.getMessageID()) ){
					return 0;
				}
				
				// Test subject from and date
				boolean test = true;
				if( a.getSubject()!= null && b.getSubject()!=null  ) {
					test &= a.getSubject().equals(b.getSubject());
				}
				
				// Dates are always non-null
				test &= a.getDate().equals(b.getDate());

				if(a.getFrom() != null && b.getFrom() != null) {
					 test &= a.getFrom().equals(b.getFrom());					
				}

				if( test ) {
					return 0;
				}
				
				// if unequal compare dates
				return a.getDate().compareTo(b.getDate());
			}
		});
		
		// The srcList contains all messages that need to be copied
		List<IMAPHeader> copyList = srcList; 
		
		System.out.println("Copying "+ copyList.size() + " mails to dest...");
		int i = 1;
		for( IMAPHeader h: copyList ) {			
			System.out.println("Copying " + h.getUid());
			if( !dryrun ) dest.append(destMatcher.group(3),source.uidFetchMessage(h.getUid()));
		}
		source.logout();
		dest.logout();
	}

	private static void printUsage() {
		System.out.println("IMAPrsync [options] user@src.imap.server:mailbox user@dest.imap.server:mailbox");
		System.out.println("Options:");
		System.out.println("\t-d\t dry run");
		System.out.println("\t-l\t list mailboxes only");		
		System.out.println("\t-v\t verbose");
	}

	

	
	
}
