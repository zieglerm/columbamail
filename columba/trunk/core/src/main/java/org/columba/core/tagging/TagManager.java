package org.columba.core.tagging;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.columba.core.config.Config;
import org.columba.core.config.DefaultXmlConfig;
import org.columba.core.io.DiskIO;
import org.columba.core.tagging.api.ITag;
import org.columba.core.tagging.api.ITagManager;
import org.columba.core.xml.XmlElement;

public class TagManager implements ITagManager {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.core.tagging.TagManager");

	public static Vector<ITag> tags;

	public static final String TAG_PREFIX = "tag_";

	private static final String CORE_STR = "core";

	private File path; // tag configuration file path

	private File tagsConfigurationFile; // tag configuration file

	static private TagManager instance;

	// protected singleton constructor

	protected TagManager() {
	}

	// private file methods

	private boolean addToFile(ITag tag) {
		path = Config.getInstance().getConfigDirectory();
		DiskIO.ensureDirectory(path);

		tagsConfigurationFile = new File(path, "tags.xml");

		// just ensure thst the file exists
		if (!tagsConfigurationFile.exists())
			try {
				tagsConfigurationFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		Config.getInstance().registerPlugin(CORE_STR,
				tagsConfigurationFile.getName(),
				new DefaultXmlConfig(tagsConfigurationFile));

		DefaultXmlConfig xml = Config.getInstance().getPlugin(CORE_STR,
				"tags.xml");

		if (xml.getRoot().getElement("tags") == null)
			xml.getRoot().addElement(new XmlElement("tags"));

		List l = xml.getRoot().getElement("tags").getElements();
		for (int i = 0; i < l.size(); i++) {
			XmlElement e = (XmlElement) l.get(i);
			if (e.getAttribute("id").equals(tag.getId())
					&& (e.getAttribute("name").equals(tag.getName()))) {
				LOG.severe("Duplicate Name!");
				return false;
			}
		}

		XmlElement newXmlTag = new XmlElement("tag");
		newXmlTag.addAttribute("id", tag.getId());
		newXmlTag.addAttribute("name", tag.getName());
		xml.getRoot().getElement("tags").addElement(newXmlTag);

		try {
			xml.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	private void removeFromConfig(ITag tag) {
		path = Config.getInstance().getConfigDirectory();
		DiskIO.ensureDirectory(path);

		tagsConfigurationFile = new File(path, "tags.xml");

		// just ensure thst the file exists
		if (!tagsConfigurationFile.exists()) {
			try {
				tagsConfigurationFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// nothing to remove
			return;
		}

		Config.getInstance().registerPlugin(CORE_STR,
				tagsConfigurationFile.getName(),
				new DefaultXmlConfig(tagsConfigurationFile));

		DefaultXmlConfig xml = Config.getInstance().getPlugin(CORE_STR,
				"tags.xml");

		if (xml.getRoot().getElement("tags") == null)
			// nothing to remove
			return;

		List l = xml.getRoot().getElement("tags").getElements();
		for (int i = 0; i < l.size(); i++) {
			XmlElement e = (XmlElement) l.get(i);
			if (e.getAttribute("id").equals(tag.getId())
					&& (e.getAttribute("name").equals(tag.getName()))) {
				LOG.info("Removed Tag " + tag.getName() + "!");
				xml.getRoot().getElement("tags").removeElement(e);
			}
		}

		try {
			xml.save();
		} catch (Exception e) {
			// could not save, huh
			e.printStackTrace();
		}
	}

	private void loadTags() {
		this.tags = new Vector<ITag>();
		path = Config.getInstance().getConfigDirectory();
		DiskIO.ensureDirectory(path);

		tagsConfigurationFile = new File(path, "tags.xml");

		// just ensure thst the file exists
		if (!tagsConfigurationFile.exists()) {
			try {
				tagsConfigurationFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// nothing to load
			return;
		}

		Config.getInstance().registerPlugin(CORE_STR,
				tagsConfigurationFile.getName(),
				new DefaultXmlConfig(tagsConfigurationFile));

		DefaultXmlConfig xml = Config.getInstance().getPlugin(CORE_STR,
				"tags.xml");

		if (xml.getRoot().getElement("tags") == null)
			return;

		List l = xml.getRoot().getElement("tags").getElements();
		ITag addTag;
		for (int i = 0; i < l.size(); i++) {
			XmlElement e = (XmlElement) l.get(i);
			addTag = new Tag(e.getAttribute("id"));
			addTag.setName(e.getAttribute("name"));
			this.tags.add(addTag);
		}

	}

	// public methods

	public ITag addTag(String name) {
		// Do not allow an empty name
		if ((name == null) || ("".equals(name)) || (name.trim().length() == 0)) {
			LOG.severe("Name not set correctly, cannot add tag!");
			return null;
		}

		// Create a new tag with the Id Tag Prefix
		ITag tag = new Tag(TAG_PREFIX + name);
		tag.setName(name);

		// if the vector is loaded, add the tag also to the vector
		// if not, storing in the file is enough
		if (tags != null)
			if (getTagById(tag.getId()) == null)
				tags.add(tag);
			else {
				LOG.severe("Duplicate tag Name, cannot add tag " + name + "!");
				return null;
			}

		// storing in the file
		if (addToFile(tag))
			return tag;
		else
			return null;
	}

	public Iterator<ITag> getAllTags() {
		if (tags == null)
			loadTags();
		return tags.iterator();
	}

	public ITag getTag(String name) {
		if (tags == null)
			loadTags();
		for (ITag tag : tags) {
			if (tag.getName().equals(name))
				return tag;
		}
		return null;
	}

	// TODO: needed?
	public ITag getTagById(String id) {
		if (tags == null)
			loadTags();
		for (ITag tag : tags) {
			if (tag.getId().equals(id))
				return tag;
		}
		return null;
	}

	// TODO: needed?
	public void removeTagById(String id) {
		if (tags == null)
			loadTags();
		for (ITag tag : tags) {
			if (tag.getId().equals(id)) {
				tags.remove(tag);
				removeFromConfig(tag);
				break;
			}
		}
	}

	public void removeTag(String name) {
		if (tags == null)
			loadTags();
		for (ITag tag : tags) {
			if (tag.getName().equals(name)) {
				tags.remove(tag);
				removeFromConfig(tag);
				break;
			}
		}
	}

	public static TagManager getInstance() {
		if (instance == null) {
			synchronized (TagManager.class) {
				if (instance == null)
					instance = new TagManager();
			}
		}
		return instance;
	}

}
