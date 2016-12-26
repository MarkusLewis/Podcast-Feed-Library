package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.QName;

public class ITunesOwner {

    private final Element ownerElement;

    private String name, email;

    public ITunesOwner(Element ownerElement) {
        this.ownerElement = ownerElement;
    }

    public String getName() {
        if (this.name != null) {
            return this.name;
        }

        Element nameElement = this.ownerElement.element(QName.get("name", "itunes"));
        if (nameElement == null) {
            return null;
        }

        return this.name = nameElement.getText();
    }

    public String getEmail() {
        if (this.email != null) {
            return this.email;
        }

        Element emailElement = this.ownerElement.element(QName.get("email", "itunes"));
        if (emailElement == null) {
            return null;
        }

        return this.email = emailElement.getText();
    }
}
