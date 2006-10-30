package org.columba.addressbook.folder;

import org.columba.addressbook.folder.virtual.VirtualFolder;
import org.columba.addressbook.model.ContactModel;
import org.columba.addressbook.model.EmailModel;
import org.columba.addressbook.model.IContactModel;

public class VirtualFolderContactTest extends AbstractFolderTstCase {

	public VirtualFolderContactTest(String arg0) {
		super(arg0);	
	}
	

	public void testAdd() throws Exception{
		ContactModel c = new ContactModel();

		c.setNickName("nickname");
		c.addEmail(new EmailModel("name@mail.com", EmailModel.TYPE_HOME));
		String parentId = getSourceFolder().add(c);

		VirtualFolder vf = new VirtualFolder();
		
		String id = vf.add(getSourceFolder(), parentId);
		
		IContactModel c2 = vf.get(id);

		assertEquals("nickname", c2.getNickName());
	}
	
	public void testModify() throws Exception{
		ContactModel c = new ContactModel();

		c.setNickName("nickname");
		c.addEmail(new EmailModel("name@mail.com", EmailModel.TYPE_HOME));
		String parentId = getSourceFolder().add(c);

		VirtualFolder vf = new VirtualFolder();
		
		String id = vf.add(getSourceFolder(), parentId);
		
		ContactModel c1 = (ContactModel) vf.get(id);
		c1.setNickName("new");
		vf.modify(id, c1);
		
		IContactModel c2 = vf.get(id);

		assertEquals("new", c2.getNickName());
	}
	
}
