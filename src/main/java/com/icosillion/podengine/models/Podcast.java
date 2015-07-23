package com.icosillion.podengine.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import com.icosillion.podengine.exceptions.InvalidFeedException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.icosillion.podengine.exceptions.MalformedFeedException;

public class Podcast {
	
	protected String xmlData;
	protected Document document;
	protected URL feedURL;
	//TODO Add support for HTTP basic-auth

	private Element rootElement, channelElement;

	//Caching
	private String title, description, language, copyright, managingEditor, webMaster, pubDateString,
			lastBuildDateString, generator, picsRating;
	private URL link, docs;
	private Date pubDate, lastBuildDate;
	private CloudInfo cloudInfo;
	private Integer ttl;
	private TextInputInfo textInputInfo;
	private Set<Integer> skipHours;
	private Set<String> skipDays;
    private ITunesChannelInfo iTunesChannelInfo;
	
	public Podcast(URL feed) throws InvalidFeedException, MalformedFeedException {
		this.feedURL = feed;
		URLConnection connection;
		try {
			connection = feed.openConnection();
			connection.connect();
			this.xmlData = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) {
				this.xmlData += line + "\n";
			}
			reader.close();
			this.document = DocumentHelper.parseText(this.xmlData);
		} catch (IOException e) {
			throw new InvalidFeedException("Error reading feed.", e);
		} catch (DocumentException e) {
			throw new MalformedFeedException("Error parsing feed.", e);
		}
	}
	
	public Podcast(String xml) throws MalformedFeedException {
		try {
			this.xmlData = xml;
			this.document = DocumentHelper.parseText(this.xmlData);
			this.rootElement = this.document.getRootElement();
			this.channelElement = this.rootElement.element("channel");
			if(this.channelElement == null)
				throw new MalformedFeedException("Missing required element 'channel'.");
		} catch (DocumentException e) {
			throw new MalformedFeedException("Error parsing feed.", e);
		}
	}
	
	public Podcast(String xml, URL feed) throws MalformedFeedException {
		try {
			this.xmlData = xml;
			this.feedURL = feed;
			this.document = DocumentHelper.parseText(this.xmlData);
			this.rootElement = this.document.getRootElement();
			this.channelElement = this.rootElement.element("channel");
			if(this.channelElement == null)
				throw new MalformedFeedException("Missing required element 'channel'.");
		} catch (DocumentException e) {
			throw new MalformedFeedException("Error parsing document.", e);
		}
	}

	public String getTitle() throws MalformedFeedException {
		if(this.title != null)
			return this.title;

		Element titleElement = this.channelElement.element("title");
		if(titleElement == null)
			throw new MalformedFeedException("Missing required title element.");

		return this.title = titleElement.getText();
	}

	public String getDescription() throws MalformedFeedException {
		if(this.description != null)
			return this.description;

		Element descriptionElement = this.channelElement.element("description");
		if(descriptionElement == null)
			throw new MalformedFeedException("Missing required description element.");

		return this.description = descriptionElement.getText();
	}

	public URL getLink() throws MalformedURLException, MalformedFeedException {
		if(this.link != null)
			return this.link;

		Element linkElement = this.channelElement.element("link");
		if(linkElement == null)
			throw new MalformedFeedException("Missing required link element.");

		if("atom".equalsIgnoreCase(linkElement.getNamespacePrefix()))
			return new URL(linkElement.attributeValue("href"));

		//TODO Handle URL Exceptions?

		return this.link = new URL(linkElement.getText());
	}
	
	//Optional Params (Can return null)

	public String getLanguage() {
		if(this.language != null)
			return this.language;

		Element languageElement = this.channelElement.element("language");
		if(languageElement == null)
			return null;

		return this.language = languageElement.getText();
	}

	public String getCopyright() {
		if(this.copyright != null)
			return this.copyright;

		Element copyrightElement = this.channelElement.element("copyright");
		if(copyrightElement == null)
			return null;
		
		return this.copyright = copyrightElement.getText();
	}

	public String getManagingEditor() {
		if(this.managingEditor != null)
			return this.managingEditor;

		Element managingEditorElement = this.channelElement.element("managingEditor");
		if(managingEditorElement == null)
			return null;
		
		return this.managingEditor = managingEditorElement.getText();
	}

	public String getWebMaster() {
		if(this.webMaster != null)
			return this.webMaster;

		Element webMasterElement = this.channelElement.element("webMaster");
		if(webMasterElement == null)
			return null;

		return this.webMaster = webMasterElement.getText();
	}

	public Date getPubDate() {
		if(this.pubDate != null)
			return this.pubDate;

		String pubDateString = getPubDateString();
		if(pubDateString == null)
			return null;
		
		return this.pubDate = stringToDate(pubDateString.trim());
	}

	public String getPubDateString() {
		if(this.pubDateString != null)
			return this.pubDateString;

		Element pubDateElement = this.channelElement.element("pubDate");
		if(pubDateElement == null)
			return null;

		return this.pubDateString = pubDateElement.getText();
	}

	public Date getLastBuildDate() {
		if(this.lastBuildDate != null)
			return this.lastBuildDate;

		String lastBuildDateString = getLastBuildDateString();
		if(lastBuildDateString == null)
			return null;

		return this.lastBuildDate = stringToDate(lastBuildDateString);
	}

	public String getLastBuildDateString() {
		if(this.lastBuildDateString != null)
			return this.lastBuildDateString;

		Element lastBuildDateElement = this.channelElement.element("lastBuildDate");
		if(lastBuildDateElement == null)
			return null;

		return this.lastBuildDateString = lastBuildDateElement.getText();
	}

	//TODO Update this with caching
	public String[] getCategories() {
		List<String> categories = new ArrayList<String>();
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		boolean hasiTunes = false;
		if(channel.element("category") != null) {
			for(Element child : (List<Element>) channel.elements("category")) {
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
						
						for(Element category : (List<Element>) child.elements("category")) {
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
						
						for(Element category : (List<Element>) child.elements("category")) {
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
		if(this.generator != null)
			return this.generator;

		Element generatorElement = this.channelElement.element("generator");
		if(generatorElement == null)
			return null;

		return this.generator = generatorElement.getText();
	}

	public URL getDocs() throws MalformedURLException {
		if(this.docs != null)
			return this.docs;

		Element docsElement = this.channelElement.element("docs");
		if(docsElement == null)
			return null;

		return this.docs = new URL(docsElement.getText());
	}

	public CloudInfo getCloud() {
		if(this.cloudInfo != null)
			return this.cloudInfo;

		Element cloudElement = this.channelElement.element("cloud");
		if(cloudElement == null)
			return null;

		return this.cloudInfo = new CloudInfo(cloudElement);
	}

	public Integer getTTL() {
		if(this.ttl != null)
			return this.ttl;

		Element ttlElement = this.channelElement.element("ttl");
		if(ttlElement == null)
			return null;

		try {
			return this.ttl = Integer.valueOf(ttlElement.getTextTrim());
		} catch(NumberFormatException e) {
			return null;
		}
	}

	public URL getImageURL() throws MalformedURLException {
		Element thumbnailElement = this.channelElement.element("thumbnail");
		if(thumbnailElement != null)
			return new URL(thumbnailElement.attributeValue("url"));
		for(Element image : (List<Element>) this.channelElement.elements("image")) {
			if("itunes".equalsIgnoreCase(image.getNamespacePrefix()))
				return new URL(image.attributeValue("href"));
			else if(image.element("url") != null)
				return new URL(image.element("url").getText());
		}
		
		return null;
	}
	
	public String getPICSRating() {
		if(this.picsRating != null)
			return this.picsRating;

		Element ratingElement = this.channelElement.element("rating");
		if(ratingElement == null)
			return null;

		return this.picsRating = ratingElement.getText();
	}
	
	public TextInputInfo getTextInput() {
		if(this.textInputInfo != null)
			return this.textInputInfo;

		Element textInputElement = this.channelElement.element("textInput");
		if(textInputElement == null)
			return null;

		return this.textInputInfo = new TextInputInfo(textInputElement);
	}

	public Set<Integer> getSkipHours() throws MalformedFeedException {
		if(this.skipHours != null)
			return this.skipHours;

		Element skipHoursElement = this.channelElement.element("skipHours");
		if(skipHoursElement == null)
			return null;

		List hourElements = skipHoursElement.elements("hour");
		if(hourElements.size() == 0)
			return null;

		Set<Integer> skipHours = new HashSet<>();

		for(Object hourObject : hourElements) {
			if(hourObject instanceof Element) {
				Element hourElement = (Element) hourObject;
				int hour;
				try {
					hour = Integer.valueOf(hourElement.getTextTrim());
				} catch(NumberFormatException e) {
					throw new MalformedFeedException("Invalid hour in skipHours element.");
				}

				if(hour < 0 || hour > 23)
					throw new MalformedFeedException("Hour in skipHours element is outside of valid range 0 - 23");

				skipHours.add(hour);
			}
		}

		if(skipHours.size() == 0)
			return null;

		return this.skipHours = Collections.unmodifiableSet(skipHours);
	}

	public Set<String> getSkipDays() throws MalformedFeedException {
		if(this.skipDays != null)
			return this.skipDays;

		Element skipDaysElement = this.channelElement.element("skipDays");
		if(skipDaysElement == null)
			return null;

		List dayElements = skipDaysElement.elements("day");
		if(dayElements.size() == 0)
			return null;

		if(dayElements.size() > 7)
			throw new MalformedFeedException("More than 7 day elements present within skipDays element.");

		Set<String> skipDays = new HashSet<>();

		final String[] validDays = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
													"Sunday" };

		for(Object dayObject : dayElements) {
			if(dayObject instanceof Element) {
				Element dayElement = (Element) dayObject;
				String day = dayElement.getTextTrim();
				if(day == null || day.isEmpty())
					continue;

				boolean valid = false;
				for(String validDay : validDays) {
					if(day.equalsIgnoreCase(validDay))
						valid = true;
				}

				if(valid)
					skipDays.add(day);
			}
		}

		if(skipDays.size() == 0)
			return null;

		return this.skipDays = Collections.unmodifiableSet(skipDays);
	}

	//TODO Update this with caching and convert to Set<String>
	public String[] getKeywords() {
		List<String> keywords = new ArrayList<String>();
		Element rootElement = this.document.getRootElement();
		Element channel = rootElement.element("channel");
		boolean hasiTunes = false;
		if(channel.element("keywords") != null) {
			for(Element child : (List<Element>) channel.elements("keywords")) {
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
	//TODO Update this with caching
	public boolean isExplicit() {
		Element explicitElement = this.channelElement.element("explicit");
		if(explicitElement != null && "itunes".equalsIgnoreCase(explicitElement.getNamespacePrefix()))
			return "yes".equalsIgnoreCase(explicitElement.getTextTrim());
		
		return false;
	}
	
	//Episodes
	//TODO Update this with caching
	//TODO Rewrite that with new element syntax used within rest of code (It's far more efficient)
	public List<Episode> getEpisodes() throws MalformedURLException, MalformedFeedException {
		List<Episode> episodes = new ArrayList<>();
		for(Element item : (List<Element>) this.channelElement.elements("item")) {
			Episode episode = new Episode();

			//Required Tags

			//Title
			Element titleElement = item.element("title");
			if(titleElement == null)
				throw new MalformedFeedException("Item is missing required element title.");
			episode.setTitle(titleElement.getText());

			//Description
			Element descriptionElement = item.element("description");
			if(descriptionElement == null)
				throw new MalformedFeedException("Item is missing required element descripton.");

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
			List<String> categories = new ArrayList<>();
			for(Element category : (List<Element>) item.elements("category"))
				categories.add(category.getText());
			
			if(categories.size() > 0) {
				String[] categoryArray = new String[categories.size()];
				categories.toArray(categoryArray);
				episode.setCategories(categoryArray);
			} else
				episode.setCategories(new String[0]);
			
			Element commentsElement = item.element("comments");
			if(commentsElement != null)
				episode.setComments(new URL(commentsElement.getTextTrim()));
			
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

    public ITunesChannelInfo getITunesInfo() {
        if(this.iTunesChannelInfo != null)
            return this.iTunesChannelInfo;

        return this.iTunesChannelInfo = new ITunesChannelInfo(this.channelElement);
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

	public URL getFeedURL() {
		return feedURL;
	}
}
