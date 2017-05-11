package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Contact;
import cn.cherish.blog.dal.repository.ContactDAO;
import cn.cherish.blog.dal.repository.IBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContactService extends ABaseService<Contact,Long> {

    @Autowired
    private ContactDAO contactDao;

    @Override
    protected IBaseDAO<Contact, Long> getEntityDAO() {
        return contactDao;
    }




}
