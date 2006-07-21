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
package POPStats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.columba.ristretto.auth.AuthenticationException;
import org.columba.ristretto.auth.AuthenticationFactory;
import org.columba.ristretto.auth.NoSuchAuthenticationException;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.io.TempSourceFactory;
import org.columba.ristretto.log.RistrettoLogger;
import org.columba.ristretto.parser.ParserException;
import org.columba.ristretto.pop3.POP3Exception;
import org.columba.ristretto.pop3.POP3Protocol;
import org.columba.ristretto.pop3.ScanListEntry;

import MessageDecomposer.MessageDecomposer;

/**
 * POPStats class - A command line utility to read the status of a POP3 Server.
 * Can also be useful in debugging why a POP server might appear not to be
 * working properly.
 * @author timo
 */
public class POPStats {

	private static final String helpMessage = "Usage : POPStats pop3-server username password <outdir>\n\n"
			+ "Example: POPStats pop3.mail.com myname mypassword\n\n";

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println(helpMessage);
			return;
		}

		String server = args[0];
		String username = args[1];
		String password = args[2];

		File outDir;

		if (args.length > 3) {
			outDir = new File(args[3]);
		} else {
			outDir = new File(".");
		}

		if (!outDir.exists() || !outDir.isDirectory()) {
			if (!outDir.mkdir()) {
				System.out.println("Could not create directory"
						+ outDir.toString());
				return;
			}
		}

		boolean sslSupported = false;

		POP3Protocol protocol = new POP3Protocol(server,
				POP3Protocol.DEFAULT_PORT);
		// RistrettoLogger.setLogStream(System.out);

		try {
			// --- Connect and log in to the server

			System.out.println("Connecting to " + server + " ...");
			protocol.openPort();

			if (protocol.isApopSupported()) {
				System.out.println("APOP authentication supported");
			}
			String saslCapabilities = null;

			try {
				String[] capabilities = protocol.capa();
				for (int i = 0; i < capabilities.length
						&& saslCapabilities == null; i++) {
					if (capabilities[i].toLowerCase().startsWith("sasl")) {
						saslCapabilities = capabilities[i];
						System.out
								.println("SASL authentication method(s) supported: "
										+ saslCapabilities);
					}

					if (capabilities[i].toLowerCase().startsWith("stls")) {
						System.out.println("SSL supported");
						sslSupported = true;
					}

				}
			} catch (POP3Exception e1) {
				System.out.println("CAPA command not supported");
			}

			if (false) {// sslSupported ) {
				System.out.println("Establishing secure SSL connection...");
				protocol.startTLS();
			}

			boolean authenticated = false;

			try {
				if (protocol.isApopSupported()) {
					System.out.println("Authenticating with APOP...");
					protocol.apop(username, password.toCharArray());
				} else if (saslCapabilities != null) {
					try {
						String authMethod = AuthenticationFactory.getInstance()
								.getSecurestMethod(saslCapabilities);
						System.out.println("Authenticating with SASL "
								+ authMethod + "...");
						protocol.auth(authMethod, username, password
								.toCharArray());

					} catch (NoSuchAuthenticationException e) {
						System.err
								.println("No Authentication Method available: "
										+ e.getLocalizedMessage());
						protocol.quit();
						return;
					}
				}
			} catch (AuthenticationException e) {
				System.err.println("Authentication error: "
						+ e.getLocalizedMessage());
				System.err.println("Fallback to USER/PASS method");
			} catch (POP3Exception e1) {
				System.err.println("Authentication error: " + e1.getMessage());
				System.err.println("Fallback to USER/PASS method");
			}

			if (protocol.getState() != POP3Protocol.TRANSACTION) {
				protocol.userPass(username, password.toCharArray());
			}

			// ----- We are now logged in to the server

			// Get some mailbox statistics

			System.out.println("Getting mailbox statisics...");
			ScanListEntry[] list = protocol.list();
			System.out.println(list.length + " messages on server");
			int maxSize = -1;
			int sumSize = 0;
			for (int i = 0; i < list.length; i++) {
				sumSize += list[i].getSize();
				maxSize = Math.max(maxSize, list[i].getSize());
			}
			System.out.println("Total mailbox size: " + (sumSize / 1024)
					+ " kB");
			System.out.println("Mean message size: "
					+ (sumSize / (1024 * list.length)) + " kB");
			System.out.println("Largest message size: " + (maxSize / 1024)
					+ " kB");

			// Download the latest mail to a file
			int messageIndex = list.length - 1;
			System.out.println("Downloading message " + messageIndex + "...");
			InputStream messageStream = protocol.retr(list[messageIndex]
					.getIndex(), list[messageIndex].getIndex());

			// We like to monitor the download progress to the shell
			messageStream = new ShellProgressMonitorInputStream(messageStream);

			// Create a temporary source from the downloaded message
			// Note: This actually downloads the message and saves
			// it into a temporary file
			Source messageSource = TempSourceFactory
					.createTempSource(messageStream);
			messageStream.close();

			new MessageDecomposer().decomposeMessage(messageSource, outDir);

			// ----- We are done

			protocol.quit();

		} catch (IOException e) {
			System.err.println("IO Error: " + e.getLocalizedMessage());

		} catch (POP3Exception e) {
			System.err.println("POP3 Error: " + e.getMessage());
			try {
				protocol.quit();
			} catch (IOException e1) {
			} catch (POP3Exception e1) {
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

}
