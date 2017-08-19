package cn.cherish.blog;

import cn.cherish.blog.service.ArticleService;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogApplicationTests {

	@Autowired
	private ArticleService articleService;
    RestClient restClient;

    @Before
    public void init() throws IOException {
        restClient = RestClient.builder(new HttpHost("39.108.67.111", 9200, "http")).build();
    }

    @Test
	public void contextLoads() {

        articleService.findAll().forEach(article -> {
            HttpEntity entity = new NStringEntity(JSON.toJSONString(article), ContentType.APPLICATION_JSON);
            try {
                restClient.performRequest(
                        "POST",
                        "/cherish_blog/blog/" + article.getId(),
                        Collections.emptyMap(),
                        entity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
	}

	@After
    public void destroy() throws IOException {
        restClient.close();
    }

}
