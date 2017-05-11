package cn.cherish.blog.util;


import cn.cherish.blog.dal.entity.WxUser;

import javax.servlet.http.HttpSession;

/**
 * @author Cherish
 */
public class SessionUtil {
	
	private static String WWEXINUSER = "WeixinUser";

	public static WxUser getWeixinUser(HttpSession session) {
		return (WxUser) session.getAttribute(WWEXINUSER);
	}

	public static void addWeixinUser(HttpSession session, WxUser weixinUser) {
		session.setAttribute(WWEXINUSER, weixinUser);
	}


	public static Object get(HttpSession session, String attrName) {
		return session.getAttribute(attrName);
	}

	public static void add(HttpSession session, String attrName, Object val) {
		session.setAttribute(attrName, val);
	}
	
}
