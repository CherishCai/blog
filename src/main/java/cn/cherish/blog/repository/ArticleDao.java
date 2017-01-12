package cn.cherish.blog.repository;

import cn.cherish.blog.entity.Article;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by Cherish on 2017/1/10.
 */
@CacheConfig(cacheNames = "articles")
public interface ArticleDao extends IBaseDao<Article,Long>{

    @Cacheable
    Page<Article> findAll(Specification<Article> spec, Pageable pageable);

    @Cacheable
    Article findOne(Long id);


}
