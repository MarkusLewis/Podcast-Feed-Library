package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Podcast unit test class.
 */
public class PodcastTest {

    @Test
    public void testPodcastFeedCreation(){
        try {
            Podcast podcast = new Podcast(new URL("https://www.relay.fm/cortex/feed"));
            System.out.println(podcast.getTitle());
            System.out.println(podcast.getDescription());
        } catch (InvalidFeedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (MalformedFeedException e) {
            e.printStackTrace();
        }
    }

}
