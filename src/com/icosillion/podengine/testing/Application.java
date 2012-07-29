package com.icosillion.podengine.testing;

import java.util.List;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.DocumentException;

import com.icosillion.podengine.exceptions.MalformedFeedException;
import com.icosillion.podengine.models.Episode;
import com.icosillion.podengine.models.Podcast;

public class Application {

	public Application(String[] args) throws MalformedURLException, IOException, DocumentException, MalformedFeedException {
		System.out.println("Loading podcast...");
		long startTime = System.currentTimeMillis();
		Podcast podcast = new Podcast(new URL("http://tested.com/podcast-xml"));
		System.out.println("Loaded in " + ((System.currentTimeMillis() - startTime) / 1000f) + "s");
		System.out.println("Title - " + podcast.getTitle());
		System.out.println("Description - " + podcast.getDescription());
		System.out.println("Link - " + podcast.getLink());
		System.out.println("Language - " + podcast.getLanguage());
		System.out.println("Copyright - " + podcast.getCopyright());
		System.out.println("Managing Editor - " + podcast.getManagingEditor());
		System.out.println("Webmaster - " + podcast.getWebMaster());
		System.out.println("Get Publication Date - " + podcast.getPubDate());
		System.out.println("Get Last Build Date - " + podcast.getLastBuildDate());
		System.out.println("Categories :-");
		for(String category : podcast.getCategories())
			System.out.println("\t- " + category);
		System.out.println("Generator - " + podcast.getGenerator());
		System.out.println("Docs - " + podcast.getDocs());
		System.out.println("TTL - " + podcast.getTTL());
		System.out.println("Image URL - " + podcast.getImageURL());
		System.out.println("Explicit? - " + podcast.isExplicit());
		System.out.print("Keywords - ");
		for(String kw : podcast.getKeywords()) {
			System.out.print(kw + ", ");
		}
		System.out.println();
		List<Episode> episodes = podcast.getEpisodes();
		System.out.println("Episodes(" + episodes.size() + ") :-");
		for(Episode episode : episodes) {
			System.out.println("\tTitle - " + episode.getTitle());
			System.out.println("\tLink - " + episode.getLink());
			System.out.println("\tDescription - " + episode.getDescription());
			System.out.println("\tEncoded Content - " + episode.getEncodedContent());
			System.out.println("\tAuthor - " + episode.getAuthor());
			System.out.println("\tCategories :-");
			for(String category : episode.getCategories()) {
				System.out.println("\t\t- " + category);
			}
			System.out.println("\tComments - " + episode.getComments());
			System.out.println("\tMedia Location - " + episode.getMediaLocation());
			System.out.println("\tMedia Length - " + episode.getMediaLength());
			System.out.println("\tMedia Type - " + episode.getMediaType());
			System.out.println("\tGUID - " + episode.getGuid());
			System.out.println("\tPub Date - " + episode.getPubDate());
			System.out.println("\tSource Name - " + episode.getSourceName());
			System.out.println("\tSource Link - " + episode.getSourceLink());
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException, DocumentException, MalformedFeedException {
		new Application(args);
	}

}
