package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.QName;

public class ITunesItemInfo extends ITunesInfo {

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

    private String duration;
    private Boolean isClosedCaptioned;
    private Integer order;

    public ITunesItemInfo(Element parent) {
        super(parent);
    }

    public String getDuration() {
        //TODO Further parse this?
        if(this.duration != null)
            return this.duration;

        Element durationElement = this.parent.element(QName.get("duration", "itunes"));
        if(durationElement == null)
            return null;

        return this.duration = durationElement.getText();
    }

    public boolean isCloseCaptioned() {
        if(this.isClosedCaptioned != null)
            return this.isClosedCaptioned;

        Element isClosedCaptionedElement = this.parent.element(QName.get("isClosedCaptioned", "itunes"));
        if(isClosedCaptionedElement == null)
            return this.isClosedCaptioned = false;

        if("yes".equalsIgnoreCase(isClosedCaptionedElement.getTextTrim()))
            return this.isClosedCaptioned = true;

        return this.isClosedCaptioned = false;
    }

    public Integer getOrder() {
        if(this.order != null)
            return this.order;

        Element orderElement = this.parent.element(QName.get("order", "itunes"));
        if(orderElement == null)
            return null;

        try {
            return this.order = Integer.parseInt(orderElement.getTextTrim());
        } catch(NumberFormatException e) {
            return null;
        }
    }
}
