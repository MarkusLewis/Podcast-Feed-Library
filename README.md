# PodEngine 2.4.1 – Podcast Feed Library

## Java library for parsing your podcast feeds 🚀
* Written in Java 7 🤖
* Thoroughly tested 🕹️
* Parses iTunes-specific tags 🎵
* Handles all RSS attributes 💪
* MIT Licensed (Use it for all your commercial things!) 🤑

## Installation 📦
### Gradle
```groovy
dependencies {
    compile 'com.icosillion.podengine:podengine:2.4.1'
}
```

### Maven
```xml
<dependencies>
    <dependency>
        <groupId>com.icosillion.podengine</groupId>
        <artifactId>podengine</artifactId>
        <version>2.4.1</version>
    </dependency>
</dependencies>
```

## Getting Started 🌱
### Reading your feed
```java
//Download and parse the Cortex RSS feed
Podcast podcast = new Podcast(new URL("https://www.relay.fm/cortex/feed"));

//Display Feed Details
System.out.printf("💼 %s has %d episodes!\n", podcast.getTitle(), podcast.getEpisodes().size());

//List all episodes
for (Episode episode : episodes) {
	System.out.println("- " + episode.getTitle());
}
```

