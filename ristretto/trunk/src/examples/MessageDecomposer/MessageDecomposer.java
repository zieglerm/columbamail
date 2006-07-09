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
package MessageDecomposer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;

import org.columba.ristretto.coder.Base64DecoderInputStream;
import org.columba.ristretto.coder.CharsetDecoderInputStream;
import org.columba.ristretto.coder.QuotedPrintableDecoderInputStream;
import org.columba.ristretto.io.FileSource;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.io.StreamUtils;
import org.columba.ristretto.message.BasicHeader;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.Message;
import org.columba.ristretto.message.MimeHeader;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.MimeType;
import org.columba.ristretto.parser.MessageParser;
import org.columba.ristretto.parser.ParserException;

public class MessageDecomposer {

	private static final String helpMessage =
        "Usage : MessageDecomposer mail-file <outdir> (optional)\n\n"
            + "Example: POPStats mail.txt result_dir\n\n";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if( args.length < 1) {
			System.out.println(helpMessage);
			return;
		}
		
		File messageFile = new File(args[0]);
		File outDir;
		
		if( args.length > 1) {
			outDir = new File(args[1]);			
		} else {
			outDir = new File(".");
		}
		
		
		if( !messageFile.exists() ) {
			System.out.println("File not found " + messageFile.toString());
			return;
		}
		
		if( !outDir.exists() || !outDir.isDirectory()) {
			if( !outDir.mkdir() ) {
				System.out.println("Could not create directory" + outDir.toString());
				return;				
			}
		}
		
		try {
			new MessageDecomposer().decomposeMessage(new FileSource(messageFile), outDir);
		} catch (ParserException e) {
			System.out.println("Parser Exception: " + e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getLocalizedMessage());
		}
		
		System.exit(0);
	}

	public void decomposeMessage(Source source, File outDir) throws ParserException, IOException {
		Message message = MessageParser.parse(source);
		
		writeStream(message.getHeader().getInputStream(), new File(outDir, "header.txt"));
		printHeader(message.getHeader());
		
		MimeTree mimeTree = message.getMimePartTree();
		List mimeParts = mimeTree.getAllLeafs();
		
		Iterator it = mimeParts.iterator();			

		int counter = 1;
		while(it.hasNext()) {
			LocalMimePart part = (LocalMimePart)it.next();
			MimeHeader h = part.getHeader();
			String filename = h.getFileName();
			if( filename == null) {
				MimeType type = h.getMimeType();
				if( type.getSubtype().equals("html")) {
					filename = "part"+ counter + ".html"; 
				} else if (type.getSubtype().equals("plain")) {
					filename = "part"+ counter + ".txt";
				} else {
					filename = "part"+counter;
				}
			}
			
			InputStream stream = part.getInputStream();
			if( h.getContentTransferEncoding() == MimeHeader.BASE64) {
				stream = new Base64DecoderInputStream(stream);
			} else if( h.getContentTransferEncoding() == MimeHeader.QUOTED_PRINTABLE) {
				stream = new QuotedPrintableDecoderInputStream(stream);
			}
			
			if( h.getContentParameter("charset") != null) {
				stream = new CharsetDecoderInputStream(stream, Charset.forName(h.getContentParameter("charset")));
			}

			// Now the stream contains the data ready to be processed.
			// In our case we write it to a file:
			writeStream(stream, new File(outDir, filename));
			
			// .. but you could convert it to a String if you know
			// it is text or to a byte array or the like
			
			counter++;
		}
		
		source.close();
	}
	
	private void writeStream(InputStream in, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		StreamUtils.streamCopy(in, out);
		in.close();		
		out.close();
	}
	
	private void printHeader(Header rawHeader) {
		BasicHeader header = new BasicHeader(rawHeader);
		if( header.getFrom() != null ) 
	    System.out.println("From    : " + header.getFrom().toString());
		System.out.println("Date    : " + DateFormat.getInstance().format(header.getDate()));
		System.out.println("Subject : " + header.getSubject());		
	}
	

}
