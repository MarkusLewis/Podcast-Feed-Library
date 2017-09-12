package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.utils.DateUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Podcast {

    private String xmlData;
    private Document document;
    private URL feedURL;
    private URL resolvedURL;

    private Element rootElement, channelElement;

    //Caching
    private String title, description, language, copyright, managingEditor, webMaster, pubDateString,
            lastBuildDateString, generator, picsRating, docsString;
    private URL link, docs;
    private Date pubDate, lastBuildDate;
    private CloudInfo cloudInfo;
    private Integer ttl;
    private TextInputInfo textInputInfo;
    private Set<Integer> skipHours;
    private Set<String> skipDays;
    private ITunesChannelInfo iTunesChannelInfo;
    private List<Episode> episodes;

    public Podcast(URL feed) throws InvalidFeedException, MalformedFeedException {
        HttpURLConnection ic = null;
        InputStream is = null;
        BOMInputStream bomInputStream = null;

        try {
            //Open Connection
            ic = (HttpURLConnection) feed.openConnection();
            ic.setInstanceFollowRedirects(true);
            ic.setRequestProperty("User-Agent", "PodEngine/2.2");
            is = ic.getInputStream();

            //Create BOMInputStream to strip any Byte Order Marks
            bomInputStream = new BOMInputStream(is, false);

            this.feedURL = feed;
            this.resolvedURL = ic.getURL();
            this.xmlData = IOUtils.toString(bomInputStream);
            this.document = DocumentHelper.parseText(xmlData);
            this.rootElement = this.document.getRootElement();
            this.channelElement = this.rootElement.element("channel");
            if (this.channelElement == null) {
                throw new MalformedFeedException("Missing required channel element.");
            }
        } catch (IOException e) {
            throw new InvalidFeedException("Error reading feed.", e);
        } catch (DocumentException e) {
            throw new InvalidFeedException("Error parsing feed XML.", e);
        } finally {
            IOUtils.closeQuietly(bomInputStream);
            IOUtils.closeQuietly(is);
        }
    }

    public Podcast(String xml) throws MalformedFeedException {
        try {
            this.xmlData = xml;
            this.document = DocumentHelper.parseText(this.xmlData);
            this.rootElement = this.document.getRootElement();
            this.channelElement = this.rootElement.element("channel");
            if (this.channelElement == null) {
                throw new MalformedFeedException("Missing required element 'channel'.");
            }
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
            if (this.channelElement == null) {
                throw new MalformedFeedException("Missing required element 'channel'.");
            }
        } catch (DocumentException e) {
            throw new MalformedFeedException("Error parsing document.", e);
        }
    }

    public String getTitle() throws MalformedFeedException {
        if (this.title != null)
            return this.title;

        Element titleElement = this.channelElement.element("title");
        if (titleElement == null) {
            throw new MalformedFeedException("Missing required title element.");
        }

        return this.title = titleElement.getText();
    }

    public String getDescription() throws MalformedFeedException {
        if (this.description != null)
            return this.description;

        Element descriptionElement = this.channelElement.element("description");
        if (descriptionElement == null) {
            throw new MalformedFeedException("Missing required description element.");
        }

        return this.description = descriptionElement.getText();
    }

    public URL getLink() throws MalformedURLException, MalformedFeedException {
        if (this.link != null) {
            return this.link;
        }

        Element linkElement = this.channelElement.element("link");
        if (linkElement == null)
            throw new MalformedFeedException("Missing required link element.");

        if ("atom".equalsIgnoreCase(linkElement.getNamespacePrefix())) {
            return new URL(linkElement.attributeValue("href"));
        }

        //TODO Handle URL Exceptions?

        return this.link = new URL(linkElement.getText());
    }

    //Optional Params (Can return null)

    public String getLanguage() {
        if (this.language != null)
            return this.language;

        Element languageElement = this.channelElement.element("language");
        if (languageElement == null) {
            return null;
        }

        return this.language = languageElement.getText();
    }

    public String getCopyright() {
        if (this.copyright != null) {
            return this.copyright;
        }

        Element copyrightElement = this.channelElement.element("copyright");
        if (copyrightElement == null) {
            return null;
        }

        return this.copyright = copyrightElement.getText();
    }

    public String getManagingEditor() {
        if (this.managingEditor != null) {
            return this.managingEditor;
        }

        Element managingEditorElement = this.channelElement.element("managingEditor");
        if (managingEditorElement == null)
            return null;

        return this.managingEditor = managingEditorElement.getText();
    }

    public String getWebMaster() {
        if (this.webMaster != null) {
            return this.webMaster;
        }

        Element webMasterElement = this.channelElement.element("webMaster");
        if (webMasterElement == null) {
            return null;
        }

        return this.webMaster = webMasterElement.getText();
    }

    public Date getPubDate() throws DateFormatException {
        if (this.pubDate != null) {
            return this.pubDate;
        }

        String pubDateString = getPubDateString();
        if (pubDateString == null) {
            return null;
        }

        return this.pubDate = DateUtils.stringToDate(pubDateString.trim());
    }

    public String getPubDateString() {
        if (this.pubDateString != null) {
            return this.pubDateString;
        }

        Element pubDateElement = this.channelElement.element("pubDate");
        if (pubDateElement == null) {
            return null;
        }

        return this.pubDateString = pubDateElement.getText();
    }

    public Date getLastBuildDate() throws DateFormatException {
        if (this.lastBuildDate != null) {
            return this.lastBuildDate;
        }

        String lastBuildDateString = getLastBuildDateString();
        if (lastBuildDateString == null) {
            return null;
        }

        return this.lastBuildDate = DateUtils.stringToDate(lastBuildDateString);
    }

    public String getLastBuildDateString() {
        if (this.lastBuildDateString != null) {
            return this.lastBuildDateString;
        }

        Element lastBuildDateElement = this.channelElement.element("lastBuildDate");
        if (lastBuildDateElement == null) {
            return null;
        }

        return this.lastBuildDateString = lastBuildDateElement.getText();
    }

    //TODO Update this with caching
    public String[] getCategories() {
        List<String> categories = new ArrayList<>();
        Element rootElement = this.document.getRootElement();
        Element channel = rootElement.element("channel");
        boolean hasiTunes = false;
        if (channel.element("category") != null) {
            for (Element child : (List<Element>) channel.elements("category")) {
                if (!"itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
                    categories.add(child.getText());
                } else if ("itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
                    hasiTunes = true;
                    //Clear Categories
                    categories.clear();
                    if (child.elements("category").size() == 0) {
                        if (child.attribute("text") != null) {
                            categories.add(child.attributeValue("text"));
                        } else {
                            categories.add(child.getText());
                        }
                    } else {
                        String finalCategory;
                        if (child.attribute("text") != null) {
                            finalCategory = child.attributeValue("text");
                        } else {
                            finalCategory = child.getText();
                        }

                        for (Element category : (List<Element>) child.elements("category")) {
                            if (category.attribute("text") != null) {
                                finalCategory += " > " + category.attributeValue("text");
                            } else {
                                finalCategory += " > " + category.getText();
                            }
                        }
                        categories.add(finalCategory);
                    }

                } else if (hasiTunes && "itunes".equalsIgnoreCase(child.getNamespacePrefix())) {
                    if (child.elements("category").size() == 0) {
                        if (child.attribute("text") != null) {
                            categories.add(child.attributeValue("text"));
                        } else {
                            categories.add(child.getText());
                        }
                    } else {
                        String finalCategory;
                        if (child.attribute("text") != null) {
                            finalCategory = child.attributeValue("text");
                        } else {
                            finalCategory = child.getText();
                        }

                        for (Element category : (List<Element>) child.elements("category")) {
                            if (category.attribute("text") != null) {
                                finalCategory += " > " + category.attributeValue("text");
                            } else {
                                finalCategory += " > " + category.getText();
                            }
                        }
                        categories.add(finalCategory);
                    }
                }
            }
        }

        if (categories.size() == 0) {
            return new String[0];
        }

        String[] output = new String[categories.size()];
        categories.toArray(output);
        return output;
    }

    public String getGenerator() {
        if (this.generator != null) {
            return this.generator;
        }

        Element generatorElement = this.channelElement.element("generator");
        if (generatorElement == null) {
            return null;
        }

        return this.generator = generatorElement.getText();
    }

    public URL getDocs() throws MalformedURLException {
        if (this.docs != null) {
            return this.docs;
        }

        String docsString = this.getDocsString();
        if (docsString == null) {
            return null;
        }

        return this.docs = new URL(docsString);
    }

    public String getDocsString() {
        if (this.docsString != null) {
            return this.docsString;
        }

        Element docsElement = this.channelElement.element("docs");
        if (docsElement == null) {
            return null;
        }

        return this.docsString = docsElement.getText();
    }

    public CloudInfo getCloud() {
        if (this.cloudInfo != null) {
            return this.cloudInfo;
        }

        Element cloudElement = this.channelElement.element("cloud");
        if (cloudElement == null) {
            return null;
        }

        return this.cloudInfo = new CloudInfo(cloudElement);
    }

    public Integer getTTL() {
        if (this.ttl != null) {
            return this.ttl;
        }

        Element ttlElement = this.channelElement.element("ttl");
        if (ttlElement == null) {
            return null;
        }

        try {
            return this.ttl = Integer.valueOf(ttlElement.getTextTrim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public URL getImageURL() throws MalformedURLException {
        Element thumbnailElement = this.channelElement.element("thumbnail");
        if (thumbnailElement != null)
            return new URL(thumbnailElement.attributeValue("url"));
        for (Element image : (List<Element>) this.channelElement.elements("image")) {
            if ("itunes".equalsIgnoreCase(image.getNamespacePrefix())) {
                return new URL(image.attributeValue("href"));
            } else if (image.element("url") != null) {
                return new URL(image.element("url").getText());
            }
        }

        return null;
    }

    public String getPICSRating() {
        if (this.picsRating != null) {
            return this.picsRating;
        }

        Element ratingElement = this.channelElement.element("rating");
        if (ratingElement == null) {
            return null;
        }

        return this.picsRating = ratingElement.getText();
    }

    public TextInputInfo getTextInput() {
        if (this.textInputInfo != null)
            return this.textInputInfo;

        Element textInputElement = this.channelElement.element("textInput");
        if (textInputElement == null) {
            return null;
        }

        return this.textInputInfo = new TextInputInfo(textInputElement);
    }

    public Set<Integer> getSkipHours() throws MalformedFeedException {
        if (this.skipHours != null) {
            return this.skipHours;
        }

        Element skipHoursElement = this.channelElement.element("skipHours");
        if (skipHoursElement == null) {
            return null;
        }

        List hourElements = skipHoursElement.elements("hour");
        if (hourElements.size() == 0) {
            return null;
        }

        Set<Integer> skipHours = new HashSet<>();

        for (Object hourObject : hourElements) {
            if (hourObject instanceof Element) {
                Element hourElement = (Element) hourObject;
                int hour;
                try {
                    hour = Integer.valueOf(hourElement.getTextTrim());
                } catch (NumberFormatException e) {
                    throw new MalformedFeedException("Invalid hour in skipHours element.");
                }

                if (hour < 0 || hour > 23) {
                    throw new MalformedFeedException("Hour in skipHours element is outside of valid range 0 - 23");
                }

                skipHours.add(hour);
            }
        }

        if (skipHours.size() == 0) {
            return null;
        }

        return this.skipHours = Collections.unmodifiableSet(skipHours);
    }

    public Set<String> getSkipDays() throws MalformedFeedException {
        if (this.skipDays != null) {
            return this.skipDays;
        }

        Element skipDaysElement = this.channelElement.element("skipDays");
        if (skipDaysElement == null) {
            return null;
        }

        List dayElements = skipDaysElement.elements("day");
        if (dayElements.size() == 0) {
            return null;
        }

        if (dayElements.size() > 7) {
            throw new MalformedFeedException("More than 7 day elements present within skipDays element.");
        }

        Set<String> skipDays = new HashSet<>();

        final String[] validDays = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
                "Sunday"};

        for (Object dayObject : dayElements) {
            if (dayObject instanceof Element) {
                Element dayElement = (Element) dayObject;
                String day = dayElement.getTextTrim();
                if (day == null || day.isEmpty())
                    continue;

                boolean valid = false;
                for (String validDay : validDays) {
                    if (day.equalsIgnoreCase(validDay))
                        valid = true;
                }

                if (valid)
                    skipDays.add(day);
            }
        }

        if (skipDays.size() == 0) {
            return null;
        }

        return this.skipDays = Collections.unmodifiableSet(skipDays);
    }

    //TODO Update this with caching and convert to Set<String>
    public String[] getKeywords() {
        List<String> keywords = new ArrayList<>();
        Element rootElement = this.document.getRootElement();
        Element channel = rootElement.element("channel");
        boolean hasiTunes = false;
        if (channel.element("keywords") != null) {
            for (Element child : (List<Element>) channel.elements("keywords")) {
                if (!"itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
                    for (String kw : child.getText().split(",")) {
                        keywords.add(kw.trim());
                    }
                } else if ("itunes".equalsIgnoreCase(child.getNamespacePrefix()) && !hasiTunes) {
                    hasiTunes = true;
                    //Clear Categories
                    keywords.clear();
                    for (String kw : child.getText().split(",")) {
                        keywords.add(kw.trim());
                    }
                } else if (hasiTunes) {
                    for (String kw : child.getText().split(",")) {
                        keywords.add(kw.trim());
                    }
                }
            }
        }

        if (keywords.size() == 0) {
            return new String[0];
        }

        String[] output = new String[keywords.size()];
        keywords.toArray(output);
        return output;
    }

    //Episodes
    public List<Episode> getEpisodes() {
        if (this.episodes != null) {
            return this.episodes;
        }

        List<Episode> episodes = new ArrayList<>();
        for (Object itemObject : this.channelElement.elements("item")) {
            if (!(itemObject instanceof Element)) {
                continue;
            }

            episodes.add(new Episode((Element) itemObject));
        }

        if (episodes.size() == 0) {
            return null;
        }

        return this.episodes = Collections.unmodifiableList(episodes);
    }

    public ITunesChannelInfo getITunesInfo() {
        if (this.iTunesChannelInfo != null) {
            return this.iTunesChannelInfo;
        }

        return this.iTunesChannelInfo = new ITunesChannelInfo(this.channelElement);
    }

    public String getXMLData() {
        return this.xmlData;
    }

    public URL getFeedURL() {
        return feedURL;
    }

    public URL getResolvedURL() {
        return resolvedURL;
    }
}
