package com.icosillion.podengine.models;

import org.dom4j.Element;
import org.dom4j.QName;

public class ITunesItemInfo extends ITunesInfo {

    public enum EpisodeType {
        FULL, TRAILER, BONUS
    }

    private String duration;
    private Boolean isClosedCaptioned;
    private Integer order;
    private Integer seasonNumber;
    private Integer episodeNumber;
    private String title;
    private EpisodeType episodeType;

    public ITunesItemInfo(Element parent) {
        super(parent);
    }

    public String getDuration() {
        if (this.duration != null) {
            return this.duration;
        }

        Element durationElement = this.parent.element(QName.get("duration", this.iTunesNamespace));
        if (durationElement == null) {
            return null;
        }

        return this.duration = durationElement.getText();
    }

    public boolean isClosedCaptioned() {
        if (this.isClosedCaptioned != null) {
            return this.isClosedCaptioned;
        }

        Element isClosedCaptionedElement = this.parent.element(QName.get("isClosedCaptioned", this.iTunesNamespace));
        if (isClosedCaptionedElement == null) {
            return this.isClosedCaptioned = false;
        }

        if ("yes".equalsIgnoreCase(isClosedCaptionedElement.getTextTrim())) {
            return this.isClosedCaptioned = true;
        }

        return this.isClosedCaptioned = false;
    }

    public Integer getOrder() {
        if (this.order != null) {
            return this.order;
        }

        Element orderElement = this.parent.element(QName.get("order", this.iTunesNamespace));
        if (orderElement == null) {
            return null;
        }

        try {
            return this.order = Integer.parseInt(orderElement.getTextTrim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getEpisodeNumber() {
        if (this.episodeNumber != null) {
            return this.episodeNumber;
        }

        Element episodeNumberElement = this.parent.element(QName.get("episode", this.iTunesNamespace));
        if (episodeNumberElement == null) {
            return null;
        }

        String rawEpisodesNumber = episodeNumberElement.getTextTrim();
        try {
            return this.episodeNumber = Integer.parseInt(rawEpisodesNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getSeasonNumber() {
        if (this.seasonNumber != null) {
            return this.seasonNumber;
        }

        Element seasonNumberElement = this.parent.element(QName.get("season", this.iTunesNamespace));
        if (seasonNumberElement == null) {
            return null;
        }

        String rawSeasonNumber = seasonNumberElement.getTextTrim();
        try {
            return this.seasonNumber = Integer.parseInt(rawSeasonNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getTitle() {
        if (this.title != null) {
            return this.title;
        }

        Element titleElement = this.parent.element(QName.get("title", this.iTunesNamespace));
        if (titleElement == null) {
            return null;
        }

        return this.title = titleElement.getText();
    }

    public EpisodeType getEpisodeType() {
        if (this.episodeType != null) {
            return this.episodeType;
        }

        Element episodeTypeElement = this.parent.element(QName.get("episodeType", this.iTunesNamespace));
        if (episodeTypeElement == null) {
            return this.episodeType = EpisodeType.FULL;
        }

        String rawEpisodeType = episodeTypeElement.getTextTrim().toLowerCase();
        switch (rawEpisodeType) {
            case "bonus":
                this.episodeType = EpisodeType.BONUS;
                break;
            case "trailer":
                this.episodeType = EpisodeType.TRAILER;
                break;
            case "full":
                this.episodeType = EpisodeType.FULL;
                break;
            default:
                this.episodeType = EpisodeType.FULL;
                break;
        }

        return this.episodeType;
    }
}
