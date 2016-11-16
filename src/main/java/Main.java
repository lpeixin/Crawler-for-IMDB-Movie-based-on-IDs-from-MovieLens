import fetching.Crawler;
import org.springframework.boot.SpringApplication;

/**
 * Created by liu.peixin on 2016-11-14.
 */
public class Main {

    public static void main(String args[]) {
        SpringApplication.run(Crawler.class, "http://www.imdb.com/title/tt1740299/");
    }
}
