# PodEngine 2.2 – Podcast Feed Library

## Java library for parsing your podcast feeds 🚀
* Written in Java 7 🤖
* Thoroughly tested 🕹️
* Parses iTunes-specific tags 🎵
* Handles all RSS attributes 💪
* MIT Licensed (Use it for all your commercial things!) 🤑

## Installation 📦
### Gradle
```groovy
repositories {
    maven {
        url 'https://maven.icosillion.com/artifactory/open-source/'
    }
}

dependencies {
    compile 'com.icosillion.podengine:podengine:2.2'
}
```

### Maven
```xml
<repositories>
    <repository>
        <id>icosillion</id>
        <name>Icosillion Repository</name>
        <url>https://maven.icosillion.com/artifactory/open-source/</url>
    </repoistory>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.icosillion.podengine</groupId>
        <artifactId>podengine</artifactId>
        <version>2.2</version>
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

