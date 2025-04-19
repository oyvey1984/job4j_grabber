package ru.job4j.grabber.service;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final Logger LOG = Logger.getLogger(HabrCareerParse.class);
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PREFIX = "/vacancies?page=";
    private static final String SUFFIX = "&q=Java%20developer&type=all";
    private static final int PAGES = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> fetch() {
        var result = new ArrayList<Post>();
        for (int i = 0; i < PAGES; i++) {
            try {
                String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, i, SUFFIX);
                var connection = Jsoup.connect(fullLink);
                var document = connection.get();
                var rows = document.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    var titleElement = row.select(".vacancy-card__title").first();
                    var linkElement = titleElement.child(0);
                    var dateElement = row.select(".vacancy-card__date").first();
                    var timeTag = dateElement.select("time").first();
                    String vacancyName = titleElement.text();
                    String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                    String datetime = timeTag.attr("datetime");
                    String descriptionText = retrieveDescription(link);
                    var localDateTime = dateTimeParser.parse(datetime);
                    long timeMillis = Timestamp.valueOf(localDateTime).getTime();
                    System.out.printf("%s %s %s %s%n", vacancyName, link, localDateTime, descriptionText);
                    var post = new Post();
                    post.setTitle(vacancyName);
                    post.setLink(link);
                    post.setTime(timeMillis);
                    post.setDescription(descriptionText);
                    result.add(post);
                });
            } catch (IOException e) {
                LOG.error("When load page", e);
            }
        }
        return result;
    }

    private String retrieveDescription(String link) {
        try {
            var connectionToLink = Jsoup.connect(link);
            var documentLink = connectionToLink.get();
            var descriptionElement = documentLink.select(".vacancy-description__text");
            return descriptionElement.text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}