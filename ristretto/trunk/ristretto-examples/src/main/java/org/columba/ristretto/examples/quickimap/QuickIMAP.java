/*
 *  BEGIN LICENSE BLOCK Version: MPL 1.1/GPL 2.0/LGPL 2.1
 * 
 * The contents of this file are subject to the Mozilla Public License Version 1.1 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 * 
 * The Original Code is Ristretto Mail API.
 * 
 * The Initial Developers of the Original Code are Timo Stich and Frederik Dietz. Portions created by the Initial
 * Developers are Copyright (C) 2004 All Rights Reserved.
 * 
 * Contributor(s):
 * 
 * Alternatively, the contents of this file may be used under the terms of either the GNU General Public License Version
 * 2 or later (the "GPL"), or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"), in which case the
 * provisions of the GPL or the LGPL are applicable instead of those above. If you wish to allow use of your version of
 * this file only under the terms of either the GPL or the LGPL, and not to allow others to use your version of this
 * file under the terms of the MPL, indicate your decision by deleting the provisions above and replace them with the
 * notice and other provisions required by the GPL or the LGPL. If you do not delete the provisions above, a recipient
 * may use your version of this file under the terms of any one of the MPL, the GPL or the LGPL.
 * 
 * END LICENSE BLOCK
 */
package org.columba.ristretto.examples.quickimap;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;

import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.CharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.imap.IMAPException;
import org.columba.ristretto.imap.IMAPHeader;
import org.columba.ristretto.imap.IMAPProtocol;
import org.columba.ristretto.imap.SearchKey;
import org.columba.ristretto.imap.SequenceSet;
import org.columba.ristretto.io.StreamUtils;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.MailboxInfo;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;

/**
 * QuickIMAP class - a simple command line IMAP client demonstration
 * 
 * @author timo
 */
public class QuickIMAP {

  private static final String[] HEADER      = new String[] { "From", "Date", "Subject" };

  private static final String   helpMessage = "Usage : QuickIMAP imap-server username password\n\n"
                                                + "Example: QuickIMAP imap.mail.com myname mypassword\n\n";

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

    // RistrettoLogger.setLogStream(System.out);

    IMAPProtocol protocol = new IMAPProtocol(server, IMAPProtocol.DEFAULT_PORT);
    try {
      System.out.println("Connecting to " + server + " ...");
      protocol.openPort();

      System.out.println("Login in as " + username + " ...");
      protocol.login(username, password.toCharArray());

      System.out.print("Checking Inbox: ");
      MailboxInfo info = protocol.select("INBOX");
      if (info.getFirstUnseen() > 0) {
        Integer[] unseenMails = protocol.search(new SearchKey[] { new SearchKey(SearchKey.UNSEEN) });

        System.out.println(unseenMails.length + " unseen message(s).");

        IMAPHeader[] header = protocol.fetchHeaderFields(new SequenceSet(unseenMails), HEADER);

        for (int i = header.length - 1; i >= 0; i--) {
          System.out.println("===");
          printHeader(header[i]);

          MimeTree mimeTree = protocol.fetchBodystructure(unseenMails[i].intValue());
          MimePart textPart = mimeTree.getFirstTextPart("plain");

          if (textPart != null) {
            InputStream body = protocol.fetchBody(unseenMails[i].intValue(), textPart.getAddress());
            MimeHeader textHeader = textPart.getHeader();

            if (textHeader.getContentTransferEncoding() == MimeHeader.QUOTED_PRINTABLE) {
              body = new QuotedPrintableDecoderInputStream(body);
            } else if (textHeader.getContentTransferEncoding() == MimeHeader.BASE64) {
              body = new Base64DecoderInputStream(body);
            }

            String charsetName = textHeader.getContentParameter("charset");
            if (charsetName == null) {
              charsetName = System.getProperty("file.encoding");
            }

            body = new CharsetDecoderInputStream(body, Charset.forName(charsetName));

            System.out.println("---");
            System.out.println(StreamUtils.readInString(body));
            System.out.println("---");
          }

          if (mimeTree.count() > 1 || textPart == null) {
            System.out.println("message has attachments");
            System.out.println("---");
          }
        }
      } else {
        System.out.println(" no unseen messages found.");
      }

      protocol.logout();

    } catch (IOException e) {
      System.err.println("IO Error: " + e.getLocalizedMessage());

    } catch (IMAPException e) {
      System.err.println("IMAP Error: " + e.getMessage());

      try {
        protocol.logout();
      } catch (IOException e1) {
      } catch (IMAPException e1) {
      }
    }

    System.exit(0);
  }

  /**
   * printHeader method
   */
  private static void printHeader(IMAPHeader rawHeader) {
    BasicHeader header = new BasicHeader(rawHeader.getHeader());

    System.out.println(header.getFrom().toString());
    System.out.println(DateFormat.getInstance().format(header.getDate()));
    System.out.println(header.getSubject());
  }
}
