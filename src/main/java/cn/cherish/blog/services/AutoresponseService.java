package cn.cherish.blog.services;

import cn.cherish.blog.entity.Autoresponse;
import cn.cherish.blog.repository.AutoresponseDao;
import cn.cherish.blog.repository.IBaseDao;
import cn.cherish.blog.utils.MessageUtil;
import cn.cherish.blog.utils.WeixinMsgUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AutoresponseService extends  ABaseService<Autoresponse, Long>{

	@Autowired
	private AutoresponseDao autoresponseDao;

	@Override
	protected IBaseDao getEntityDAO() {
		return autoresponseDao;
	}

	public Autoresponse findByKeyword(String keyword) {
		return keyword == null ? null : autoresponseDao.findByKeyword(keyword);
	}

	public String getReturnMsg(HttpServletRequest request) throws IOException, DocumentException {

		Map<String, String> msgMap = MessageUtil.xmlToMap(request);
		String fromUserName = msgMap.get("FromUserName");//来自用户
		String toUserName = msgMap.get("ToUserName");//发给这边的公众号
		String msgType = msgMap.get("MsgType");
		String content = msgMap.get("Content");

		String message = null;
		if(MessageUtil.MESSAGE_TEXT.equals(msgType)){//文字信息
			/* 核心是解剖对方发过来的文字信息   作出相应的回答  */

			if("1".equals(content)){
				message = MessageUtil.initText(toUserName, fromUserName, "天气查询（例如：天气广州，城市名称，国内城市支持中英文，国际城市支持英文）");
			}else if(content.startsWith("天气")){
                String city = content.replaceAll("^天气", "").trim();
                if("".equals(city)){
                    message = MessageUtil.initText(toUserName, fromUserName, "天气查询（例如：天气广州，城市名称，国内城市支持中英文，国际城市支持英文）");
                }else{
                    message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.queryWeather(city));
                }
			}else if("2".equals(content)){
				message = MessageUtil.initNewsMessage(toUserName, fromUserName);
			}else if("3".equals(content)){
                Autoresponse translate = autoresponseDao.findByKeyword("translate");
                message = MessageUtil.initText(toUserName, fromUserName, translate != null ?translate.getMessage():"对不起！翻译功能未完善！");
			}else if("4".equals(content)){
				message = MessageUtil.initImageMessage(toUserName, fromUserName);
			}else if("5".equals(content)){
				message = MessageUtil.initMusicMessage(toUserName, fromUserName);
			}else if("?".equals(content) || "？".equals(content) || "help".equals(content)){
                Autoresponse help = autoresponseDao.findByKeyword("help");
                message = MessageUtil.initText(toUserName, fromUserName, help != null ?help.getMessage():"有什么可以帮到您的呢！");
			}else if(content.startsWith("翻译")){//^翻译 指以"翻译"开头的字符串
				String word = content.replaceAll("^翻译", "").trim();
				if("".equals(word)){
                    Autoresponse translate = autoresponseDao.findByKeyword("translate");
					message = MessageUtil.initText(toUserName, fromUserName, translate != null ?translate.getMessage():"对不起！翻译功能未完善！");
				}else{
					message = MessageUtil.initText(toUserName, fromUserName, WeixinMsgUtil.baduiTranslate(word));
				}
			}else if("图片".equals(content)){
				message = MessageUtil.initImageMessage(toUserName, fromUserName);
			}else if("音乐".equals(content)){
				message = MessageUtil.initMusicMessage(toUserName, fromUserName);
			}else{

                Autoresponse byKeyword = autoresponseDao.findByKeyword(content);
				if (byKeyword != null) {
					String returnMsg = byKeyword.getMessage();
					String returnMsgType = byKeyword.getMsgType();
					message = MessageUtil.initText(toUserName, fromUserName, returnMsg);
				}else{
                    Autoresponse help = autoresponseDao.findByKeyword("help");
                    message = MessageUtil.initText(toUserName, fromUserName, help != null ?help.getMessage():"你输入了----》" + content);
				}
			}

		}else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){//事件信息
			String eventType = msgMap.get("Event");
			if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){//关注
                Autoresponse subscribe = autoresponseDao.findByKeyword("subscribe");
                message = MessageUtil.initText(toUserName, fromUserName, subscribe != null ?subscribe.getMessage():"欢迎关注！");
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

		}

		return message;
	}

    @Transactional
    public void saveAutoresponse(Autoresponse newResponse) {
        newResponse.setCreatetime(new Date());
        autoresponseDao.save(newResponse);
    }

    @Transactional
	public void updateAutoresponse(Autoresponse newResponse) {
        Autoresponse old = autoresponseDao.findOne(newResponse.getId());

        old.setKeyword(newResponse.getKeyword());
        old.setMessage(newResponse.getMessage());
        old.setMsgType(newResponse.getMsgType());

        autoresponseDao.save(old);
	}


}
