package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.QName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ITunesChannelInfo extends ITunesInfo {

    public enum FeedType {
        EPISODIC, SERIAL
    }

    private Set<Category> categories;
    private Boolean complete;
    private URL newFeedURL;
    private ITunesOwner owner;
    private FeedType type;

    public ITunesChannelInfo(Element parent) {
        super(parent);
    }

    public Set<Category> getCategories() {
        if (this.categories != null) {
            return this.categories;
        }

        this.categories = new HashSet<>();

        List<Element> categoryElements = this.parent.elements(QName.get("category", this.iTunesNamespace));
        for (Element categoryElement : categoryElements) {
            Set<Category> subcategories = new HashSet<>();

            List<Element> subcategoryElements = categoryElement.elements(QName.get("category", this.iTunesNamespace));
            for (Element subcategoryElement : subcategoryElements) {
                subcategories.add(new Category(subcategoryElement.attributeValue("text")));
            }

            Category category = new Category(categoryElement.attributeValue("text"), subcategories);

            this.categories.add(category);
        }

        return this.categories;
    }

    public boolean isComplete() {
        if (this.complete != null) {
            return this.complete;
        }

        Element completeElement = this.parent.element(QName.get("complete", this.iTunesNamespace));
        if (completeElement == null) {
            return this.complete = false;
        }

        if ("yes".equalsIgnoreCase(completeElement.getTextTrim())) {
            return this.complete = true;
        }

        return this.complete = false;
    }

    public URL getNewFeedURL() throws MalformedURLException {
        if (this.newFeedURL != null) {
            return this.newFeedURL;
        }

        Element newFeedURLElement = this.parent.element(QName.get("new-feed-url", this.iTunesNamespace));
        if (newFeedURLElement == null) {
            return null;
        }

        return this.newFeedURL = new URL(newFeedURLElement.getTextTrim());
    }

    public ITunesOwner getOwner() {
        if (this.owner != null) {
            return this.owner;
        }

        Element ownerElement = this.parent.element(QName.get("owner", this.iTunesNamespace));
        if (ownerElement == null) {
            return null;
        }

        return this.owner = new ITunesOwner(ownerElement);
    }

    public FeedType getType() {
        if (this.type != null) {
            return this.type;
        }

        Element typeElement = this.parent.element(QName.get("type", this.iTunesNamespace));
        if (typeElement == null) {
            return this.type = FeedType.EPISODIC;
        }

        String rawType = typeElement.getTextTrim().toLowerCase();

        if (rawType.equals("episodic")) {
            this.type = FeedType.EPISODIC;
        } else if (rawType.equals("serial")) {
            this.type = FeedType.SERIAL;
        }

        return this.type;
    }
}
