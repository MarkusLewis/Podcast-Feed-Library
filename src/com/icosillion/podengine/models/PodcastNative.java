package com.icosillion.podengine.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.icosillion.podengine.exceptions.MalformedFeedException;

public class PodcastNative implements Podcast {
	
	protected String title, description, language, copyright, managingEditor, webMaster, generator, xmlData;
	protected URL link, docs, image, feedURL;
	protected String[] categories, keywords;
	protected Date pubDate, lastBuildDate;
	protected boolean explicit;
	protected Object cloud;
	protected int ttl;
	protected List<Episode> episodes = new ArrayList<Episode>();
	
	public PodcastNative(Podcast podcast) {
		try {
			this.title = podcast.getTitle();
			this.description = podcast.getDescription();
			this.link = podcast.getLink();
			this.language = podcast.getLanguage();
			this.copyright = podcast.getCopyright();
			this.managingEditor = podcast.getManagingEditor();
			this.webMaster = podcast.getWebMaster();
			this.pubDate = podcast.getPubDate();
			this.lastBuildDate = podcast.getLastBuildDate();
			this.categories = podcast.getCategories();
			this.generator = podcast.getGenerator();
			this.docs = podcast.getDocs();
			this.cloud = podcast.getCloud();
			this.ttl = podcast.getTTL();
			this.image = podcast.getImageURL();
			this.keywords = podcast.getKeywords();
			this.explicit = podcast.isExplicit();
			this.episodes = podcast.getEpisodes();
			this.xmlData = podcast.getXMLData();
			this.feedURL = podcast.getFeedURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (MalformedFeedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public URL getLink() throws MalformedURLException {
		return link;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public String getManagingEditor() {
		return managingEditor;
	}

	@Override
	public String getWebMaster() {
		return webMaster;
	}

	@Override
	public Date getPubDate() {
		return pubDate;
	}

	@Override
	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	@Override
	public String[] getCategories() {
		return categories;
	}

	@Override
	public String getGenerator() {
		return generator;
	}

	@Override
	public URL getDocs() throws MalformedURLException {
		return docs;
	}

	@Override
	public Object getCloud() {
		return cloud;
	}

	@Override
	public int getTTL() {
		return ttl;
	}

	@Override
	public URL getImageURL() throws MalformedURLException {
		return image;
	}

	@Override
	public String[] getKeywords() {
		return keywords;
	}

	@Override
	public boolean isExplicit() {
		return explicit;
	}

	@Override
	public List<Episode> getEpisodes() throws MalformedURLException, MalformedFeedException {
		return episodes;
	}

	@Override
	public String getXMLData() {
		return xmlData;
	}
	
	@Override
	public URL getFeedURL() {
		return feedURL;
	}

}
