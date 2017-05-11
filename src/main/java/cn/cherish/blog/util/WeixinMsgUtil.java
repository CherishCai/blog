package cn.cherish.blog.util;

import cn.cherish.blog.common.weixin4j.WeixinConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Map;

/**
 * 微信工具类
 * @author Cherish
 *
 */
public class WeixinMsgUtil {
			//蔡梦缘wx267713c547d9779a    //测试号wx5aa597cdb526288d
			//6afc5502c25f8c9262d6ed7eb3dd0c98	//d4624c36b6795d1d99dcf0547af5443d
	private static final String APPID = WeixinConfig.getValue("appid");
	private static final String APPSECRET = WeixinConfig.getValue("secret");

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	

	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.parseObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.parseObject(result);
		return jsonObject;
	}
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.parseObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}

	/**
	 * 百度翻译
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static String baduiTranslate(String source) throws IOException{
		String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?"+
					"q=WORD&from=auto&to=auto&appid=APPID&salt=SALT&sign=SIGN";
		
		String appid = "20160116000009013";
		String word = URLEncoder.encode(source, "UTF-8");
		String passwd = "ClglmaCpBrvM_8HCOPMl";
		int salt = (int) ( Math.random()*1000 );
		String sign = MD5Util.md5(appid+source+salt+passwd);
		
		url = url.replace("WORD", word)//
				.replace("APPID", appid)//
				.replace("SALT", salt+"")//随机数int
				.replace("SIGN", sign);//签字appid+q+salt+passwd
		
		JSONObject jsonObject = doGetStr(url);
		StringBuffer dst = new StringBuffer();
		System.out.println(jsonObject.toString());
		List<Map> list = (List<Map>) jsonObject.get("trans_result");
		for(Map map : list){
			dst.append(map.get("dst"));
		}
		return dst.toString();
	}

	/**
	 * 完整翻译
	 * @param source
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String translateFull(String source) throws ParseException, IOException{
		String url = "http://openapi.baidu.com/public/2.0/bmt/translate" +
				"?client_id=jNg0LPSBe691Il0CG5MwDupw&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source, "UTF-8"));
		JSONObject jsonObject = doGetStr(url);
		StringBuffer dst = new StringBuffer();
		System.out.println(jsonObject.toString());
		List<Map> list = (List<Map>) jsonObject.get("trans_result");
		for(Map map : list){
			dst.append(map.get("dst"));
		}
		return dst.toString();
	}


    public static String queryWeather(String city) {
		return WeatherUtil.queryByCity(city);
    }

	public static String queryShipper(String expNo) {

		StringBuilder sb = new StringBuilder("查询出错！请确认单号是否正确！");
		String result = null;
		try {
			result = KdApiOrderDistinguish.getOrderTracesByJson(expNo);
			if (result.indexOf("true") > 0) {
				//查到了单号
				KdApiOrderDistinguish kdApiOrderDistinguish = JSON.parseObject(result, KdApiOrderDistinguish.class);

				List<KdApiOrderDistinguish.ShippersBean> shippers = kdApiOrderDistinguish.getShippers();
				for (int i = 0; i < shippers.size(); i++) {
					KdApiOrderDistinguish.ShippersBean shipper = shippers.get(i);
					String shipperName = shipper.getShipperName();
					result = KdniaoTrackQueryAPI.getOrderTracesByJson(shipper.getShipperCode(), expNo);
					if (result.indexOf("true") > 0) {
						KdniaoTrackQueryAPI kdniaoTrackQueryAPI = JSON.parseObject(result, KdniaoTrackQueryAPI.class);
						if (kdniaoTrackQueryAPI.getState() > 0) {
							sb = new StringBuilder(shipperName + ":\r\n");
							for (KdniaoTrackQueryAPI.TracesBean traces : kdniaoTrackQueryAPI.getTraces()) {
								sb.append(traces.getAcceptTime() + traces.getAcceptStation() + ":\r\n");
							}
							sb.append("\r\n");
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			sb = new StringBuilder("查询出错！请确认单号是否正确！");
		}

		return sb.toString();
	}

    /*public static void main(String[] args) {
        queryShipper("1000745320654");
    }*/

    public static String getReturnMsg(HttpServletRequest request) throws IOException, DocumentException {

        Map<String, String> msgMap = MessageUtil.xmlToMap(request);
        String fromUserName = msgMap.get("FromUserName");//来自发送方帐号（一个OpenID）
        String toUserName = msgMap.get("ToUserName");//发给这边的公众号
        String msgType = msgMap.get("MsgType");
        String content = msgMap.get("Content");

        String message = null;
        if(MessageUtil.MESSAGE_TEXT.equals(msgType)){//文字信息
			/* 核心是解剖对方发过来的文字信息   作出相应的回答  */

            if("1".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.WEATHER);
            }else if(content.startsWith("天气")){
                String city = content.replaceAll("^天气", "").trim();
                if("".equals(city)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.WEATHER);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.queryWeather(city));
                }
            }else if("2".equals(content)){
                message = MessageUtil.initNewsMessage(toUserName, fromUserName);
            }else if("3".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.TRANSLATRE);
            }else if(content.startsWith("翻译")){//^翻译 指以"翻译"开头的字符串
                String word = content.replaceAll("^翻译", "").trim();
                if("".equals(word)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.TRANSLATRE);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.baduiTranslate(word));
                }
            }else if("4".equals(content) || "图片".equals(content)){
                message = MessageUtil.initImageMessage(toUserName, fromUserName);
            }else if("5".equals(content) || "音乐".equals(content)){
                message = MessageUtil.initMusicMessage(toUserName, fromUserName);
            }else if("6".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.EXPRESSAGE);
            }else if(content.startsWith("快递")){
                String expNo = content.replaceAll("^快递", "").trim();
                if("".equals(expNo)){
                    message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.EXPRESSAGE);
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.queryShipper(expNo));
                }
                //看我博客的朋友喔，推送链接
            }else if("7".equals(content) || "博客".equals(content) || "文章".equals(content)){
                message = MessageUtil.initBlogMessage(toUserName, fromUserName);
            }else if("help".equals(content) || "?".equals(content) || "？".equals(content)){
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.HELP);
            }else{
                message = MessageUtil.initText(toUserName, fromUserName, "你输入了-->" + content +"\n请输入help查看菜单");
            }

            //事件信息
        }else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
            String eventType = msgMap.get("Event");
            if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){//关注
                message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.SUBSCRIBE);
            }else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){//点击
                message = MessageUtil.initText(toUserName, fromUserName, "点击了啥？");
            }else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
                String url = msgMap.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, url);
            }else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
                String key = msgMap.get("EventKey");
                message = MessageUtil.initText(toUserName, fromUserName, key);
            }

        }else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){//位置
            String label = msgMap.get("Label");
            message = MessageUtil.initText(toUserName, fromUserName, label);

        }else if(MessageUtil.MESSAGE_IMAGE.equals(msgType)){//图片
            message = MessageUtil.initText(toUserName, fromUserName, "美图/::B");

        }else if(MessageUtil.MESSAGE_VOICE.equals(msgType) || MessageUtil.MESSAGE_MUSIC.equals(msgType)){
            message = MessageUtil.initText(toUserName, fromUserName, "好声音/::$");

        }else if(MessageUtil.MESSAGE_SHORTVIDEO.equals(msgType) || MessageUtil.MESSAGE_VIDEO.equals(msgType)){
            message = MessageUtil.initText(toUserName, fromUserName, "还我快播/::~");

        }else if(MessageUtil.MESSAGE_LINK.equals(msgType)){//链接
            message = MessageUtil.initText(toUserName, fromUserName, "http://www.caihongwen.cn/");

            //什么奇葩情况？
        }else {
            message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.HELP);
        }

        return message;
    }

    public static String askTuLing(String me, String openid, String info){
        String tulingJson = TulingUtil.askWithUserId(info, openid.substring(0,10).toLowerCase());
        System.out.println("tulingJson = " + tulingJson);

        if (tulingJson.indexOf("100000") > 0){//文本类
            TulingUtil.TextBean textBean = JSON.parseObject(tulingJson, TulingUtil.TextBean.class);
            return MessageUtil.initText(me, openid, textBean.getText());
        }else if (tulingJson.indexOf("200000") > 0) {//链接类 （图片、列车、航班）
            TulingUtil.LinkBean linkBean = JSON.parseObject(tulingJson, TulingUtil.LinkBean.class);
            return MessageUtil.initImgTextMessage(me, openid, linkBean.getText(), linkBean.getUrl());
        }else if (tulingJson.indexOf("302000") > 0) {//新闻类
            TulingUtil.NewsBean newsBean = JSON.parseObject(tulingJson, TulingUtil.NewsBean.class);
            return MessageUtil.initTuLingNewsMessage(me, openid, newsBean.getList());
        }else if (tulingJson.indexOf("308000") > 0) {//菜谱类
            TulingUtil.CookbookBean cookbookBean = JSON.parseObject(tulingJson, TulingUtil.CookbookBean.class);
            return MessageUtil.initTuLingCookMessage(me, openid, cookbookBean.getList());
        }else{//出错了咯？
            return MessageUtil.initText(me, openid, "对不起，数据出错！");
        }
    }

}
