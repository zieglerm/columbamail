/*
 * The contents of this file are subject to the Mozilla Public License 
 * Version 1.1 (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at 
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
 * for the specific language governing rights and
 * limitations under the License.
 *
 * The Original Code is "Java Security Component Framework"
 *
 * The Initial Developer of the Original Code are Thomas Wabner, alias waffel.
 * Portions created by Thomas Wabner are Copyright (C) 2004. 
 * 
 * All Rights Reserved.
 * Created on 21.01.2004
 *
 */
package org.waffel.jscf.gpg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDefinitions;
import org.waffel.jscf.JSCFException;
import org.waffel.jscf.JSCFResultSet;
import org.waffel.jscf.JSCFStatement;

/**
 * Don't use methods from this class. Use the JSCFramwork (
 * {@link org.waffel.jscf.JSCFStatement})
 * 
 * This class implements the JSCFStatement interface and provides some more
 * useful helper methods. It is the gpg command line implementation which calls
 * simple the installed gpg tool with some parameters.
 * 
 * @author waffel (Thomas Wabner)
 */
public final class GPGStatement implements JSCFStatement {

	/**
	 * The Decrypt action used for decrypting messages.
	 */
	public static final int DECRYPT_ACTION = 0;

	/**
	 * The Encrypt action used for encrypting messages.
	 */
	public static final int ENCRYPT_ACTION = 1;

	/**
	 * The sign action used for signing messages.
	 */
	public static final int SIGN_ACTION = 2;

	/**
	 * The verify action used to verify a message.
	 */
	public static final int VERIFY_ACTION = 3;

	/**
	 * The action to list keys for names.
	 */
	public static final int LIST_KEYS_ACTIONS = 4;

	/**
	 * <code>myConnection</code> to be used for the driver.
	 */
	private JSCFConnection myConnection;

	/**
	 * <code>lineSeparator</code> to separate lines system independently.
	 */
	private byte[] lineSeperator;

	/**
	 * Commands to send to the gpgp commandline tool. The order in this string
	 * array are used by the action variables. For example if you use the
	 * DECRYPT_ACTION which has the integer value 0, the first (0.) entry from
	 * this array is used to create the commandline statement for the gpg tool.
	 */
	private static final String[] COMMANDS = {
			"--batch --no-tty --passphrase-fd 0 -d",
			"--no-secmem-warning --no-greeting --batch --no-tty --armor --output - --encrypt --group recipientgroup=%recipients%  -r recipientgroup",
			"--no-secmem-warning --no-greeting --batch --digest-algo SHA1 --yes --no-tty --armor --textmode --passphrase-fd 0 --output - --detach-sign -u %user% ",
			"--no-secmem-warning --batch --no-tty --digest-algo %digest-algo% --verify %sigfile% -",
			"--no-secmem-warning --no-greeting --batch --no-tty --output - --list-keys" };

	/**
	 * Contructor which saves the given connection and initialises any internal
	 * variables.
	 * 
	 * @param connection
	 *            Connection which should be used for this statement.
	 */
	public GPGStatement(final JSCFConnection connection) {
		this.myConnection = connection;
		this.lineSeperator = System.getProperty("line.separator").getBytes(); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeSign(final InputStream message,
			final String userID, final String password) throws JSCFException {
		// create a newly empty map which holds the parameters userID and
		// password. These parameters are replaced in the commandline which is
		// send to
		// the gpg process to sign the given message stream.
		Map<String, String> paramMap = new Hashtable<String, String>();
		// add the userID as parameter
		paramMap.put("user", userID);
		// create a empty commandline parameter list as a array list. We cannot
		// send a *complete* string to the external gpg process. We should
		// seperate each string into extra commandline parameters.
		List<String> cmdList = new ArrayList<String>();
		// adding the path to the gpg tool to the commandlineList
		cmdList.add(((GPGConnection) this.myConnection).getPath());
		try {
			// creating the whole commandline parameter list
			cmdList.addAll(createCommandArray(paramMap, SIGN_ACTION));
		} catch (Exception e1) {
			throw new JSCFException("error on userid [" + e1.getMessage() + "]");
		}
		return intern(cmdList, password, message);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeSign(final InputStream message,
			final String password) throws JSCFException {
		String userId = ((GPGConnection) this.myConnection).getId();
		if (userId == null) {
			userId = this.myConnection.getProperties().getProperty(
					JSCFDefinitions.USERID);
		}
		return executeSign(message, userId, password);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeSign(final InputStream message)
			throws JSCFException {
		String password = ((GPGConnection) this.myConnection).getPassword();
		if (password == null) {
			password = this.myConnection.getProperties().getProperty(
					JSCFDefinitions.PASSWORD);
		}
		return executeSign(message, password);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeVerify(final InputStream message,
			final InputStream signature, final String digestAlgorithm)
			throws JSCFException {
		GPGResultSet resultSet = new GPGResultSet();
		Map<String, String> paramMap = new Hashtable<String, String>();
		FileOutputStream fout = null;
		File tempFile = null;

		paramMap.put("digest-algo", digestAlgorithm);
		List<String> cmdList = new ArrayList<String>();
		cmdList.add(((GPGConnection) this.myConnection).getPath());
		try {
			tempFile = File.createTempFile("gpgSig", null);
			// make sure file is deleted automatically when closing VM
			tempFile.deleteOnExit();
			fout = new FileOutputStream(tempFile);
			StreamUtils.streamCopy(signature, fout);
			fout.flush();
      fout.close();
			paramMap.put("sigfile", tempFile.getAbsolutePath());
		} catch (Exception e) {
			throw new JSCFException(
					"error on creating temp file from signature ["
							+ e.getMessage() + "]");
		}
		try {
			cmdList.addAll(createCommandArray(paramMap, VERIFY_ACTION));
		} catch (Exception e1) {
			throw new JSCFException("error on digestAlgorithm ["
					+ e1.getMessage() + "]");
		}
		try {
			String[] obj = new String[0];
			// starting process
			Process p = this.executeCommand(cmdList.toArray(obj));
			// write the pgpMessage out
			StreamUtils.streamCopy(message, p.getOutputStream());
			p.getOutputStream().close();

			resultSet.setReturnValue(p.waitFor());
			String error = StreamUtils.readInString(p.getErrorStream())
					.toString();
			resultSet
					.setErrorStream(new ByteArrayInputStream(error.getBytes()));
			resultSet
					.setResultStream(new ByteArrayInputStream(error.getBytes()));
			p.destroy();
			fout.close();
			tempFile.delete();
		} catch (Exception e2) {
			throw new ProgramNotFoundException("error running command ["
					+ e2.getMessage() + "]");
		}
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeVerify(final InputStream message,
			final InputStream signature) throws JSCFException {
		String digestAlgo = this.myConnection.getProperties().getProperty(
				GPGDefinitions.DIGESTALGORITHM);
		// if the algo not given per property we use the default sha1 algo
		if (digestAlgo == null) {
			digestAlgo = "sha1";
		}
		return executeVerify(message, signature, digestAlgo);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeEncrypt(final InputStream message,
			final String recipients) throws JSCFException {
		GPGResultSet resultSet = new GPGResultSet();
		Map<String, String> paramMap = new Hashtable<String, String>();
		paramMap.put("recipients", recipients);
		List<String> cmdList = new ArrayList<String>();
		cmdList.add(((GPGConnection) this.myConnection).getPath());
		try {
			cmdList.addAll(createCommandArray(paramMap, ENCRYPT_ACTION));
		} catch (Exception e1) {
			throw new JSCFException("error on recipients [" + e1.getMessage()
					+ "]");
		}
		try {
			String[] obj = new String[0];
			// System.out.println("start execute");
			// starting process
			Process p = this.executeCommand(cmdList.toArray(obj));
			// write the pgpMessage out

			StreamGlobber pStream = new StreamGlobber(message, p
					.getOutputStream());
			StreamGlobber errStream = new StreamGlobber(p.getErrorStream());
			StreamGlobber resStream = new StreamGlobber(p.getInputStream());

			pStream.start();
			errStream.start();
			resStream.start();

			pStream.join();
			p.getOutputStream().close();
			errStream.join();
			resStream.join();

			resultSet.setReturnValue(p.waitFor());
			resultSet.setErrorStream(errStream.getRetStream());
			resultSet.setResultStream(resStream.getRetStream());

			p.getInputStream().close();
			p.getErrorStream().close();

			p.destroy();
		} catch (Exception e2) {
			throw new ProgramNotFoundException("error running command ["
					+ e2.getMessage() + "]");
		}
		return resultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeEncrypt(final InputStream message)
			throws JSCFException {
		String recipients = this.myConnection.getProperties().getProperty(
				GPGDefinitions.RECIPIENTS);
		return executeEncrypt(message, recipients);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeDecrypt(final InputStream message,
			final String password) throws JSCFException {
		List<String> cmdList = new ArrayList<String>();
		cmdList.add(((GPGConnection) myConnection).getPath());
		try {
			cmdList.addAll(createCommandArray(new Hashtable(), DECRYPT_ACTION));
		} catch (Exception e1) {
			throw new JSCFException("error on decrypt [" + e1.getMessage()
					+ "]");
		}
		return intern(cmdList, password, message);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet executeDecrypt(final InputStream message)
			throws JSCFException {
		String password = ((GPGConnection) this.myConnection).getPassword();
		if (password == null) {
			password = this.myConnection.getProperties().getProperty(
					JSCFDefinitions.PASSWORD);
		}
		return executeDecrypt(message, password);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkPassphrase(final String password) throws JSCFException {
		String testStr = "testmessage";
		JSCFResultSet res = new GPGResultSet();
		res = this.executeSign(new ByteArrayInputStream(testStr.getBytes()),
				password);
		if (res.isError()) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkPassphrase() throws JSCFException {
		String password = ((GPGConnection) this.myConnection).getPassword();
		if (password == null) {
			password = this.myConnection.getProperties().getProperty(
					JSCFDefinitions.PASSWORD);
		}
		return checkPassphrase(password);
	}

	/**
	 * {@inheritDoc}
	 */
	public JSCFResultSet getKeys(Collection<String> theNames)
			throws JSCFException {
		GPGResultSet resultSet = new GPGResultSet();
		Map<String, String> paramMap = new Hashtable<String, String>();
		List<String> cmdList = new ArrayList<String>();

		cmdList.add(((GPGConnection) this.myConnection).getPath());

		try {
			cmdList.addAll(createCommandArray(paramMap,
					GPGStatement.LIST_KEYS_ACTIONS));
			// if there are names, we have to add each name as a seperate
			// command line argument to the command list (not as a string
			// seperated by space)
			if (theNames != null) {
				cmdList.addAll(theNames);
			}
		} catch (Exception e1) {
			throw new JSCFException("error on names [" + e1.getMessage() + "]");
		}
		try {
			String[] obj = new String[0];
			// starting process
			Process p = this.executeCommand(cmdList.toArray(obj));

			StreamGlobber errStream = new StreamGlobber(p.getErrorStream());
			StreamGlobber resStream = new StreamGlobber(p.getInputStream());

			errStream.start();
			resStream.start();

			errStream.join();
			resStream.join();

			resultSet.setReturnValue(p.waitFor());
			resultSet.setErrorStream(errStream.getRetStream());
			resultSet.setResultStream(resStream.getRetStream());

			p.getInputStream().close();
			p.getErrorStream().close();

			p.destroy();
		} catch (Exception e2) {
			throw new ProgramNotFoundException("error running command ["
					+ e2.getMessage() + "]");
		}
		return resultSet;
	}

	/**
	 * Executes the given command and the returns the connected process.
	 * 
	 * @param cmd
	 *            Command to be executed
	 * @return Process which is connected with the executed command
	 * @throws Exception
	 *             if the command cannot be executed.
	 * @see Runtime#exec(java.lang.String[])
	 */
	private Process executeCommand(final String[] cmd) throws Exception {
		Process p = Runtime.getRuntime().exec(cmd);
		return p;
	}

	/**
	 * This methods gets from the internal commands string array the commandline
	 * for the given action. The commandline is separated on white spaces to
	 * create for each commandline option a separated string. The given
	 * parameter map is used to insert the parameter from the map on the right
	 * position in the commandline. The position is identified by %% tokens. The
	 * string inside these tokens is the key in the given map and will be
	 * replaced with the accordingly entry from the map.
	 * 
	 * @param params
	 *            map with parameters that contain entries which are placed in
	 *            the according position in the commandline.
	 * @param action
	 *            Action to determine the right commandline string from the
	 *            internal commands string array
	 * @return A list of strings. Each string represent a commandline option.
	 * @throws Exception
	 *             If a StringTokenizer operation fails or the parsing of the
	 *             commandline string fails.
	 */
	private List<String> createCommandArray(final Map params, final int action)
			throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		StringTokenizer strToken = new StringTokenizer(COMMANDS[action]);
		String tok = null;
		while (strToken.hasMoreTokens()) {
			tok = strToken.nextToken();
			tok = parseCommandLine(params, tok);
			list.add(tok);
		}
		return list;
	}

	/**
	 * This method looks in the given string for %% characters. If these
	 * characters are found, the string inside these character (for example
	 * user) is used to get the entry for the key (for example user) from the
	 * given map. If an entry is found, the entry is returned without the %%
	 * characters.
	 * 
	 * @param params
	 *            Parameter map with entry which should be replacrd with the
	 *            string inside the %% characters.
	 * @param str
	 *            commandline option which eventually holds the %% characters.
	 *            Other strings are allowed, but then the string is only
	 *            returned without replace operations.
	 * @return The given string without modifying it, if there are no %%
	 *         characters found. Else the entry from the given parameter map
	 *         which is found per key which is included in the %% characters.
	 * @throws JSCFException
	 *             if the key inside the %%characters are not found in the given
	 *             parameter map.
	 */
	private String parseCommandLine(final Map params, final String str)
			throws JSCFException {
		String command = str;
		int startIdx = 0;
		int nextIdx = 0;
		String cmdParam = null;
		String param = null;
		StringBuffer ret = new StringBuffer(command);

		while ((startIdx = ret.indexOf("%", nextIdx)) != -1) {
			nextIdx = ret.indexOf("%", startIdx + 1);
			cmdParam = ret.substring(startIdx + 1, nextIdx);
			param = (String) params.get(cmdParam);
			if (param == null) {
				throw new JSCFException("missing parameter " + cmdParam);
			}
			ret.replace(startIdx, nextIdx + 1, param);
			nextIdx++;
		}
		return ret.toString();
	}

	/**
	 * This method starts the real gpg process, passes the commandline string to
	 * it and then copies the results from the process to a newly created
	 * JSCFResultSet. The resultSet is created initially with an empty error
	 * stream and an empty result stream. After running the process, the process
	 * is destroyed. This method is used if we do operations which need a
	 * password. The password is given separate to the process, in fact we must
	 * send the line separator to the process before we can send the password.
	 * 
	 * @param cmdList
	 *            The commandline options to pass to the gpg process.
	 * @param password
	 *            the password for gpg.
	 * @param message
	 *            The message on which the process should operate.
	 * @return a newly created JSCFResultSet which holds the results (error or
	 *         standard result message) from the executed process.
	 * @throws ProgramNotFoundException
	 *             If the process cannot be executed.
	 */
	private JSCFResultSet intern(final List<String> cmdList,
			final String password, final InputStream message)
			throws ProgramNotFoundException {
		GPGResultSet resultSet = new GPGResultSet();
		resultSet.setErrorStream(new ByteArrayInputStream(new String("")
				.getBytes()));
		resultSet.setResultStream(new ByteArrayInputStream(new String("")
				.getBytes()));
		try {
			String[] obj = new String[0];
			// starting process
			Process p = this.executeCommand(cmdList.toArray(obj));
			// give the process the password
			p.getOutputStream().write(password.getBytes());
			p.getOutputStream().write(this.lineSeperator);
			// System.out.println("command: " + cmdList);
			StreamGlobber pStream = new StreamGlobber(message, p
					.getOutputStream());
			StreamGlobber errStream = new StreamGlobber(p.getErrorStream());
			StreamGlobber resStream = new StreamGlobber(p.getInputStream());

			pStream.start();
			errStream.start();
			resStream.start();

			pStream.join();
			p.getOutputStream().close();
			errStream.join();
			resStream.join();

			resultSet.setReturnValue(p.waitFor());
			resultSet.setErrorStream(errStream.getRetStream());
			resultSet.setResultStream(resStream.getRetStream());

			p.getInputStream().close();
			p.getErrorStream().close();

			p.destroy();
		} catch (Exception e2) {
			throw new ProgramNotFoundException("error running command ["
					+ e2.getMessage() + "]");
		}
		return resultSet;
	}

}
