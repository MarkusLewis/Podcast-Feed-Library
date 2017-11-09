package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class ITunesOwner {

    private final Element ownerElement;
    private final Namespace iTunesNamespace;
    private String name, email;

    public ITunesOwner(Element ownerElement) {
        this.ownerElement = ownerElement;
        this.iTunesNamespace = this.ownerElement.getNamespaceForPrefix("itunes");
    }

    public String getName() {
        if (this.name != null) {
            return this.name;
        }

        Element nameElement = this.ownerElement.element(QName.get("name", this.iTunesNamespace));
        if (nameElement == null) {
            return null;
        }

        return this.name = nameElement.getText();
    }

    public String getEmail() {
        if (this.email != null) {
            return this.email;
        }

        Element emailElement = this.ownerElement.element(QName.get("email", this.iTunesNamespace));
        if (emailElement == null) {
            return null;
        }

        return this.email = emailElement.getText();
    }
}
