# Crawler-for-IMDB-Movie-based-on-IDs-from-MovieLens
It is a crawler built for fetching movie information from IMDB movie pages. The fetching process need a initial data from MovieLens since there are ID information for movies on IMDB.

## Structure
The project is based on the Java web crawler built by HTTPClient library. For each fetching request, it pass on the Movie ID of IMDB from MovieLens dataset and construct the fetching url with the ID. After got the HTML page information of Movie from IMDB, I use Regular Expression to match certain values that I need (for example, director, producer, actor, actress...) to save into the database. I use MySQL as my Database.
