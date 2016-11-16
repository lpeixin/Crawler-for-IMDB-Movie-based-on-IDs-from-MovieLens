package fetching;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liu.peixin on 2016-11-14.
 */
@SpringBootApplication
public class Crawler implements CommandLineRunner {


    private static final Logger log = LoggerFactory.getLogger(Crawler.class);

    private static CloseableHttpClient httpClient;

    private static Pattern patternRating = Pattern.compile("<span\\sitemprop=\"contentRating\">([^\\s]*)</span>(\\s*)<span\\sclass=\"ghost\">");

    private static Pattern patternDirector = Pattern.compile("<span\\sitemprop=\"director\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
            "<a href=\"(.*)\"(\\s*)" +
            "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>");

    private static Pattern patternWriter = Pattern.compile("<span\\sitemprop=\"creator\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
            "<a href=\"(.*)\"(\\s*)" +
            "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>");

    private static Pattern patternStars12 = Pattern.compile(
            "<div class=\"credit_summary_item\">(\\s*)<h4 class=\"inline\">Stars:</h4>(\\s*)<span\\sitemprop=\"actors\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
                    "<a href=\"(.*)\"(\\s*)" +
                    "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>,(\\s*)</span>(\\s*)"+
                    "<span\\sitemprop=\"actors\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
                    "<a href=\"(.*)\"(\\s*)" +
                    "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>,(\\s*)</span>(\\s*)");

    private static Pattern patternStars = Pattern.compile(
            "<span\\sitemprop=\"actors\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
                    "<a href=\"(.*)\"(\\s*)" +
                    "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>,(\\s*)</span>(\\s*)"+
                    "<span\\sitemprop=\"actors\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
                    "<a href=\"(.*)\"(\\s*)" +
                    "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>,(\\s*)</span>(\\s*)"+
                    "<span\\sitemprop=\"actors\"\\sitemscope\\sitemtype=\"http://schema.org/Person\">(\\s*)" +
                    "<a href=\"(.*)\"(\\s*)" +
                    "itemprop='url'><span\\sclass=\"itemprop\"\\sitemprop=\"name\">(.*)</span></a>(\\s*)</span>");

    private static Pattern patternCountry = Pattern.compile("<h4 class=\"inline\">Release Date:</h4>([^(]*)\\((\\w*)\\)");

    private static Pattern patternGenre = Pattern.compile("<span class=\"itemprop\" itemprop=\"genre\">(\\w*)</span></a>");


    @Override
    public void run(String... strings) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = strings[0];
        log.info("Start fetching from " + url);
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpget);
        String html = null;
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            long len = entity.getContentLength();
            if (len != -1 && len < 2048) {
                html = EntityUtils.toString(entity);
            }
        } else {
            log.error("Got error when fetching from" + url);
        }

        if (html.indexOf("PG") != -1) {
            System.out.println(html.substring(html.indexOf("PG"), html.indexOf("PG") + 300));

        }

        Matcher matcherRating = patternRating.matcher(html);
        Matcher matcherDirector = patternDirector.matcher(html);
        Matcher matcherWriter = patternWriter.matcher(html);
        Matcher matcherStars = patternStars.matcher(html);
        Matcher matcherCountry = patternCountry.matcher(html);
        Matcher matcherGenre = patternGenre.matcher(html);

        if (matcherRating.find()) {
            String res = matcherRating.group(1);
            if (StringUtils.isNotBlank(res)) {
                System.out.println("Rating: " + res);
            }
        }

        if (matcherDirector.find()) {
            String res = matcherDirector.group(4);
            if (StringUtils.isNotBlank(res)) {
                System.out.println("Director: " + res);
            }
        }

        if (matcherWriter.find()) {
            String res = matcherWriter.group(4);
            if (StringUtils.isNotBlank(res)) {
                System.out.println("Writer: " + res);
            }
        }

        if (matcherStars.find()) {
            String star1 = matcherStars.group(4);
            String star2 = matcherStars.group(10);
            String star3 = matcherStars.group(16);
            if (StringUtils.isNotBlank(star1)) {
                System.out.print("Actors: " + star1);
            }
            if (StringUtils.isNotBlank(star2)) {
                System.out.print(", " + star2);
            }
            if (StringUtils.isNotBlank(star3)) {
                System.out.println(", " + star3);
            }
        }

        if (matcherCountry.find()) {
            String res = matcherCountry.group(2);
            if (StringUtils.isNotBlank(res)) {
                System.out.println("Country: " + res);
            }
        }

        if (matcherGenre.find()) {
            String res = matcherGenre.group(1);

            if (StringUtils.isNotBlank(res)) {
                System.out.println("Genre: " + res);
            }
        }

        response.close();
    }
}
