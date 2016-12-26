package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Podcast unit test class.
 */
public class PodcastTest {

    @Test
    public void testPodcastFeed() {
        try {
            Podcast podcast = new Podcast(new URL("http://feeds.feedburner.com/thetimferrissshow"));
            assertEquals("The Tim Ferriss Show", podcast.getTitle());
        } catch (InvalidFeedException | MalformedURLException | MalformedFeedException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testRelayFeed() {
        try {
            Podcast podcast = new Podcast(new URL("https://www.relay.fm/master/feed"));
            assertEquals("Relay FM Master Feed", podcast.getTitle());
        } catch (InvalidFeedException | MalformedURLException | MalformedFeedException e) {
            fail(e.getMessage());
        }
    }

}
