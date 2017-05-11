package cn.cherish.blog.common.weixinpo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "xml")
public class NewsMessage extends BaseMessage {

    private int ArticleCount;
    private List<News> Articles;

    @XmlElement(name = "ArticleCount")
    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    @XmlElement(name = "Articles")
    public List<News> getArticles() {
        return Articles;
    }

    public void setArticles(List<News> articles) {
        Articles = articles;
    }
}
