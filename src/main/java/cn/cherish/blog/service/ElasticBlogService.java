package cn.cherish.blog.service;

import cn.cherish.blog.dal.entity.Article;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Cherish
 * @version 1.0
 * @date 2017/8/17 10:07
 */
@Slf4j
@Component
public class ElasticBlogService {

    private static final String BLOG_URI = "/cherish_blog/blog/";
    private RestClient restClient;
    private static final Map<String, String> EMPTY_PARAMS = new HashMap<>();

    @PostConstruct
    void init(){
        restClient = RestClient.builder(
                new HttpHost("39.108.67.111", 9200, "http")).build();
    }

    @PreDestroy
    void destroy(){
        if (restClient != null) {
            try {
                restClient.close();
            } catch (IOException e) {
                log.error("[ElasticService] {}", Throwables.getStackTraceAsString(e));
            }
        }
    }

    public boolean saveArticle(Article article) throws IOException {
        HttpEntity entity = new NStringEntity(JSON.toJSONString(article), ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "POST",
                BLOG_URI + article.getId(),
                Collections.emptyMap(),
                entity);
        int statusCode = response.getStatusLine().getStatusCode();
        if (200 == statusCode){
            return true;
        }
        return false;
    }

    public void delArticle(Long articleId) throws IOException {
        restClient.performRequest("DELETE", BLOG_URI + articleId);
    }

    public boolean updateArticle(Article article) throws IOException {
        String jsonString = JSON.toJSONString(article);
        jsonString = "{\"doc\":" + jsonString + "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "POST",
                BLOG_URI + article.getId() + "/_update",
                Collections.emptyMap(),
                entity);
        int statusCode = response.getStatusLine().getStatusCode();
        if (200 == statusCode){
            return true;
        }
        return false;
    }

    public Page<Article> searchArticle(String search, Pageable pageable) throws IOException {
        Objects.requireNonNull(search);
        Objects.requireNonNull(pageable);
        String s =
                "{\"from\":"+pageable.getOffset()+"," +
                    "\"size\":"+pageable.getPageSize()+"," +
                    "\"query\":{" +
                        "\"multi_match\":{" +
                            "\"query\":\""+search.trim()+"\"," +
                            "\"fields\":[\"title\",\"intro\",\"mqcontent\"]" +
                        "}" +
                    "}," +
                    "\"highlight\":{" +
                        "\"pre_tags\":\"<em style='color:red;'>\"," +
                        "\"post_tags\":\"</em>\"," +
                        "\"fields\":{" +
                            "\"title\":{},\"intro\":{}" +
                        "}" +
                    "}" +
                "}";
        log.info("s = " + s);
        HttpEntity entity = new NStringEntity(s, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "GET",
                BLOG_URI + "_search",
                Collections.emptyMap(),
                entity);

        log.debug("【response】 : {}", response);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if (200 == statusCode){
            HttpEntity httpEntity = response.getEntity();
            String entityStr = EntityUtils.toString(httpEntity);
            log.debug("【httpEntity】 : {}", entityStr);

            JSONObject jsonObject = JSON.parseObject(entityStr);
            log.debug("【jsonObject】 : {}", jsonObject);

            String hits = jsonObject.getString("hits");
            log.debug("hits : {}", hits);

            JSONObject jsonObject1 = JSON.parseObject(hits);
            Integer total = jsonObject1.getInteger("total");
            String hitsArr = jsonObject1.getString("hits");

            List<String> list = JSON.parseArray(hitsArr, String.class);

            List<Article> articles = list.stream().map(str -> {
                log.debug("str : {}", str);
                JSONObject jsonObject2 = JSON.parseObject(str);
                Article article = jsonObject2.getObject("_source", Article.class);
                log.debug("article : {}", article);
                String highlight = jsonObject2.getString("highlight");
                JSONObject jsonObject3 = JSON.parseObject(highlight);
                String title = jsonObject3.getString("title");
                String intro = jsonObject3.getString("intro");

                if (intro != null) {
                    log.debug("【highlight】intro : {}", intro);
                    article.setIntro(intro.substring(2, intro.length() - 1));
                }
                if (title != null) {
                    log.debug("【highlight】title : {}", title);
                    article.setTitle(title.substring(2, title.length() - 1));
                }

                return article;
            }).collect(Collectors.toList());

            return new PageImpl<>(articles, pageable, total);
        }else {
            return null;
        }

    }



}
