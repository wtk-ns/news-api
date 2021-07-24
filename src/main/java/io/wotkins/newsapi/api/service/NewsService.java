package io.wotkins.newsapi.api.service;

import io.wotkins.newsapi.api.entities.NewsEntity;
import io.wotkins.newsapi.api.exceprions.InvalidParseGapException;
import io.wotkins.newsapi.api.exceprions.InvalidResourceException;
import io.wotkins.newsapi.api.repository.NewsRepository;
import io.wotkins.newsapi.api.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;


    public List<NewsEntity> getNewsForGap(Integer gap, String resource) throws Exception{

        if (gap > Constants.getMaxPossibleTimeGap() || gap<=0){
            throw new InvalidParseGapException("Временной промежуток может быть от 1 до " +
                    Constants.getMaxPossibleTimeGap() + " часов");
        }
        Calendar lastOkDate = new GregorianCalendar(Constants.getTimeZone());
        lastOkDate.add(GregorianCalendar.HOUR_OF_DAY, -gap);

        List<NewsEntity> newsEntities = newsRepository.findAllByPublisheddateAfterAndResource(
                lastOkDate.getTime(), resource.toUpperCase());

        if(newsEntities.isEmpty()){
            throw new InvalidResourceException("Ресурса " + resource.toUpperCase() + " не существует " +
                    "или нет новостей за временной промежуток " + gap + " час/часов");
        }

        newsEntities.sort(new Comparator<NewsEntity>() {
            @Override
            public int compare(NewsEntity o1, NewsEntity o2) {

                if(o1.getPublisheddate().after(o2.getPublisheddate())){
                    return -1;
                } else if (o2.getPublisheddate().after(o1.getPublisheddate()))
                {
                    return 1;
                } else return 0;
            }
        });

        return newsEntities;

    }

}
