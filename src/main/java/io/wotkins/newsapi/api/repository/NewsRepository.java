package io.wotkins.newsapi.api.repository;

import io.wotkins.newsapi.api.entities.NewsEntity;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
public interface NewsRepository extends CrudRepository<NewsEntity, String> {
    List<NewsEntity> findAllByPublisheddateAfterAndResource(Date publisheddate, String resource);
    List<NewsEntity> removeAllByPublisheddateBefore(Date lastPossibleDate);
}
