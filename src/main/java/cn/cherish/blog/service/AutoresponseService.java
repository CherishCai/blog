package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Autoresponse;
import cn.cherish.blog.dal.repository.AutoresponseDAO;
import cn.cherish.blog.dal.repository.IBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "autoresponse")
public class AutoresponseService extends  ABaseService<Autoresponse, Long>{

	private final AutoresponseDAO autoresponseDao;

    @Autowired
    public AutoresponseService(AutoresponseDAO autoresponseDao) {
        this.autoresponseDao = autoresponseDao;
    }

    @Override
	protected IBaseDAO getEntityDAO() {
		return autoresponseDao;
	}

	@Cacheable(key = "#keyword", unless = "#result == null ")
	public Autoresponse findByKeyword(String keyword) {
		return keyword == null ? null : autoresponseDao.findByKeyword(keyword);
	}

    @Transactional
    public Autoresponse save(Autoresponse newResponse) {
        newResponse.setCreatedTime(new Date());
        return autoresponseDao.save(newResponse);
    }

    @Transactional
    @CacheEvict(key = "#p0.getKeyword()")
	public Autoresponse update(Autoresponse newResponse) {
        Autoresponse old = autoresponseDao.findOne(newResponse.getId());

        old.setKeyword(newResponse.getKeyword());
        old.setMessage(newResponse.getMessage());
        old.setMsgType(newResponse.getMsgType());

        return autoresponseDao.save(old);
	}


}
