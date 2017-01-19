package cn.cherish.blog.repository;

import cn.cherish.blog.entity.WeixinUser;

public interface WeixinUserDao extends IBaseDao<WeixinUser,Long>{

    WeixinUser findByOpenid(String openid);

}
