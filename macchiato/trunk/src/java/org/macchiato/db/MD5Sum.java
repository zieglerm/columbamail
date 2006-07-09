// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.macchiato.db;


/**
 * Light-weight class storing a MD5 sum and a timestamp.
 * <p>
 * Timestamp is used for token database cleanups.
 * 
 * @author fdietz
 *
 */
public class MD5Sum {
    private byte[] md5sum;
    private long timestamp;
    
    public MD5Sum(byte[] md5sum) {
        this.md5sum = md5sum;
    }
    
    public MD5Sum(byte[] md5sum, long timestamp) {
        this.md5sum = md5sum;
        this.timestamp = timestamp;
    }
   
    /**
     * @return Returns the md5sum.
     */
    public byte[] getMd5sum() {
        return md5sum;
    }
    /**
     * @param md5sum The md5sum to set.
     */
    public void setMd5sum(byte[] md5sum) {
        this.md5sum = md5sum;
    }
    /**
     * @return Returns the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
