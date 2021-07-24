package io.wotkins.newsapi.api.service;
import io.wotkins.newsapi.api.entities.NewsEntity;
import io.wotkins.newsapi.api.repository.NewsRepository;
import io.wotkins.newsapi.api.utility.Constants;
import io.wotkins.newsapi.api.utility.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class NewsUpdateThreadService implements Runnable{

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private Parser parser;
    private final List<String> listOfUrls = Constants.getArrayOfUrls();

    @PostConstruct
    private void preInit(){
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        while (true){
            System.out.println("Thread running " + this.hashCode());
            for (String url : listOfUrls){
                try {
                    List<NewsEntity> entities = parser.parseNewsEntities(url, Constants.getParsePeriodInHours());
                    for (NewsEntity entity : entities){
                        newsRepository.save(entity);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            if(isNow(2)){
                Calendar lastPossibleDate = new GregorianCalendar();
                lastPossibleDate.add(GregorianCalendar.HOUR_OF_DAY, -Constants.getMaxPossibleTimeGap());
                List<NewsEntity> removed = newsRepository.removeAllByPublisheddateBefore(lastPossibleDate.getTime());
                System.out.println("Removed - " + removed.size() + " news");
            }

            try {
                Thread.sleep(Constants.getParseCallPeriodInHours() * 3600000); //3600000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean isNow(int hour){
        return (LocalTime.now(Constants.getTimeZone().toZoneId()).getHour() == hour);
    }


}
