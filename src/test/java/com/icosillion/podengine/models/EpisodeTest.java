package com.icosillion.podengine.models;

import com.icosillion.podengine.exceptions.DateFormatException;
import com.icosillion.podengine.exceptions.InvalidFeedException;
import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EpisodeTest {

    private Podcast podcast;

    @Before
    public void setup() throws IOException, InvalidFeedException, MalformedFeedException {
        String source = new String(Files.readAllBytes(Paths.get("feed.rss")));
        podcast = new Podcast(source);
    }

    @Test
    public void testEpisode() throws MalformedFeedException, MalformedURLException, DateFormatException {
        List<Episode> episodes = podcast.getEpisodes();
        Episode episode = episodes.get(0);

        assertEquals("Episode 1: Are you not getting bored yet?", episode.getTitle());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", episode.getDescription());
        assertEquals("https://podcast-feed-library.owl.im/episodes/1", episode.getLink().toString());
        assertEquals("Icosillion", episode.getAuthor());
        Set<String> categories = episode.getCategories();
        assertTrue(categories.contains("Technology"));
        assertTrue(categories.contains("Testing"));
        assertEquals("https://podcast-feed-library.owl.im/episodes/1/comments", episode.getComments().toString());
        assertEquals("https://podcast-feed-library.owl.im/episodes/1", episode.getGUID());
        assertEquals(DateUtils.stringToDate("Mon, 28 Nov 2016 13:30:00 GMT"), episode.getPubDate());
        assertEquals("Master Feed", episode.getSourceName());
        assertEquals("http://podcast-feed-library.owl.im/feed.rss", episode.getSourceURL().toString());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.\n" +
                "                The show notes live in this section, but we have nothing else interesting to say.\n" +
                "            ", episode.getContentEncoded());

        //Enclosure
        Episode.Enclosure enclosure = episode.getEnclosure();
        assertEquals("https://podcast-feed-library.owl.im/audio/episode-1.mp3", enclosure.getURL().toString());
        assertEquals(1234000L, (long) enclosure.getLength());
        assertEquals("audio/mp3", enclosure.getType());

        //iTunes Info
        ITunesItemInfo iTunesInfo = episode.getITunesInfo();
        assertEquals("Icosillion", iTunesInfo.getAuthor());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", iTunesInfo.getSubtitle());
        assertEquals("Our hosts start getting bored of running a testing podcast feed. There's probably some moaning about Apple too. This is a technology podcast after all.", iTunesInfo.getSummary());
        assertFalse(iTunesInfo.isBlocked());
        assertEquals(ITunesInfo.ExplicitLevel.CLEAN, iTunesInfo.getExplicit());
        assertEquals("https://podcast-feed-library.owl.im/images/artwork.png", iTunesInfo.getImage().toString());
        assertEquals("12:34", iTunesInfo.getDuration());
        assertFalse(iTunesInfo.isClosedCaptioned());
        assertEquals(1, (int) iTunesInfo.getOrder());
        assertEquals(1, (int) iTunesInfo.getSeasonNumber());
        assertEquals(1, (int) iTunesInfo.getEpisodeNumber());

    }

    @Test
    public void testEpisodeWithExplicitNo()
    {
        List<Episode> episodes = podcast.getEpisodes();
        Episode episode = episodes.get(1);
        ITunesItemInfo iTunesInfo = episode.getITunesInfo();
        assertEquals(ITunesInfo.ExplicitLevel.NO, iTunesInfo.getExplicit());
    }
}
