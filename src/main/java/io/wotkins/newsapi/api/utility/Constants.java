package io.wotkins.newsapi.api.utility;

import org.hibernate.annotations.Synchronize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
public class Constants {
    private static final Integer parseCallPeriodInHours = 1;
    private static final List<String> arrayOfUrls = new ArrayList<>();
    private static final TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
    private static final Integer maxPossibleTimeGap = 72;
    private static final Integer parsePeriodInHours = 12;

    public Constants(){
        arrayOfUrls.add("https://vc.ru/rss");
        arrayOfUrls.add("https://journal.tinkoff.ru/feed/");
        arrayOfUrls.add("https://kod.ru/rss/");
    }

    public static Integer getParsePeriodInHours() {
        return parsePeriodInHours;
    }

    public static Integer getParseCallPeriodInHours() {
        return parseCallPeriodInHours;
    }

    public static List<String> getArrayOfUrls() {
        return arrayOfUrls;
    }

    public static TimeZone getTimeZone() {
        return timeZone;
    }

    public static Integer getMaxPossibleTimeGap() {
        return maxPossibleTimeGap;
    }
}
