package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class PodcastOverviewTest {

    private Podcast podcast;

    @Before
    public void setup() throws IOException, InvalidFeedException, MalformedFeedException {
        String source = new String(Files.readAllBytes(Paths.get("feed.rss")));
        podcast = new Podcast(source);
    }

    @Test
    public void testOverview() throws MalformedFeedException, MalformedURLException, DateFormatException {
        assertEquals("Testing Feed", podcast.getTitle());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", podcast.getDescription());
        assertEquals("https://podcast-feed-library.owl.im/feed", podcast.getLink().toString());
        assertEquals("en-GB", podcast.getLanguage());
        assertEquals("Copyright © 2017 Icosillion", podcast.getCopyright());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getManagingEditor());
        assertEquals("Marcus Lewis (marcus@icosillion.com)", podcast.getWebMaster());
        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getPubDateString());
        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getPubDate());
        assertEquals(DateUtils.stringToDate("Mon, 12 Dec 2016 15:30:00 GMT"), podcast.getLastBuildDate());
        assertEquals("Mon, 12 Dec 2016 15:30:00 GMT", podcast.getLastBuildDateString());
        assertEquals("Handcrafted", podcast.getGenerator());
        assertEquals("https://podcast-feed-library.owl.im/docs", podcast.getDocs().toString());
        assertEquals(60, (int) podcast.getTTL());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", podcast.getImageURL().toString());
        assertNull(podcast.getPICSRating());

        Set<Integer> skipHours = podcast.getSkipHours();
        assertTrue(skipHours.contains(0));
        assertTrue(skipHours.contains(4));
        assertTrue(skipHours.contains(8));
        assertTrue(skipHours.contains(12));
        assertTrue(skipHours.contains(16));

        Set<String> skipDays = podcast.getSkipDays();
        assertTrue(skipDays.contains("Monday"));
        assertTrue(skipDays.contains("Wednesday"));
        assertTrue(skipDays.contains("Friday"));

        assertEquals(1, podcast.getEpisodes().size());
    }

    @Test
    public void testCategoriesWithITunes() {
        Set<Category> categories = new HashSet<>();
        categories.add(new Category("Tech", "https://podcast-feed-library.owl.im/category/tech"));
        categories.add(new Category("Open Source"));
        Category technology = new Category("Technology");
        technology.getSubcategories().add(new Category("Tech News"));
        categories.add(technology);
        categories.add(new Category("Business"));

        assertEquals(categories, podcast.getCategories(true));
    }

    @Test
    public void testCategories() {
        Set<Category> categories = new HashSet<>();
        categories.add(new Category("Tech", "https://podcast-feed-library.owl.im/category/tech"));
        categories.add(new Category("Open Source"));

        assertEquals(categories, podcast.getCategories());
    }

    @Test
    public void testKeywords() {
        Set<String> keywords = new HashSet<>();
        keywords.add("podcast");
        keywords.add("java");
        keywords.add("xml");
        keywords.add("dom4j");
        keywords.add("icosillion");
        keywords.add("maven");

        assertEquals(keywords, podcast.getKeywords());
    }

    @Test
    public void testTextInput() throws MalformedURLException {
        TextInputInfo textInput = podcast.getTextInput();
        assertEquals("Feedback", textInput.getTitle());
        assertEquals("Feedback for the Testing Feed", textInput.getDescription());
        assertEquals("feedback", textInput.getName());
        assertEquals("https://podcast-feed-library.owl.im/feedback/submit", textInput.getLink().toString());
    }

    @Test
    public void testITunesInfo() throws Exception {
        ITunesChannelInfo iTunesInfo = podcast.getITunesInfo();
        assertEquals("Icosillion", iTunesInfo.getAuthor());
        assertEquals("A dummy podcast feed for testing the Podcast Feed Library.", iTunesInfo.getSubtitle());
        assertEquals("This podcast brings testing capabilities to the Podcast Feed Library", iTunesInfo.getSummary());
        assertEquals(false, iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.CLEAN, iTunesInfo.getExplicit());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", iTunesInfo.getImage().toString());
        assertEquals(ITunesChannelInfo.FeedType.SERIAL, iTunesInfo.getType());
    }

    @Test
    public void testITunesOwnerInfo() {
        ITunesOwner iTunesOwner = podcast.getITunesInfo().getOwner();
        assertEquals("Icosillion", iTunesOwner.getName());
        assertEquals("hello@icosillion.com", iTunesOwner.getEmail());
    }

    @Test
    public void testITunesCategories() {
        Set<Category> categories = podcast.getITunesInfo().getCategories();

        assertEquals(2, categories.size());

        for (Category category : categories) {
            switch (category.getName()) {
                case "Technology":
                    assertEquals("Technology > Tech News", category.toString());
                    assertEquals(1, category.getSubcategories().size());
                    Category subcategory = (Category) category.getSubcategories().toArray()[0];
                    assertEquals("Tech News", subcategory.getName());
                    break;
                case "Business":
                    assertEquals("Business", category.getName());
                    assertEquals("Business", category.toString());
                    assertEquals(0, category.getSubcategories().size());
                    break;
                default:
                    fail("Unexpected category " + category.getName());
                    break;
            }
        }
    }

    @Test
    public void testCloudInfo() {
        CloudInfo cloudInfo = podcast.getCloud();
        assertEquals("rpc.owl.im", cloudInfo.getDomain());
        assertEquals(8080, (int) cloudInfo.getPort());
        assertEquals("/rpc", cloudInfo.getPath());
        assertEquals("owl.register", cloudInfo.getRegisterProcedure());
        assertEquals("xml-rpc", cloudInfo.getProtocol());
    }
}
