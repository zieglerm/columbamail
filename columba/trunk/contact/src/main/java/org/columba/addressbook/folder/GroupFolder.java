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

import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.columba.addressbook.config.FolderItem;
import org.columba.addressbook.gui.tree.AddressbookTreeModel;
import org.columba.addressbook.model.GroupModel;
import org.columba.addressbook.model.IContactModel;
import org.columba.addressbook.model.IContactModelPartial;
import org.columba.addressbook.model.IGroupModel;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.exception.StoreException;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.xml.XmlElement;

/**
 * Group folder storing a list of contact indices.
 * <p>
 * This can be seen as a filtered view of a contact folder. 
 * 
 * @author fdietz
 *  
 */
public class GroupFolder extends AbstractFolder implements IContactStorage, IGroupFolder {

	private IGroupModel group;

	Map<String,IContactModelPartial> map = new Hashtable<String,IContactModelPartial>();

	private ImageIcon groupImageIcon = ImageLoader
	.getSmallIcon(IconKeys.USER);

	/**
	 * @param name
	 */
	public GroupFolder(String name, String dir) {
		super(name, dir);

		group = new GroupModel();
	}

	/**
	 * @param item
	 */
	public GroupFolder(FolderItem item) {
		super(item);

		XmlElement property = item.getElement("property");
		XmlElement e = property.getElement("group");
		if (e == null) {
			e = new XmlElement("group");
			property.addElement(e);
		}

		group = new GroupModel(e, property, getId());
	}

	public void createChildren(IWorkerStatusController worker) {
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#add(IContactModel)
	 */
	public String add(IContactModel contact) throws StoreException {
		String uid = contact.getId();

		group.addMember(uid);

		updateContactItemMap();

		fireItemAdded(uid);

		return uid;
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#count()
	 */
	public int count() throws StoreException{

		return group.count();
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#exists(java.lang.Object)
	 */
	public boolean exists(String uid) throws StoreException{

		return group.exists(uid);
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#get(java.lang.Object)
	 */
	public IContactModel get(String uid) throws StoreException {

		AbstractFolder parent = (AbstractFolder) getParent();

		return parent.get(uid);
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#modify(java.lang.Object,
	 *      IContactModel)
	 */
	public void modify(String uid, IContactModel contact) throws StoreException {
		AbstractFolder parent = (AbstractFolder) getParent();

		parent.modify(uid, contact);

		updateContactItemMap();

		fireItemChanged(uid);

	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#remove(java.lang.Object)
	 */
	public void remove(String uid) throws StoreException {
		group.remove(uid);

		updateContactItemMap();

		fireItemRemoved(uid);
	}

	private void updateContactItemMap() {
		AbstractFolder parent = (AbstractFolder) getParent();

		Map<String,IContactModelPartial> parentmap = parent.getContactItemMap();

		map.clear();

		String[] members = group.getMembers();
		for (int i = 0; i < members.length; i++) {
			IContactModelPartial p = parentmap.get(members[i]);
			if (p == null) {
				// contact doesn't exist in parent folder anymore
				// -> remove it

				remove(members[i]);
			} else {
				map.put(members[i], p);
			}
		}
	}

	/**
	 * @see org.columba.addressbook.folder.IContactStorage#getHeaderItemList()
	 */
	public Map<String,IContactModelPartial> getContactItemMap() throws StoreException {
		updateContactItemMap();

		return map;
	}

	/**
	 * @return Returns the group.
	 */
	public IGroupModel getGroup() {
		return group;
	}

	/**
	 * @see org.columba.addressbook.folder.AddressbookTreeNode#getIcon()
	 */
	public ImageIcon getIcon() {
		return groupImageIcon;
	}

	public void modelChanged() {
		AddressbookTreeModel.getInstance().nodeChanged(this);

		updateContactItemMap();

		fireItemAdded(null);
	}
}