package cn.cherish.blog.common.weixinpo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class TextMessage extends BaseMessage{

	private String Content;
	private String MsgId;

	@XmlElement(name = "Content")
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}

	@XmlElement(name = "MsgId")
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
