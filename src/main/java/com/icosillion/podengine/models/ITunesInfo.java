package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ITunesInfo {

    public enum ExplicitLevel {
        EXPLICIT, CLEAN, UNKNOWN
    }

    protected final Element parent;
    protected final Namespace iTunesNamespace;
    private String author, subtitle, summary, imageString;
    private ExplicitLevel explicit;
    private Boolean block;
    private URL image;

    public ITunesInfo(Element parent) {
        this.parent = parent;
        this.iTunesNamespace = this.parent.getNamespaceForPrefix("itunes");
    }

    public String getAuthor() {
        if (this.author != null) {
            return this.author;
        }

        Element authorElement = this.parent.element(QName.get("author", this.iTunesNamespace));
        if (authorElement == null) {
            return null;
        }

        return this.author = authorElement.getText();
    }

    public String getSubtitle() {
        if (this.subtitle != null) {
            return this.subtitle;
        }

        Element subtitleElement = this.parent.element(QName.get("subtitle", this.iTunesNamespace));
        if (subtitleElement == null) {
            return null;
        }

        return this.subtitle = subtitleElement.getText();
    }

    public String getSummary() {
        if (this.summary != null) {
            return this.summary;
        }

        Element summaryElement = this.parent.element(QName.get("summary", this.iTunesNamespace));
        if (summaryElement == null) {
            return null;
        }

        return this.summary = summaryElement.getText();
    }

    public boolean isBlocked() {
        if (this.block != null) {
            return this.block;
        }

        Element blockElement = this.parent.element(QName.get("block", this.iTunesNamespace));
        if (blockElement == null) {
            return this.block = false;
        }

        return this.block = "yes".equalsIgnoreCase(blockElement.getTextTrim());
    }

    public ExplicitLevel getExplicit() {
        if (this.explicit != null) {
            return this.explicit;
        }

        Element explicitElement = this.parent.element(QName.get("explicit", this.iTunesNamespace));
        if (explicitElement == null) {
            return this.explicit = ExplicitLevel.UNKNOWN;
        }

        String explicitText = explicitElement.getTextTrim();
        if ("yes".equalsIgnoreCase(explicitText)) {
            return this.explicit = ExplicitLevel.EXPLICIT;
        }

        if ("clean".equalsIgnoreCase(explicitText)) {
            return this.explicit = ExplicitLevel.CLEAN;
        }

        return this.explicit = ExplicitLevel.UNKNOWN;
    }

    public URL getImage() throws MalformedURLException {
        String imageString = this.getImageString();

        if (imageString == null) {
            return null;
        }

        return this.image = new URL(imageString);
    }

    public String getImageString() {
        if (this.imageString != null) {
            return this.imageString;
        }

        Element imageElement = this.parent.element(QName.get("image", this.iTunesNamespace));
        if (imageElement == null) {
            return null;
        }

        return this.imageString = imageElement.attributeValue("href");
    }
}
