package cn.cherish.blog.dal.repository;

import cn.cherish.blog.dal.entity.WxUser;

public interface WxUserDAO extends IBaseDAO<WxUser,Long> {

    WxUser findByOpenid(String openid);

}
