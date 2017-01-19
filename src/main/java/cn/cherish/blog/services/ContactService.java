package cn.cherish.blog.services;

import cn.cherish.blog.entity.Contact;
import cn.cherish.blog.repository.ContactDao;
import cn.cherish.blog.repository.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
