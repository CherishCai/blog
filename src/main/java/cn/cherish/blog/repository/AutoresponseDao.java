package cn.cherish.blog.repository;

import cn.cherish.blog.entity.Autoresponse;

/**
 * Created by Cherish on 2017/1/10.
 */
public interface AutoresponseDao extends IBaseDao<Autoresponse,Long>{

    Autoresponse findByKeyword(String keyword);

}
