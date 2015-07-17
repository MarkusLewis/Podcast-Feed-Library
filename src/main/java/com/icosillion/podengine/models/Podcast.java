package com.icosillion.podengine.models;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.icosillion.podengine.exceptions.MalformedFeedException;

public interface Podcast {

	/**
	 * Gets the title of the podcast from the stored XML data
	 * 
	 * @return
	 */
	public String getTitle();

	public String getDescription();

	public URL getLink() throws MalformedURLException;

	public String getLanguage();

	public String getCopyright();

	public String getManagingEditor();

	public String getWebMaster();

	public Date getPubDate();

	public Date getLastBuildDate();

	public String[] getCategories();

	public String getGenerator();

	public URL getDocs() throws MalformedURLException;

	public Object getCloud();

	public int getTTL();

	public URL getImageURL() throws MalformedURLException;

	public String[] getKeywords();

	//Some iTunes Specific Stuff
	public boolean isExplicit();

	//Episodes
	public List<Episode> getEpisodes() throws MalformedURLException, MalformedFeedException;

	public String getXMLData();
	
	public URL getFeedURL();

}