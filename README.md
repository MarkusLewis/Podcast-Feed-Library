# Java Podcast Feed Parser
_Simple library to read podcast feeds_

__Version__ 2.0.0-SNAPSHOT

## Usage
__Read Feed__
	
	Podcast podcast = new Podcast(new URL("FEED URL"));
	System.out.println("Title - " + podcast.getTitle());

__Get Episodes__

	Podcast podcast = new Podcast(new URL("FEED URL"));
	List<Episode> episodes = podcast.getEpisodes();
	for(Episode episode : episodes) {
		System.out.println("Episode Title - " + episode.getTitle());
	}

## Dependencies
[dom4j](http://dom4j.sourceforge.net/) _XML Parsing Library_
[Apache Commons IO](https://commons.apache.org/proper/commons-io/) _IO Utilities_

## License (MIT)
	Copyright (c) 2012 - 2015 Icosillion

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
