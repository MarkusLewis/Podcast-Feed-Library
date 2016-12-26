package com.icosillion.podengine.models;

import org.dom4j.Attribute;
import org.dom4j.Element;

public class CloudInfo {

    private final Element cloudElement;

    //Caching
    private String domain, path, registerProcedure, protocol;
    private Integer port;

    public CloudInfo(Element cloudElement) {
        this.cloudElement = cloudElement;
    }

    public String getDomain() {
        if (this.domain != null) {
            return this.domain;
        }

        Attribute domainAttribute = cloudElement.attribute("domain");
        if (domainAttribute == null) {
            return null;
        }

        return this.domain = domainAttribute.getValue();
    }

    public Integer getPort() {
        if (this.port != null) {
            return this.port;
        }

        Attribute portAttribute = cloudElement.attribute("port");
        if (portAttribute == null) {
            return null;
        }

        try {
            this.port = Integer.valueOf(portAttribute.getValue());
        } catch (NumberFormatException e) {
            //TODO Should this return an exception?
            return null;
        }

        return this.port;
    }

    public String getPath() {
        if (this.path != null) {
            return this.path;
        }

        Attribute pathAttribute = cloudElement.attribute("path");
        if (pathAttribute == null) {
            return null;
        }

        return this.path = pathAttribute.getValue();
    }

    public String getRegisterProcedure() {
        if (this.registerProcedure != null) {
            return this.registerProcedure;
        }

        Attribute registerProcedureAttribute = cloudElement.attribute("registerProcedure");
        if (registerProcedureAttribute == null) {
            return null;
        }

        return this.registerProcedure = registerProcedureAttribute.getValue();
    }

    public String getProtocol() {
        if (this.protocol != null) {
            return this.protocol;
        }

        Attribute protocolAttribute = cloudElement.attribute("protocol");
        if (protocolAttribute == null) {
            return null;
        }

        return this.protocol = protocolAttribute.getValue();
    }
}
