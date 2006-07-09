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
package org.macchiato.db.berkleydb;

import java.io.File;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author fdietz
 *  
 */
public class MyDBEnv {
	
	// use 20% of the VM memory size for the cache
	private final static int CACHE_SIZE=20;
	
	private Environment myEnv;

	// The databases that our application uses
	private Database tokenDb;

	private Database classCatalogDb;

	// Needed for object serialization
	private StoredClassCatalog classCatalog;

	// Our constructor does nothing
	public MyDBEnv() {
	}

	// The setup() method opens all our databases and the environment
	// for us.
	public void setup(File envHome, boolean readOnly) throws DatabaseException {

		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
		DatabaseConfig myDbConfig = new DatabaseConfig();

		// If the environment is read-only, then
		// make the databases read-only too.
		myEnvConfig.setReadOnly(readOnly);
		myDbConfig.setReadOnly(readOnly);

		// If the environment is opened for write, then we want to be
		// able to create the environment and databases if
		// they do not exist.
		myEnvConfig.setAllowCreate(!readOnly);
		myDbConfig.setAllowCreate(!readOnly);

		// Allow transactions if we are writing to the database
		myEnvConfig.setTransactional(!readOnly);
		myDbConfig.setTransactional(!readOnly);

		// use 20% of the VM memory 
		myEnvConfig.setCachePercent(CACHE_SIZE);
		
		// Open the environment
		myEnv = new Environment(envHome, myEnvConfig);

		// Now open, or create and open, our databases
		// Open the token databases
		tokenDb = myEnv.openDatabase(null, "TokenDB", myDbConfig);

		// Open the class catalog db. This is used to
		// optimize class serialization.
		classCatalogDb = myEnv.openDatabase(null, "ClassCatalogDB", myDbConfig);

		// Create our class catalog
		classCatalog = new StoredClassCatalog(classCatalogDb);

	}

	// getter methods

	// Needed for things like beginning transactions
	public Environment getEnv() {
		return myEnv;
	}

	public Database getTokenDB() {
		return tokenDb;
	}

	public StoredClassCatalog getClassCatalog() {
		return classCatalog;
	}

	//Close the environment
	public void close() {
		if (myEnv != null) {
			try {

				tokenDb.close();

				classCatalogDb.close();

				// Finally, close the environment.
				myEnv.close();
			} catch (DatabaseException dbe) {
				System.err.println("Error closing MyDbEnv: " + dbe.toString());
				System.exit(-1);
			}
		}
	}
}