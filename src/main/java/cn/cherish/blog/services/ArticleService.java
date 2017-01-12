package cn.cherish.blog.services;

import cn.cherish.blog.entity.Article;
import cn.cherish.blog.repository.ArticleDao;
import cn.cherish.blog.repository.IBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Cherish on 2017/1/10.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ArticleService extends ABaseService<Article, Long> {

    @Autowired
    private ArticleDao articleDao;

    @Override
    protected IBaseDao<Article, Long> getEntityDAO() {
        return articleDao;
    }

    @Transactional
    public void updateArticle(Article newA) {
        Article old = articleDao.findOne(newA.getId());
        old.setTitle(newA.getTitle());
        old.setReadSum(newA.getReadSum());
        old.setContent(newA.getContent());
        old.setMdcontent(newA.getMdcontent());

        articleDao.save(old);
    }

    @Transactional
    public void saveArticle(Article article) {
        article.setCreatetime(new Date());
        articleDao.save(article);
    }


}
