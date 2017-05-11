package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Article;
import cn.cherish.blog.dal.repository.ArticleDAO;
import cn.cherish.blog.dal.repository.IBaseDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "articles")
public class ArticleService extends ABaseService<Article, Long> {

    @Autowired
    private ArticleDAO articleDao;

    @Override
    protected IBaseDAO<Article, Long> getEntityDAO() {
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
