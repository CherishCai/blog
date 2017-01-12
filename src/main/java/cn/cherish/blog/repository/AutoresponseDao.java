package cn.cherish.blog.repository;

import cn.cherish.blog.entity.Autoresponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by Cherish on 2017/1/10.
 */
@CacheConfig(cacheNames = "autoresponse")
public interface AutoresponseDao extends IBaseDao<Autoresponse,Long>{

    @Cacheable
    Autoresponse findByKeyword(String keyword);



}
