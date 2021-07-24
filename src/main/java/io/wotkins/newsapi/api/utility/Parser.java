package io.wotkins.newsapi.api.utility;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import io.wotkins.newsapi.api.entities.NewsEntity;
import io.wotkins.newsapi.api.exceprions.InvalidResourceException;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
public class Parser {

    public List<NewsEntity> parseNewsEntities(String FEED, int ammountOfHoursBefore) throws Exception{
        List<SyndEntry> syndParsed = parse(FEED,ammountOfHoursBefore);

        List<NewsEntity> listOfNews = new ArrayList<>();

        System.out.println("Парсим " + FEED + " найдено " + syndParsed.size() + " synd Entry");

        for (SyndEntry news : syndParsed){
            NewsEntity entity = new NewsEntity();
            entity.setLink(news.getLink());
            entity.setPublisheddate(news.getPublishedDate());
            entity.setResource(getResourceFromRss(FEED));
            entity.setTitle(news.getTitle());
            listOfNews.add(entity);
        }

        return listOfNews;
    }

    private String getResourceFromRss(String FEED) throws Exception{

        if (FEED.contains("vc")){
            return "VC";
        } else if (FEED.contains("journal.tinkoff")) {
            return "TJ";
        } else if (FEED.contains("kod.ru")){
            return "KOD";
        } else {
            throw new InvalidResourceException("Ресурс не найден парсером");
        }

    }

    public List<SyndEntry> parse(String FEED, int amountOfHoursBefore) throws Exception{

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(Constants.getTimeZone());
        calendar.add(Calendar.HOUR_OF_DAY, -amountOfHoursBefore);

        SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(FEED)));

        List<SyndEntry> list = new ArrayList<>();
        for (SyndEntry entry : syndFeed.getEntries())
        {
            if (entry.getPublishedDate().after(calendar.getTime())) {
                shortLink(entry);
                list.add(entry);
            }
        }

        return sort(list);
    }

    private void shortLink(SyndEntry entry){
        String temp = entry.getLink();

        if (temp.contains("vc.ru"))
        {
            entry.setLink(temp.substring(0, temp.lastIndexOf("/")+7));
        } else if (temp.contains("journal.tinkoff.ru"))
        {
            entry.setLink(temp.substring(0, temp.lastIndexOf("/")+1));
        }
    }

    private List<SyndEntry> sort(List<SyndEntry> list){


        Comparator<SyndEntry> comparator = (o1, o2) -> {
            if (o1.getPublishedDate().after(o2.getPublishedDate()))
            {
                return -1;
            } else if (o1.getPublishedDate().before(o2.getPublishedDate())) {
                return 1;
            } else return 0;

        };
        list.sort(comparator);



        return  list;
    }


}
