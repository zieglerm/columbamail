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
package org.columba.addressbook.folder;

import java.util.Map;

import org.columba.addressbook.model.ContactModel;
import org.columba.addressbook.model.EmailModel;
import org.columba.addressbook.model.IContactModelPartial;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fdietz
 * 
 */
public class GetHeaderItemListTest extends AbstractFolderTstCase {


	/*
	 * Class under test for HeaderItemList getHeaderItemList()
	 */
    @Test
	public void testGetHeaderItemList() throws Exception {
		ContactModel c = new ContactModel();
		c.addEmail(new EmailModel("test@test.de", EmailModel.TYPE_HOME));
		c.setSortString("sortstring");

		String uid = getSourceFolder().add(c);

		Map<String, IContactModelPartial> list = getSourceFolder().getContactItemMap();

		IContactModelPartial item = list.get(uid);

		Assert.assertEquals("same displayname", c.getSortString(), item
				.getName());
	}

}