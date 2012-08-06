package com.icosillion.podengine.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.icosillion.podengine.exceptions.MalformedFeedException;

public class Podcast {
	
	protected String xmlData;
	protected Document document;
	
	//TODO Add support for HTTP basic-auth
	
	public Podcast(URL feed) throws IOException, DocumentException {
		URLConnection connection = feed.openConnection();
		connection.connect();
		this.xmlData = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while((line = reader.readLine()) != null) {
			this.xmlData += line + "\n";
		}
		reader.close();
		this.document = DocumentHelper.parseText(this.xmlData);
	}
	
	public Podcast(String xml) throws DocumentException {
		this.xmlData = xml;
		this.document = DocumentHelper.parseText(this.xmlData);
	}
	
	/**
	 * Gets the title of the podcast from the stored XML data
	 * 
	 * @return
	 */
	public String getTitle() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		return channel.elementText("title");
	}
	
	public String getDescription() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		return channel.elementText("description");
	}
	
	public URL getLink() throws MalformedURLException {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if("atom".equalsIgnoreCase(channel.element("link").getNamespacePrefix()))
			return new URL(channel.element("link").attributeValue("href"));
		return new URL(channel.elementText("link"));
	}
	
	//Optional Params (Can return null)
	
	public String getLanguage() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("language") != null)
			return channel.elementText("language");
		
		return null;
	}
	
	public String getCopyright() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("copyright") != null)
			return channel.elementText("copyright");
		
		return null;
	}
	
	public String getManagingEditor() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("managingEditor") != null)
			return channel.elementText("managingEditor");
		
		return null;
	}
	
	public String getWebMaster() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("webMaster") != null)
			return channel.elementText("webMaster");
		
		return null;
	}
	
	public Date getPubDate() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("pubDate") != null) {
			String dt = channel.elementText("pubDate").trim();
			return this.stringToDate(dt);
		}
		
		return null;
	}
	
	public Date getLastBuildDate() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("lastBuildDate") != null) {
			String dt = channel.elementText("lastBuildDate").trim();
			return this.stringToDate(dt);
		}
		
		return null;
	}
	
	public String[] getCategories() {
		List<String> categories = new ArrayList<String>();
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		boolean hasiTunes = false;
		if(channel.element("category") != null) {
			for(Element child : (List<Element>)channel.elements("category")) {
				if(!"itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
					categories.add(child.getText());
				} else if("itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
					hasiTunes = true;
					//Clear Categories
					categories.clear();
					if(child.elements("category").size() == 0) {
						if(child.attribute("text") != null)
							categories.add(child.attributeValue("text"));
						else
							categories.add(child.getText());
					} else {
						String finalCategory = "";
						if(child.attribute("text") != null)
							finalCategory = child.attributeValue("text");
						else
							finalCategory = child.getText();
						
						for(Element category : (List<Element>)child.elements("category")) {
							if(category.attribute("text") != null)
								finalCategory += " > " + category.attributeValue("text");
							else
								finalCategory += " > " + category.getText();
						}
						categories.add(finalCategory);
					}
					
				} else if(hasiTunes && "itunes".equalsIgnoreCase(child.getNamespacePrefix())) {
					if(child.elements("category").size() == 0) {
						if(child.attribute("text") != null)
							categories.add(child.attributeValue("text"));
						else
							categories.add(child.getText());
					} else {
						String finalCategory = "";
						if(child.attribute("text") != null)
							finalCategory = child.attributeValue("text");
						else
							finalCategory = child.getText();
						
						for(Element category : (List<Element>)child.elements("category")) {
							if(category.attribute("text") != null)
								finalCategory += " > " + category.attributeValue("text");
							else
								finalCategory += " > " + category.getText();
						}
						categories.add(finalCategory);
					}
				}
			}
		}
		
		if(categories.size() == 0) return new String[0];
		String[] output = new String[categories.size()];
		categories.toArray(output);
		return output;
	}
	
	public String getGenerator() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("generator") != null)
			return channel.elementText("generator");
		
		return null;
	}
	
	public URL getDocs() throws MalformedURLException {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("docs") != null)
			return new URL(channel.elementText("docs"));
		
		return null;
	}
	
	public Object getCloud() {
		//TODO
		return null;
	}
	
	public int getTTL() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("ttl") != null)
			return Integer.valueOf(channel.elementText("ttl"));
		
		return -1;
	}
	
	public URL getImageURL() throws MalformedURLException {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("thumbnail") != null)
			return new URL(channel.element("thumbnail").attributeValue("url"));
		for(Element image : (List<Element>)channel.elements("image")) {
			if("itunes".equalsIgnoreCase(image.getNamespacePrefix()))
				return new URL(image.attributeValue("href"));
			else if(image.element("url") != null)
				return new URL(image.element("url").getText());
		}
		
		return null;
	}
	
	//TODO Add getPICSRating()
	
	//IGNORED getTextInput()
	
	//IGNORED getSkipHours() / getSkipDays()
	
	public String[] getKeywords() {
		List<String> keywords = new ArrayList<String>();
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		boolean hasiTunes = false;
		if(channel.element("keywords") != null) {
			for(Element child : (List<Element>)channel.elements("keywords")) {
				if(!"itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
					for(String kw : child.getText().split(","))
						keywords.add(kw.trim());
				} else if("itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
					hasiTunes = true;
					//Clear Categories
					keywords.clear();
					for(String kw : child.getText().split(","))
						keywords.add(kw.trim());
				} else if(hasiTunes) {
					for(String kw : child.getText().split(","))
						keywords.add(kw.trim());
				}
			}
		}
		
		if(keywords.size() == 0) return new String[0];
		String[] output = new String[keywords.size()];
		keywords.toArray(output);
		return output;
	}
	
	//Some iTunes Specific Stuff
	public boolean isExplicit() {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		if(channel.element("explicit") != null && "itunes".equalsIgnoreCase(channel.element("explicit").getNamespacePrefix())) {
			return "yes".equalsIgnoreCase(channel.elementText("explicit"));
		}
		
		return false;
	}
	
	//Episodes
	public List<Episode> getEpisodes() throws MalformedURLException, MalformedFeedException {
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		List<Episode> episodes = new ArrayList<Episode>();
		for(Element item : (List<Element>)channel.elements("item")) {
			Episode episode = new Episode();
			//Required Tags
			boolean hasOne = false;
			//Title
			if(item.element("title") != null) {
				episode.setTitle(item.elementText("title"));
				hasOne = true;
			}
			//Description
			if(item.element("description") != null) {
				episode.setDescription(item.elementText("description"));
				hasOne = true;
			}
			
			if(!hasOne) throw new MalformedFeedException("Item contains neither title nor description", null);
			
			//Optional Tags
			//Link
			if(item.element("link") != null) {
				URL link = null;
				if("atom".equalsIgnoreCase(item.element("link").getNamespacePrefix()))
					link = new URL(item.element("link").attributeValue("href"));
				link =  new URL(item.elementText("link"));
				episode.setLink(link);
			}
			
			//Encoded Content
			if(item.element("encoded") != null && "content".equalsIgnoreCase(item.element("encoded").getNamespacePrefix()))
				episode.setEncodedContent(item.elementText("encoded"));
			
			//Author
			if(item.element("author") != null)
				episode.setAuthor(item.elementText("author"));
			
			//Categories
			List<String> categories = new ArrayList<String>();
			for(Element category : (List<Element>)item.elements("category"))
				categories.add(category.getText());
			
			if(categories.size() > 0) {
				String[] categoryArray = new String[categories.size()];
				categories.toArray(categoryArray);
				episode.setCategories(categoryArray);
			} else
				episode.setCategories(new String[0]);
			
			//TODO Comments
			
			//Enclosure
			if(item.element("enclosure") != null) {
				episode.setMediaLocation(new URL(item.element("enclosure").attributeValue("url")));
				episode.setMediaLength(Integer.valueOf(item.element("enclosure").attributeValue("length")));
				episode.setMediaType(item.element("enclosure").attributeValue("type"));
			}
			
			//GUID
			if(item.element("guid") != null)
				episode.setGuid(item.elementText("guid"));
			
			//PubDate
			if(item.element("pubDate") != null)
				episode.setPubDate(this.stringToDate(item.elementText("pubDate")));
			
			//Source
			if(item.element("source") != null) {
				episode.setSourceLink(new URL(item.element("source").attributeValue("url")));
				episode.setSourceName(item.element("source").attributeValue("name"));
			}
			
			episodes.add(episode);
		}
		
		return episodes;
	}
	
	//Helper Functions
	protected int getMonthFromCode(String code) {
		if("Jan".equalsIgnoreCase(code))
			return 0;
		else if("Feb".equalsIgnoreCase(code))
			return 1;
		else if("Mar".equalsIgnoreCase(code))
			return 2;
		else if("Apr".equalsIgnoreCase(code))
			return 3;
		else if("May".equalsIgnoreCase(code))
			return 4;
		else if("Jun".equalsIgnoreCase(code))
			return 5;
		else if("Jul".equalsIgnoreCase(code))
			return 6;
		else if("Aug".equalsIgnoreCase(code))
			return 7;
		else if("Sep".equalsIgnoreCase(code))
			return 8;
		else if("Oct".equalsIgnoreCase(code))
			return 9;
		else if("Nov".equalsIgnoreCase(code))
			return 10;
		else if("Dec".equalsIgnoreCase(code))
			return 11;
		
		return -1;
	}
	
	protected Date stringToDate(String dt) {
		Calendar calendar = Calendar.getInstance();
		String[] parts = dt.split(" ");
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parts[1]));
		calendar.set(Calendar.MONTH, this.getMonthFromCode(parts[2]));
		if(parts[3].length() == 4)
			calendar.set(Calendar.YEAR, Integer.valueOf(parts[3]));
		else {
			int year = Integer.valueOf(parts[3]);
			if(year < 80)
				year = 2000 + year;
			else
				year = 1900 + year;
			calendar.set(Calendar.YEAR, year);
		}
		String[] time = parts[4].split(":");
		calendar.set(Calendar.HOUR, Integer.valueOf(time[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
		calendar.set(Calendar.SECOND, Integer.valueOf(time[2]));
		int offset = 0;
		if("EST".equals(parts[5]))
			offset = -5 * 3600000;
		else if("EDT".equals(parts[5]))
			offset = -4 * 3600000;
		else if("CST".equals(parts[5]))
			offset = -6 * 3600000;
		else if("CDT".equals(parts[5]))
			offset = -5 * 3600000;
		else if("MST".equals(parts[5]))
			offset = -7 * 3600000;
		else if("MDT".equals(parts[5]))
			offset = -6 * 3600000;
		else if("PST".equals(parts[5]))
			offset = -8 * 3600000;
		else if("PDT".equals(parts[5]))
			offset = -7 * 3600000;
		else if(parts[5].startsWith("+")) {
			offset = Integer.valueOf(parts[5].substring(1, 3)) * 3600000;
			offset += Integer.valueOf(parts[5].substring(3, 5)) * 60000;
		}
		calendar.set(Calendar.ZONE_OFFSET, offset);
		return calendar.getTime();
	}
	
	public String getXMLData() {
		return this.xmlData;
	}
}
