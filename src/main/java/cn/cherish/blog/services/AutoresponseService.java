package cn.cherish.blog.services;

import cn.cherish.blog.entity.Autoresponse;
import cn.cherish.blog.repository.AutoresponseDao;
import cn.cherish.blog.repository.IBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class AutoresponseService extends  ABaseService<Autoresponse, Long>{

	@Autowired
	private AutoresponseDao autoresponseDao;

	@Override
	protected IBaseDao getEntityDAO() {
		return autoresponseDao;
	}

	public Autoresponse findByKeyword(String keyword) {
		return keyword == null ? null : autoresponseDao.findByKeyword(keyword);
	}



    @Transactional
    public void saveAutoresponse(Autoresponse newResponse) {
        newResponse.setCreatetime(new Date());
        autoresponseDao.save(newResponse);
    }

    @Transactional
	public void updateAutoresponse(Autoresponse newResponse) {
        Autoresponse old = autoresponseDao.findOne(newResponse.getId());

        old.setKeyword(newResponse.getKeyword());
        old.setMessage(newResponse.getMessage());
        old.setMsgType(newResponse.getMsgType());

        autoresponseDao.save(old);
	}


}
