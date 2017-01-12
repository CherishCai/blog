package cn.cherish.blog.services;

import cn.cherish.blog.entity.Contact;
import cn.cherish.blog.repository.ContactDao;
import cn.cherish.blog.repository.IBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Cherish on 2017/1/11.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ContactService extends ABaseService<Contact,Long> {

    @Autowired
    private ContactDao contactDao;

    @Override
    protected IBaseDao<Contact, Long> getEntityDAO() {
        return contactDao;
    }




}
