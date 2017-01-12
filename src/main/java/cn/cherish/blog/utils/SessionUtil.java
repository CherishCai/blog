package cn.cherish.blog.utils;


import cn.cherish.blog.entity.WeixinUser;

import javax.servlet.http.HttpSession;

/**
 * @author Cherish
 */
public class SessionUtil {
	
	private static String WWEXINUSER = "WeixinUser";

	public static WeixinUser getWeixinUser(HttpSession session) {
		return (WeixinUser) session.getAttribute(WWEXINUSER);
	}

	public static void addWeixinUser(HttpSession session, WeixinUser weixinUser) {
		session.setAttribute(WWEXINUSER, weixinUser);
	}


	public static Object get(HttpSession session, String attrName) {
		return session.getAttribute(attrName);
	}

	public static void add(HttpSession session, String attrName, Object val) {
		session.setAttribute(attrName, val);
	}
	
}
