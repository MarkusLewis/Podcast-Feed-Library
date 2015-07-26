package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.utils.DateUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.QName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Episode {

	public static class Enclosure {

		private URL url;
		private Long length;
		private String mimeType;

		private Element enclosureElement;

		public Enclosure(Element enclosureElement) {
			this.enclosureElement = enclosureElement;
		}

		public URL getURL() throws MalformedFeedException, MalformedURLException {
			if(this.url != null)
				return this.url;

			Attribute urlAttribute = this.enclosureElement.attribute("url");
			if(urlAttribute == null)
				throw new MalformedFeedException("Missing required URL attribute for element Enclosure.");

			return this.url = new URL(urlAttribute.getValue());
		}

		public Long getLength() throws MalformedFeedException {
			if(this.length != null)
				return this.length;

			Attribute lengthAttribute = this.enclosureElement.attribute("length");
			if(lengthAttribute == null)
				throw new MalformedFeedException("Missing required Length attribute for element Enclosure.");

			try {
				return this.length = Long.parseLong(lengthAttribute.getValue());
			} catch(NumberFormatException e) {
				throw new MalformedFeedException("Invalid length specified for element Enclosure.");
			}
		}

		public String getType() throws MalformedFeedException {
			if(this.mimeType != null)
				return this.mimeType;

			Attribute typeAttribute = this.enclosureElement.attribute("type");
			if(typeAttribute == null)
				throw new MalformedFeedException("Missing required Type attribute for element Enclosure.");

			return this.mimeType = typeAttribute.getValue();
		}
	}
	
	private String title;
	private URL link;
	private String description;
	private String author;
	private Set<String> categories;
	private URL comments;
	private Enclosure enclosure;
	private String guid;
	private Date pubDate;
	private String sourceName;
	private URL sourceLink;
	private ITunesItemInfo iTunesItemInfo;
	private String contentEncoded;

	private Element itemElement;

	public Episode(Element itemElement) {
		this.itemElement = itemElement;
	}

	//Required Tags
	public String getTitle() throws MalformedFeedException {
		if(this.title != null)
			return this.title;

		Element titleElement = this.itemElement.element("title");
		if(titleElement == null)
			throw new MalformedFeedException("Item is missing required element title.");

		return this.title = titleElement.getText();
	}

	public String getDescription() throws MalformedFeedException {
		if(this.description != null)
			return this.description;

		Element descriptionElement = this.itemElement.element("description");
		if(descriptionElement == null)
			throw new MalformedFeedException("Item is missing required element description.");

		return this.description = descriptionElement.getText();
	}

	//Optional Tags
	public URL getLink() throws MalformedURLException {
		if(this.link != null)
			return this.link;

		Element linkElement = this.itemElement.element("link");
		if(linkElement == null)
			return null;

		if("atom".equalsIgnoreCase(linkElement.getNamespacePrefix()))
			return this.link = new URL(linkElement.attributeValue("href"));

		return this.link = new URL(linkElement.getText());
	}

	public Enclosure getEnclosure() {
		if(this.enclosure != null)
			return this.enclosure;

		Element enclosureElement = this.itemElement.element("enclosure");
		if(enclosureElement == null)
			return null;

		return this.enclosure = new Enclosure(enclosureElement);
	}

	public String getAuthor() {
		if(this.author != null)
			return this.author;

		Element authorElement = this.itemElement.element("author");
		if(authorElement == null)
			return null;

		return this.author = authorElement.getText();
	}

	public Set<String> getCategories() {
		if(this.categories != null)
			return this.categories;

		Element categoriesElement = this.itemElement.element("categories");
		if(categoriesElement == null || categoriesElement.elements().size() == 0)
			return null;

		Set<String> categories = new HashSet<>();
		for(Object elementObject : categoriesElement.elements("category")) {
			if(!(elementObject instanceof Element))
				continue;

			Element categoryElement = (Element) elementObject;
			categories.add(categoriesElement.getText());
		}

		return this.categories = Collections.unmodifiableSet(categories);
	}

	public URL getComments() throws MalformedURLException {
		if(this.comments != null)
			return this.comments;

		Element commentsElement = this.itemElement.element("comments");
		if(commentsElement == null)
			return null;

		return this.comments = new URL(commentsElement.getTextTrim());
	}

	public String getGUID() {
		if(this.guid != null)
			return this.guid;

		Element guidElement = this.itemElement.element("guid");
		if(guidElement == null)
			return null;

		return this.guid = guidElement.getTextTrim();
	}

	public Date getPubDate() throws DateFormatException {
		if(this.pubDate != null)
			return this.pubDate;

		Element pubDateElement = this.itemElement.element("pubDate");
		if(pubDateElement == null)
			return null;

		return this.pubDate = DateUtils.stringToDate(pubDateElement.getTextTrim());
	}

	public String getSourceName() throws MalformedFeedException {
		if(this.sourceName != null)
			return this.sourceName;

		Element sourceElement = this.itemElement.element("source");
		if(sourceElement == null)
			return null;

		return this.sourceName = sourceElement.getText();
	}

	public URL getSourceURL() throws MalformedFeedException, MalformedURLException {
		if(this.sourceLink != null)
			return this.sourceLink;

		Element sourceElement = this.itemElement.element("source");
		if(sourceElement == null)
			return null;

		Attribute urlAttribute = this.itemElement.attribute("url");
		if(urlAttribute == null)
			throw new MalformedFeedException("Missing required attribute URL for element Source.");

		return this.sourceLink = new URL(urlAttribute.getText());
	}

	public ITunesItemInfo getITunesInfo() {
		if(this.iTunesItemInfo != null)
			return this.iTunesItemInfo;

		return this.iTunesItemInfo = new ITunesItemInfo(this.itemElement);
	}

	public String getContentEncoded() {
		if(this.contentEncoded != null)
			return this.contentEncoded;

		Element contentEncodedElement = this.itemElement.element(QName.get("encoded", "content"));
		if(contentEncodedElement == null)
			return null;

		return this.contentEncoded = contentEncodedElement.getText();
	}
}
