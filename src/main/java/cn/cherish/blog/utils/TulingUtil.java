package cn.cherish.blog.utils;

import com.alibaba.fastjson.JSONObject;
import com.sun.tools.javac.util.Assert;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.util.List;

/**
 * 图灵机器人工具
 * Created by Cherish on 2017/1/13.
 */
public class TulingUtil {
        //图灵网站上的secret
       private static final String secret = "8399a2937f8954b6";
        //图灵网站上的apiKey
        private static final String apiKey = "4c50196c856c4384827e3edc2d958c8c";

    /*public static void main(String[] args) {
        String info = "鱼香肉丝怎么做";//测试用例
        String userid = "18826137274";
        //待加密的json数据
//        String data = "{\"key\":\""+apiKey+"\",\"info\":\""+cmd+"\"}";
//        String data = "{\"key\":\""+apiKey+"\",\"info\":\""+info+"\",\"userid\":\""+userid+"\"}";
//        System.out.println(askTuling(data));
        System.out.println(askWithUserId(info,userid));;
    }*/

    public static String askWithUserId(String info, String userid){
        Assert.checkNonNull(info, "info不可为空！");
        Assert.checkNonNull(userid, "userid不可为空！");
        return askTuling("{\"key\":\""+apiKey+"\",\"info\":\""+info+"\",\"userid\":\""+userid+"\"}");
    }

    public static String askJustInfo(String info){
        Assert.checkNonNull(info, "info不可为空！");
        return askTuling("{\"key\":\"" + apiKey + "\",\"info\":\"" + info + "\"}");
    }

    public static String askTuling(String data) {

        //获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());

        //生成密钥
        String keyParam = secret+timestamp+apiKey;
        String aesKey = MD5(keyParam);

        //加密
        data = new Aes(aesKey).encrypt(data);

        //封装请求参数
        JSONObject json = new JSONObject();
        json.put("key", apiKey);
        json.put("timestamp", timestamp);
        json.put("data", data);

        //请求图灵api
        return SendPost(json.toString(), "http://www.tuling123.com/openapi/api");
    }


    /**
     * 向后台发送post请求
     * @param param
     * @param url
     * @return
     */
    public static String SendPost(String param, String url) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "token");
            conn.setRequestProperty("tag", "htc_new");

            conn.connect();

            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(param);

            out.flush();
            out.close();
            //
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * MD5加密算法
     * 说明：32位加密算法
     * @param s 待加密的数据
     * @return 加密结果，全小写的字符串
     */
    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = s.getBytes("utf-8");
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 文本类
     */
    public static class TextBean {
        /**
         * code : 100000
         * text : 就我怎么了，不够好看？
         */

        private int code;
        private String text;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * 链接类
     */
    public static class LinkBean {
        /**
         * code : 200000
         * text : 亲，已帮你找到图片
         * url : http://m.image.so.com/i?q=%E5%B0%8F%E7%8B%97
         */

        private int code;
        private String text;
        private String url;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * 新闻类
     */
    public static class NewsBean {
        /**
         * code : 302000
         * text : 亲，已帮您找到相关新闻
         * list : [{"article":"台媒:蔡英文到访\u201c友邦\u201d被索要援助 点头答应","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/5uLQ-fxzqnkq8972409.jpg/w160h120l1t15ea.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip0914573.d.html?vt=4&pos=3"},{"article":"冥王星大气层出现不明物体 震惊了UFO研究者","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/public_column/transform/20170113/4cJE-fxzqnkq9041588.jpg/w160h120l1t1350.jpg","detailurl":"http://fun.sina.cn/?vt=4&pos=3"},{"article":"郑州高架桥事故原因：旧桥拆除时支架坍塌","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/KIeV-fxzqnim4137054.jpg/w160h120l1t101e.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnva3397020.d.html?vt=4&pos=3"},{"article":"中东部地区10省市将遭重度霾 春运受影响","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/4drY-fxzqxha9480024.jpg/w160h120l1t16ba.jpg","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqhka2909548.d.html?vt=4&pos=3"},{"article":"北京交通委：停车须付费 以后将无免费停车","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/video/transform/20170113/FtWJ-fxzqxha9501306.jpg/w160h120l1t1db6.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnva3467278.d.html?vt=4&pos=3"},{"article":"湖南郴州致35死大巴起火事故续:21人被立案侦查","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/Y8UD-fxzqnip0997126.jpg/w160h120l1t10a2.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip0997365.d.html?vt=4&pos=3"},{"article":"云南15人开\u201c公馆\u201d组织卖淫 非法获利800余万","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/Kecr-fxzqnip0989285.JPG/w160h120l1t1218.jpg","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqhka2914706.d.html?vt=4&pos=3"},{"article":"日本防卫省：中国3艘军舰曾绕行日本列岛一周","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/2BuG-fxzqxha9469926.jpg/w160h120l1t1c17.jpg","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqhka2901963.d.html?vt=4&pos=3"},{"article":"陕西榆林神木县发生3.0级地震 震源深度0千米","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/U3_H-fxzqxha9538425.jpg/w160h120l1t189d.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1053676.d.html?vt=4&pos=3"},{"article":"消息称中方附条件归还新加坡装甲车 外交部回应","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/5AKo-fxzqnip1050204.jpg/w160h120l1t1774.jpg","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqnva3500000.d.html?vt=4&pos=3"},{"article":"对这90多人来说,特朗普干再久也只是白宫过客","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/UMMH-fxzqxha9520629.jpg/w160h120l1t123e.jpg","detailurl":"http://news.sina.cn/gj/2017-01-13/detail-ifxzqnip1050127.d.html?vt=4&pos=3"},{"article":"河南郸城宁平镇政府向村民借3万 已过16年未还","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/Ekmn-fxzqxha9534969.jpg/w160h120l1t16ed.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4282717.d.html?vt=4&pos=3"},{"article":"2017年第1个网络用语走红：我可能复习了假书","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/9l_0-fxzqxha9531914.jpg/w160h120l1t140c.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4279169.d.html?vt=4&pos=3"},{"article":"国台办回应\u201c台驻尼机构\u201d被摘牌:一中不容置疑","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/-_6n-fxzqnim4276092.jpg/w160h120l1t1188.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4277077.d.html?vt=4&pos=3"},{"article":"李鸿忠:肃清黄兴国政治作风人事的影响任务繁重","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/ba3H-fxzqxha9521865.jpg/w160h120l1t1c48.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1035713.d.html?vt=4&pos=3"},{"article":"视界观(1月13日)：听过六个鸡火车站吗","source":"新浪新闻","icon":"","detailurl":"http://photo.sina.cn/album_1_83076_107102.htm?vt=4&pos=3&ch=1"},{"article":"这个火车站叫\u201c鸡鸡鸡鸡鸡鸡\u201d 员工送鸡年祝福","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/PWaI-fxzqnip1029033.jpg/w160h120l1t132b.jpg","detailurl":"http://news.sina.cn/sh/2017-01-13/detail-ifxzqnip1029443.d.html?vt=4&pos=3"},{"article":"俞正声出席4任国家主席子女到场 只为纪念此人","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/o9XQ-fxzqnip1028192.jpg/w160h120l1t1c8e.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4262589.d.html?vt=4&pos=3"},{"article":"特朗普还没上任 他的内阁部长们已开始\u201c吃苦\u201d","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/AdN8-fxzqnip1023031.png/w160h120l1t1f87.jpg","detailurl":"http://news.sina.cn/gj/2017-01-13/detail-ifxzqnip1025189.d.html?vt=4&pos=3"},{"article":"中国奥委会发文谴责3名兴奋剂违规运动员","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/K94Q-fxzqxha9514041.jpg/w160h120l1t1485.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1023336.d.html?vt=4&pos=3"},{"article":"陕西回应贩婴案3年后才发认领公告：与事实不符","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170111/dUxI-fxzkfuk3757092.jpg/w160h120l1t14bd.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4255518.d.html?vt=4&pos=3"},{"article":"央行确定：正式集中存管第三方支付备付金","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/KWjw-fxzqnip1020491.jpg/w160h120l1t1d5a.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1021021.d.html?vt=4&pos=3"},{"article":"考试太难家长要找市长投诉 出题老师躲办公室哭","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/lL4S-fxzqnkq9053072.jpg/w160h120l1t1724.jpg","detailurl":"http://news.sina.cn/sh/2017-01-13/detail-ifxzqnim4251345.d.html?vt=4&pos=3"},{"article":"美国FBI局长因希拉里邮件门处理不当遭司法审查","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/yo1--fxzqxha9504546.jpg/w160h120l1t10c8.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4247107.d.html?vt=4&pos=3"},{"article":"女教师网恋\u201c帅哥\u201d被骗8万 对方实为学生妈妈","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/MXjJ-fxzqnip1008143.jpg/w160h120l1t10f8.jpg","detailurl":"http://news.sina.cn/sh/2017-01-13/detail-ifxzqnip1009611.d.html?vt=4&pos=3"},{"article":"父亲给儿子买火柴枪被控非法持枪 检方重新鉴定","source":"新浪新闻","icon":"https://ks.sinaimg.cn/ww3/mw690/c875cd12gw1fbp1ytik0mj20cx07eaaq.jpg/w160h120l1t1528.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1006721.d.html?vt=4&pos=3"},{"article":"高铁15元盒饭不再不断供 铁总：还有更便宜的","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/translate/20170113/dPd4-fxzqnkq9041992.jpg/w160h120l1t1a71.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnva3478604.d.html?vt=4&pos=3"},{"article":"党媒调查中小学生营养餐 称人均浪费二两粮食","source":"新浪新闻","icon":"","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip1000934.d.html?vt=4&pos=3"},{"article":"火车站治安岗亭被指形似棺材 警方称3天内撤除","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/transform/20170113/FHBh-fxzqnkq9033031.jpg/w160h120l1t1b9d.jpg","detailurl":"http://news.sina.cn/sh/2017-01-13/detail-ifxzqnim4225757.d.html?vt=4&pos=3"},{"article":"宁夏党委副书记崔波任自治区政协副主席(图)","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/X0SW-fxzqxha9484869.jpg/w160h120l1t1087.jpg","detailurl":"http://news.sina.cn/gn/2017-01-13/detail-ifxzqnim4222122.d.html?vt=4&pos=3"},{"article":"澳大利亚卫生和体育部长因公费旅游丑闻辞职","source":"新浪新闻","icon":"","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqnva3468402.d.html?vt=4&pos=3"},{"article":"发改委等5部门：持伪造过期车票将记入失信记录","source":"新浪新闻","icon":"","detailurl":"http://news.sina.cn/2017-01-13/detail-ifxzqnva3467937.d.html?vt=4&pos=3"},{"article":"小偷持刀挟持女主人 丈夫逃出房子关上防盗门","source":"新浪新闻","icon":"https://ks.sinaimg.cn/n/news/crawl/20170113/kzV_-fxzqxha9479833.jpg/w160h120l1t1110.jpg","detailurl":"http://news.sina.cn/sh/2017-01-13/detail-ifxzqnim4216016.d.html?vt=4&pos=3"}]
         */

        private int code;
        private String text;
        private List<ListBean> list;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * article : 台媒:蔡英文到访“友邦”被索要援助 点头答应
             * source : 新浪新闻
             * icon : https://ks.sinaimg.cn/n/news/crawl/20170113/5uLQ-fxzqnkq8972409.jpg/w160h120l1t15ea.jpg
             * detailurl : http://news.sina.cn/gn/2017-01-13/detail-ifxzqnip0914573.d.html?vt=4&pos=3
             */

            private String article;
            private String source;
            private String icon;
            private String detailurl;

            public String getArticle() {
                return article;
            }

            public void setArticle(String article) {
                this.article = article;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }
        }
    }


    /**
     * 菜谱类
     */
    public static class CookbookBean {
        /**
         * code : 308000
         * text : 亲，已帮您找到菜谱信息
         * list : [{"name":"鱼香肉丝","icon":"","info":"瘦肉、黑木耳、胡萝卜、靑椒、豆瓣酱，葱姜蒜、白糖，香醋，料酒、酱油，盐，、水淀粉，植物油","detailurl":"http://m.xiachufang.com/recipe/100352761/?ref=tuling"},{"name":"史上最详尽经典川菜【鱼香肉丝】","icon":"","info":"【主料】猪肉丝、青笋、木耳、盐一勺、清水2勺、【配料】葱抹、姜末、蒜末、剁椒酱、郫县豆、干淀粉一勺，清水2勺、【腌肉料】：食用油2勺、料酒3勺、白胡椒粉一勺、生抽一勺、老抽1/2勺、干淀粉一勺、【鱼香汁】：醋4勺、白糖3勺、、生抽2勺、味精一勺、","detailurl":"http://m.xiachufang.com/recipe/1102796/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪腿肉、泡椒、姜、蒜、葱、水发木耳、胡萝卜、白糖、香醋(保宁醋/镇江香醋)、老抽、生抽、盐、清汤、淀粉","detailurl":"http://m.xiachufang.com/recipe/1010097/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"里脊肉、青红辣椒、红萝卜、干木耳、郫县豆瓣酱、蒜瓣、姜蓉、泡椒、葱白、盐、糖、香醋、酱油、料酒、油、淀粉、清水","detailurl":"http://m.xiachufang.com/recipe/261379/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"瘦肉、木耳，青椒，胡萝卜、葱，姜，蒜、郫县豆瓣酱、料酒，糖，醋，盐，鸡精，淀粉，老抽，酱油，水、鸡蛋清","detailurl":"http://m.xiachufang.com/recipe/100489447/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪瘦肉、莴笋丝、水发木耳、葱花、姜米、蒜米、泡红辣椒、#鱼香汁、酱油、醋、白糖、盐、水淀粉、肉汤、料酒、#、盐、水淀粉、料酒、混合油","detailurl":"http://m.xiachufang.com/recipe/100218709/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪里脊肉、绿尖椒、胡萝卜、冬笋、黑木耳、生抽、料酒、水淀粉、醋、白糖、盐、色拉油、葱姜蒜、四川泡辣椒","detailurl":"http://m.xiachufang.com/recipe/17762/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"瘦猪肉、黑木耳、胡萝卜、春笋、葱、姜、蒜瓣、辣酱、生抽、老抽、醋、料酒、白糖、盐、香油、生粉、清水","detailurl":"http://m.xiachufang.com/recipe/1002652/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪里脊肉、冬笋、胡萝卜、黑木耳、葱末、姜末、蒜末、剁椒、水淀粉（一汤匙干淀粉加3汤匙清水调匀）、料酒、醋、生抽、白糖、香麻油、水","detailurl":"http://m.xiachufang.com/recipe/263164/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪里脊、水发玉兰片、水发木耳、胡萝卜、葱、蒜、姜、泡红辣椒、盐、白糖、醋、酱油、湿淀粉","detailurl":"http://m.xiachufang.com/recipe/1000066/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪肉、青椒、胡萝卜、黑木耳、笋片、小葱、蒜、小红辣椒、姜、花椒、郫县红油豆瓣酱、红油辣椒、盐、鸡精、生抽、糖、醋、料酒、水淀粉","detailurl":"http://m.xiachufang.com/recipe/44870/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪肉、香菇、木耳、红萝卜、# 鱼香肉丝调料、黄酒、玉米淀粉、盐","detailurl":"http://m.xiachufang.com/recipe/264781/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪瘦肉、胡萝卜、青椒、木耳、花生油、盐、酱油、料酒、白糖、醋、湿淀粉、葱、姜、蒜、郫县红油豆瓣酱","detailurl":"http://m.xiachufang.com/recipe/140066/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"瘦肉丝、红萝卜、蒜薹、黑木耳、蒜头、干红辣椒、姜、豆瓣酱、花椒油、白砂糖、盐、老陈醋、淀粉、水淀粉","detailurl":"http://m.xiachufang.com/recipe/100553600/?ref=tuling"},{"name":"鱼香肉丝","icon":"","info":"猪里脊、笋、水发木耳、水发香菇、彩椒、葱、姜、蒜、干辣椒、糖、醋、老抽、生抽、盐、淀粉、鸡汤、虾油、香油、料酒、芝麻","detailurl":"http://m.xiachufang.com/recipe/1044857/?ref=tuling"}]
         */

        private int code;
        private String text;
        private List<ListBean> list;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * name : 鱼香肉丝
             * icon :
             * info : 瘦肉、黑木耳、胡萝卜、靑椒、豆瓣酱，葱姜蒜、白糖，香醋，料酒、酱油，盐，、水淀粉，植物油
             * detailurl : http://m.xiachufang.com/recipe/100352761/?ref=tuling
             */

            private String name;
            private String icon;
            private String info;
            private String detailurl;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }

            public String getDetailurl() {
                return detailurl;
            }

            public void setDetailurl(String detailurl) {
                this.detailurl = detailurl;
            }
        }
    }
}

class Aes {

    private Key key;
    /**
     * AES CBC模式使用的Initialization Vector
     */
    private IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    /**
     * Cipher 物件
     */
    private Cipher cipher;

    /**
     * 构造方法
     * @param strKey  密钥
     */
    public Aes(String strKey) {
        try {
            this.key = new SecretKeySpec(getHash("MD5", strKey), "AES");
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加密方法
     *
     * 说明：采用128位
     *
     * @return 加密结果
     */
    public String encrypt(String strContent) {
        try {
            byte[] data = strContent.getBytes("UTF-8");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptData = cipher.doFinal(data);
            return new String(Base64.encodeBase64(encryptData), "UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     *
     * @param algorithm
     * @param text
     * @return
     */
    private static byte[] getHash(String algorithm, String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytes);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


}

