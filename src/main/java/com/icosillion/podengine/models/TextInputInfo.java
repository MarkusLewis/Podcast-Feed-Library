package com.icosillion.podengine.models;

import org.dom4j.Element;

import java.net.MalformedURLException;
import java.net.URL;

public class TextInputInfo {

    private final Element textInputElement;

    //Caching
    private String title, description, name;
    private URL link;

    //TODO Throw error if required subelement is missing?

    public TextInputInfo(Element textInputElement) {
        this.textInputElement = textInputElement;
    }

    public String getTitle() {
        if(this.title != null)
            return this.title;

        Element titleElement = this.textInputElement.element("title");
        if(titleElement == null)
            return null;

        return this.title = titleElement.getText();
    }

    public String getDescription() {
        if(this.description != null)
            return this.description;

        Element descriptionElement = this.textInputElement.element("description");
        if(descriptionElement == null)
            return null;

        return this.description = descriptionElement.getText();
    }

    public String getName() {
        if(this.name != null)
            return this.name;

        Element nameElement = this.textInputElement.element("name");
        if(nameElement == null)
            return null;

        return this.name = nameElement.getText();
    }

    public URL getLink() throws MalformedURLException {
        if(this.link != null)
            return this.link;

        Element linkElement = this.textInputElement.element("link");
        if(linkElement == null)
            return null;

        return this.link = new URL(linkElement.getTextTrim());
    }
}
