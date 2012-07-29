package com.icosillion.podengine.models;

import java.net.URL;
import java.util.Date;

public class Episode {
	
	protected String title;
	protected URL link;
	protected String description;
	protected String encodedContent;
	protected String author;
	protected String[] categories;
	protected URL comments;
	//Enclosure
	protected URL mediaLocation;
	protected long mediaLength;
	protected String mediaType;
	
	protected String guid;
	protected Date pubDate;
	protected String sourceName;
	protected URL sourceLink;
	
	public Episode() {
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public URL getLink() {
		return link;
	}
	
	public void setLink(URL link) {
		this.link = link;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEncodedContent() {
		return encodedContent;
	}

	public void setEncodedContent(String encodedContent) {
		this.encodedContent = encodedContent;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String[] getCategories() {
		return categories;
	}
	
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	
	public URL getComments() {
		return comments;
	}
	
	public void setComments(URL comments) {
		this.comments = comments;
	}
	
	public URL getMediaLocation() {
		return mediaLocation;
	}
	
	public void setMediaLocation(URL mediaLocation) {
		this.mediaLocation = mediaLocation;
	}
	
	public long getMediaLength() {
		return mediaLength;
	}
	
	public void setMediaLength(long mediaLength) {
		this.mediaLength = mediaLength;
	}
	
	public String getMediaType() {
		return mediaType;
	}
	
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public Date getPubDate() {
		return pubDate;
	}
	
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public URL getSourceLink() {
		return sourceLink;
	}
	
	public void setSourceLink(URL sourceLink) {
		this.sourceLink = sourceLink;
	}
	
}
