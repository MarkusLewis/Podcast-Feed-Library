package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.QName;

import java.net.MalformedURLException;
import java.net.URL;

public class ITunesChannelInfo extends ITunesInfo {

    /*
    author: String CI
    block: Bool CI
    category: String / Array C
    image: URL CI
    duration: String (HH:MM:SS various) I
    explicit: Bool("yes" / "clean") CI
    isClosedCaptioned: Bool I
    order: Int I
    complete: Bool C
    new-feed-url: URL C
    owner: Embedded Record C
    subtitle: String CI
    summary: String CI
    */

    //TODO Category
    private Boolean complete;
    private URL newFeedURL;
    private ITunesOwner owner;

    public ITunesChannelInfo(Element parent) {
        super(parent);
    }

    public boolean isComplete() {
        if(this.complete != null)
            return this.complete;

        Element completeElement = this.parent.element(QName.get("complete", "itunes"));
        if(completeElement == null)
            return this.complete = false;

        if("yes".equalsIgnoreCase(completeElement.getTextTrim()))
            return this.complete = true;

        return this.complete = false;
    }

    public URL getNewFeedURL() throws MalformedURLException {
        if(this.newFeedURL != null)
            return this.newFeedURL;

        Element newFeedURLElement = this.parent.element(QName.get("new-feed-url", "itunes"));
        if(newFeedURLElement == null)
            return null;

        return this.newFeedURL = new URL(newFeedURLElement.getTextTrim());
    }

    public ITunesOwner getOwner() {
        if(this.owner != null)
            return this.owner;

        Element ownerElement = this.parent.element(QName.get("owner", "itunes"));
        if(ownerElement == null)
            return null;

        return this.owner = new ITunesOwner(ownerElement);
    }
}
