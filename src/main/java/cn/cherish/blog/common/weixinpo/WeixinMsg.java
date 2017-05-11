/**
 * godhealth.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package cn.cherish.blog.common.weixinpo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Cherish
 * @version Id: WeixinMsg.java, v 0.1 2016/11/7 17:01 Cherish Exp $$
 */
@XmlRootElement(name = "xml")
public class WeixinMsg {

    //发送方微信号
    @XmlElement(name = "FromUserName")
    private String FromUserName;
    //接收方微信号
    @XmlElement(name = "ToUserName")
    private String ToUserName;
    //消息内容
    @XmlElement(name = "Content")
    private String Content;
    //消息类型
    @XmlElement(name = "MsgType")
    private String MsgType;

    @XmlElement(name = "Event")
    private String Event;

    @XmlElement(name = "EventKey")
    private String EventKey;

    @XmlElement(name = "Label")
    private String Label;

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }


}