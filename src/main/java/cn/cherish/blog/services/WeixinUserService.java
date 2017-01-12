/**
 * caihongwen.cn Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package cn.cherish.blog.services;

import cn.cherish.blog.entity.WeixinUser;
import cn.cherish.blog.repository.WeixinUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Cherish
 * @version Id: UserService.java, v 0.1 2016/10/30 21:40 Cherish Exp $$
 */
@Service
@Transactional(readOnly = true)
public class WeixinUserService {

    @Autowired
    private WeixinUserDao weixinUserDao;

    public void insert(WeixinUser weixinUser) {
        weixinUserDao.save(weixinUser);
    }

    public WeixinUser findByOpenid(String openid) {
        return openid == null ? null : weixinUserDao.findByOpenid(openid);
    }


}