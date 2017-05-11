package cn.cherish.blog.dal.repository;

import cn.cherish.blog.dal.entity.Autoresponse;

/**
 * Created by Cherish on 2017/1/10.
 */
public interface AutoresponseDAO extends IBaseDAO<Autoresponse,Long> {

    Autoresponse findByKeyword(String keyword);

}
