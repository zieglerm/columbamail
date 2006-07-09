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
package org.columba.ristretto.message.io;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.columba.ristretto.io.FileSource;
import org.columba.ristretto.io.Source;

import junit.framework.TestCase;

public class FileSourceTest extends TestCase {

    private static final String TEST_FILENAME_STR = "src/test/org/columba/ristretto/message/io/FileSourceTest.eml";

    /**
     * Constructor for FileSourceTest.
     *
     * @param arg0 name of test
     */
    public FileSourceTest(String arg0) {
        super(arg0);
    }

    public void testLength() throws IOException {
    	File file = new File(TEST_FILENAME_STR);
        FileSource source = new FileSource(file);
        assertEquals( file.length(), source.length() );
    }
    
    public void testFromActualPosition1() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        source.seek(10);
        Source subsource = source.fromActualPosition();
        assertTrue(subsource.next() == 't');
        assertTrue(subsource.next() == 'e');
        assertTrue(subsource.next() == 's');
        assertTrue(subsource.next() == 't');
    }

    public void testFromActualPosition2() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        source.seek(50);
        Source subsource = source.fromActualPosition();
        assertEquals("is a test", subsource.toString());
    }

    public void testFromActualPosition3() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        source.seek(50);
        Source subsource = source.fromActualPosition();
        subsource.seek(5);
        Source subsubsource = subsource.fromActualPosition();
        assertTrue(subsubsource.toString().equals("test"));
    }

    public void testNext() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        assertTrue(source.next() == 'T');
        assertTrue(source.next() == 'h');
        assertTrue(source.next() == 'i');
        assertTrue(source.next() == 's');
    }

    public void testRegexp() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        Pattern pattern = Pattern.compile("test");
        Matcher matcher = pattern.matcher(source);
        assertTrue(matcher.find());
    }

    public void testSeek() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        source.seek(10);
        assertTrue(source.next() == 't');
        assertTrue(source.next() == 'e');
        assertTrue(source.next() == 's');
        assertTrue(source.next() == 't');
    }

    public void testSubSequence1() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        Source subsource = source.subSource(10, 13);
        assertTrue(subsource.next() == 't');
        assertTrue(subsource.next() == 'e');
        assertTrue(subsource.next() == 's');
        assertTrue(subsource.next() == 't');
    }

    public void testSubSequence2() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        Source subsource = source.subSource(5, 9);
        assertTrue(subsource.toString().equals("is a"));
    }

    public void testSubSequence3() throws IOException {
        FileSource source = new FileSource(new File(TEST_FILENAME_STR));
        Source subsource = source.subSource(5, 9);
        Source subsubsource = subsource.subSource(0, 2);
        assertTrue(subsubsource.toString().equals("is"));
    }

}
