package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Article;
import cn.cherish.blog.dal.repository.ArticleDAO;
import cn.cherish.blog.dal.repository.IBaseDAO;
import com.google.common.base.Throwables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ArticleService extends ABaseService<Article, Long> {

    private final ElasticBlogService elasticBlogService;
    private final ArticleDAO articleDao;

    @Autowired
    public ArticleService(ElasticBlogService elasticBlogService, ArticleDAO articleDao) {
        this.elasticBlogService = elasticBlogService;
        this.articleDao = articleDao;
    }

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

        Article update = articleDao.save(old);
        try {
            elasticBlogService.updateArticle(update);
        } catch (IOException e) {
            log.error("{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Transactional
    public void saveArticle(Article article) {
        article.setCreatedTime(new Date());
        Article save = articleDao.save(article);
        try {
            elasticBlogService.saveArticle(save);
        } catch (IOException e) {
            log.error("{}", Throwables.getStackTraceAsString(e));
        }
    }

    @Transactional
    public void delete(Long id) {
        getEntityDAO().delete(id);
        try {
            elasticBlogService.delArticle(id);
        } catch (IOException e) {
            log.error("{}", Throwables.getStackTraceAsString(e));
        }
    }

    public Page<Article> search(String search, Integer pageNumber, Integer size) {
        Objects.requireNonNull(pageNumber);
        Objects.requireNonNull(size);
        try {
            return elasticBlogService.searchArticle(search, new PageRequest(pageNumber - 1, size));
        } catch (IOException e) {
            log.error("{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }
}
