//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.macchiato.io;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.macchiato.db.DBToken;
import org.macchiato.db.DBTokenImpl;
import org.macchiato.db.FrequencyDB;
import org.macchiato.db.MD5Sum;
import org.macchiato.serialize.ObjectReader;
import org.macchiato.serialize.ObjectWriter;

/**
 *
 *
 * @author fdietz
 */
public class FrequencyIO {
    
    /**
     * Save frequency db to file.
     *
     * @param map			frequency db
     * @param file			file
     * @throws Exception
     */
    public static void save(FrequencyDB map, File file) throws IOException {
        ObjectWriter writer = null;
        try {
            writer = new ObjectWriter(file);

            // save bad/good message count
            writer.writeObject(new Integer(map.getBadMessageCount()));
            writer.writeObject(new Integer(map.getGoodMessageCount()));

            // save total number of tokens
            writer.writeObject(new Integer(map.tokenCount()));

            Iterator it = map.tokenKeyIterator();
            while (it.hasNext()) {
                String key= (String) it.next();
                DBToken token= map.getToken(key);
                            /*
                            MacchiatoLogger.log.info(
                                    " write token[" + key + "]=" + token.toString());
                             */
                writer.writeObject(key);

                writer.writeObject(new Integer(token.getBadCount()));
                writer.writeObject(new Integer(token.getGoodCount()));
                writer.writeObject(new Float(token.getScore()));
                writer.writeObject(new Long(token.getTimeStamp()));
            }

            // save total number of message md5 sums
            writer.writeObject(new Integer(map.md5SumCount()));

            it = map.md5SumIterator();
            while (it.hasNext()) {
                byte[] data = (byte[]) it.next();
                MD5Sum sum = map.getMessageMD5Sum(data);

                writer.writeObject(data);
                writer.writeObject(new Long(sum.getTimestamp()));
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    
    /**
     * Load frequency db from file.
     *
     * @param map			frequency map
     * @param file			file
     * @throws Exception
     */
    public static void load(FrequencyDB map, File file) throws IOException {
        ObjectReader reader = null;
        try {
            reader = new ObjectReader(file);

            // read bad/good message count
            map.setBadMessageCount(((Integer) reader.readObject()).intValue());
            map.setGoodMessageCount(((Integer) reader.readObject()).intValue());

            // read total number of tokens
            int count = ((Integer) reader.readObject()).intValue();

            String o = null;
            for (int i = 0; i < count; i++) {

                o = (String) reader.readObject();
                DBToken token = new DBTokenImpl(o);

                Integer bad = (Integer) reader.readObject();
                token.setBadCount(bad.intValue());
                Integer good = (Integer) reader.readObject();
                token.setGoodCount(good.intValue());
                Float score = (Float) reader.readObject();
                token.setScore(score.floatValue());
                Long timeStamp = (Long) reader.readObject();
                token.setTimeStamp(timeStamp.longValue());

                            /*
                            MacchiatoLogger.log.info(
                                    "read token[" + o + "]=" + token);
                             */

                map.addToken(token);
            }

            // read total number of message md5 sums
            count = ((Integer) reader.readObject()).intValue();

            Object value = null;
            for (int i = 0; i < count; i++) {
                value = reader.readObject();

                MD5Sum sum = new MD5Sum((byte[]) value);
                value = reader.readObject();
                sum.setTimestamp(((Long) value).longValue());

                map.addMessageMD5Sum(sum);
            }
        } catch (ClassNotFoundException cnfe) {
            //this is a desaster and should not occur at all
            throw new RuntimeException(cnfe);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
