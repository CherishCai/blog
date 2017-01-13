package cn.cherish.blog.utils;

import cn.cherish.blog.weixinpo.*;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * 消息封装类
 * @author caimengyuan
 *
 */
public class MessageUtil {

    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_NEWS = "news";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_MUSIC = "music";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVNET = "event";
    public static final String MESSAGE_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_CLICK = "CLICK";
    public static final String MESSAGE_VIEW = "VIEW";
    public static final String MESSAGE_SCANCODE= "scancode_push";

    public static String HELP = "菜单如下：\r\n1 天气查询\n2 开发人员介绍\n3 翻译功能\n4 图文消息\n5 音乐\n6 快递查询\n7 Cherish博客";
    public static String WEATHER = "天气查询（例如：天气广州，城市名称，国内城市支持中英文，国际城市支持英文）";
    public static String TRANSLATRE = "翻译功能的使用\r\n例如：翻译珍惜(或翻译cherish)";
    public static String EXPRESSAGE = "快递查询（输入快递单号，\r\n例如：快递418139779609）";
    public static String SUBSCRIBE = "您的关注，终于等到你，请输入help查看菜单";
    public static String TULING = "聊天功能（开启：chat，关闭：close）";

    /**
     * xml转为map集合
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{

        InputStream ins = request.getInputStream();

        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();

        Document doc = reader.read(ins);

        Element root = doc.getRootElement();

        List<Element> list = root.elements();

        for(Element e : list){
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }

    /**
     * 将文本消息对象转为xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 组装文本消息
     * @param toUserName
     * @param fromUserName
     * @param content
     * @return
     */
    public static String initText(String toUserName,String fromUserName,String content){
        TextMessage text = new TextMessage();
        text.setFromUserName(toUserName);
        text.setToUserName(fromUserName);
        text.setMsgType(MessageUtil.MESSAGE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setContent(content);
        return textMessageToXml(text);
    }

    /**
     * 主菜单
     * @return
     */
    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("欢迎您的关注，请按照菜单提示进行操作：\n\n");
        sb.append("1、公众号介绍\n");
        sb.append("2、开发人员介绍\n");
        sb.append("3、词组翻译\n\n");
        sb.append("回复？调出此菜单。");
        return sb.toString();
    }

    public static String threeMenu(){
        StringBuffer sb = new StringBuffer();
        sb.append("词组翻译使用指南\n\n");
        sb.append("使用示例：\n");
        sb.append("翻译足球\n");
        sb.append("翻译中国足球\n");
        sb.append("翻译football\n\n");
        sb.append("回复？显示主菜单。");
        return sb.toString();
    }
    /**
     * 图文消息转为xml
     * @param newsMessage
     * @return
     */
    public static String newsMessageToXml(NewsMessage newsMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new News().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 图片消息转为xml
     * @param imageMessage
     * @return
     */
    public static String imageMessageToXml(ImageMessage imageMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", imageMessage.getClass());
        return xstream.toXML(imageMessage);
    }

    /**
     * 音乐消息转为xml
     * @param musicMessage
     * @return
     */
    public static String musicMessageToXml(MusicMessage musicMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }
    /**
     * 图文消息的组装
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initImgTextMessage(String toUserName, String fromUserName, String content, String url){
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();

        News news = new News();
        news.setTitle(content);
        news.setDescription(content);
        news.setPicUrl(url);
        news.setUrl(url);

        newsList.add(news);

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        return newsMessageToXml(newsMessage);
    }

    /**
     * 图灵新闻回复
     * @param toUserName
     * @param fromUserName
     */
    public static String initTuLingNewsMessage(String toUserName, String fromUserName, List<TulingUtil.NewsBean.ListBean> list){
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();

        News news = null;
        for (int i = 0; i < list.size(); i++) {
            TulingUtil.NewsBean.ListBean tulingNews = list.get(i);

            news = new News();
            news.setTitle(tulingNews.getSource());
            news.setDescription(tulingNews.getArticle());
            news.setPicUrl(tulingNews.getIcon());
            news.setUrl(tulingNews.getDetailurl());

            newsList.add(news);
        }

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        return newsMessageToXml(newsMessage);
    }

    /**
     * 图灵菜谱回复
     * @param toUserName
     * @param fromUserName
     */
    public static String initTuLingCookMessage(String toUserName, String fromUserName, List<TulingUtil.CookbookBean.ListBean> list){
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();

        News news = null;
        for (int i = 0; i < list.size(); i++) {
            TulingUtil.CookbookBean.ListBean tulingCook = list.get(i);

            news = new News();
            news.setTitle(tulingCook.getName());
            news.setDescription(tulingCook.getInfo());
            news.setPicUrl(tulingCook.getIcon());
            news.setUrl(tulingCook.getDetailurl());

            newsList.add(news);
        }

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        return newsMessageToXml(newsMessage);
    }

    /**
     * 图文消息的组装
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initNewsMessage(String toUserName,String fromUserName){
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();

        News news = new News();
        news.setTitle("Cherish博客");
        news.setDescription("这就是Cherish博客系统，秉着公正，大公无私的热情，想大家郑重声明，这些都是在慕课网学习的，地址：\nwww.imooc.com");
        news.setPicUrl("http://www.caihongwen.cn/image/myself.jpg");
        news.setUrl("http://www.caihongwen.cn/blog/");

        newsList.add(news);

        //多图文
        News news2 = new News();
        news2.setTitle("慕课网介绍");
        news2.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。慕课网课程涵盖前端开发、PHP、Html5、Android、iOS、Swift等IT前沿技术语言，包括基础课程、实用案例、高级分享三大类型，适合不同阶段的学习人群。");
        news2.setPicUrl("http://img.mukewang.com/57a322f00001e4ae02560256-40-40.jpg");
        news2.setUrl("www.imooc.com");

        newsList.add(news2);

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        return newsMessageToXml(newsMessage);
    }

    /**
     * 博客消息的组装
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initBlogMessage(String toUserName,String fromUserName){
        List<News> newsList = new ArrayList<>();
        NewsMessage newsMessage = new NewsMessage();

        News news = new News();
        news.setTitle("Cherish博客");
        news.setDescription("蔡鸿文的个人网站-博客，这边的世界，欢迎入侵！");
        news.setPicUrl("http://www.caihongwen.cn/image/myself.jpg");
        news.setUrl("http://www.caihongwen.cn/blog/");

        newsList.add(news);

        //多图文
        News news2 = new News();
        news2.setTitle("CSDN博客");
        news2.setDescription("一名后辈，愿跟上大神的脚步\nCSDN.NET - 全球最大中文IT社区，为IT专业技术人员提供最全面的信息传播和服务平台");
        news2.setPicUrl("http://avatar.csdn.net/9/B/B/1_caimengyuan.jpg");
        news2.setUrl("http://blog.csdn.net/caimengyuan/");

        newsList.add(news2);

        newsMessage.setToUserName(fromUserName);
        newsMessage.setFromUserName(toUserName);
        newsMessage.setCreateTime(new Date().getTime());
        newsMessage.setMsgType(MESSAGE_NEWS);
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

        return newsMessageToXml(newsMessage);
    }

    /**
     * 组装图片消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initImageMessage(String toUserName,String fromUserName){
        String message = null;
        Image image = new Image();
        image.setMediaId("VwKRpsWSEWDNrBjMkipS4P_NfoYcqSjB-bGUstLfPs-Bt7LwaDx8Sz-E31zqraBz");
        ImageMessage imageMessage = new ImageMessage();
        imageMessage.setFromUserName(toUserName);
        imageMessage.setToUserName(fromUserName);
        imageMessage.setMsgType(MESSAGE_IMAGE);
        imageMessage.setCreateTime(new Date().getTime());
        imageMessage.setImage(image);
        message = imageMessageToXml(imageMessage);
        return message;
    }

    /**
     * 组装音乐消息
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public static String initMusicMessage(String toUserName,String fromUserName){
        String message = null;
        Music music = new Music();
        //music.setThumbMediaId("VwKRpsWSEWDNrBjMkipS4P_NfoYcqSjB-bGUstLfPs-Bt7LwaDx8Sz-E31zqraBz");
        music.setTitle("追梦赤子心");
        music.setDescription("谁没有梦想，你敢不敢追？");
        music.setMusicUrl("http://music.163.com/outchain/player?type=2&id=355992&auto=1&height=66");
        music.setHQMusicUrl("http://music.163.com/outchain/player?type=2&id=355992&auto=1&height=66");

        MusicMessage musicMessage = new MusicMessage();
        musicMessage.setFromUserName(toUserName);
        musicMessage.setToUserName(fromUserName);
        musicMessage.setMsgType(MESSAGE_MUSIC);
        musicMessage.setCreateTime(new Date().getTime());
        musicMessage.setMusic(music);
        message = musicMessageToXml(musicMessage);
        return message;
    }
}
