package cn.cherish.blog.repository;

import cn.cherish.blog.entity.WeixinUser;

/**
 * Created by Cherish on 2017/1/10.
 */
public interface WeixinUserDao extends IBaseDao<WeixinUser,Long>{

    WeixinUser findByOpenid(String openid);


}
