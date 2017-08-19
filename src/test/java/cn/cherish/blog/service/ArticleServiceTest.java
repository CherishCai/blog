package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/8/17 16:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Test
    public void search() throws Exception {
        Page<Article> page = articleService.search("多姿多彩邮件Java,Mysql", 1, 10);
        System.out.println("page = " + page);
        System.out.println("getTotalPages = " + page.getTotalPages());
    }

}