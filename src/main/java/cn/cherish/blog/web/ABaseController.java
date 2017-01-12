package cn.cherish.blog.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建人：Cherish
 * 联系方式：18826137274/785427346@qq.com 
 * @author Cherish
 * @date 2016年8月17日 下午6:51:31
 * @version 1.0
 */
public class ABaseController {

	protected static final int PAGE_SIZE = 20;
	protected static final int SUCCESS_CODE = 200;
	protected static final int FORBIDDEN_CODE = 403;
	protected static final int ERROR_CODE = 500;
	protected static final int NOT_FOUND_CODE = 404;
	protected static final int NOT_LOGIN_CODE = 100;

	protected Map<String, Object> getReturnMap(int code, String message, Object data) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("code", code);
		map.put("message", message);
		map.put("data", data);
		return map;
	}
	
	protected Map<String, Object> getReturnMap(boolean success, String message, Object data) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("success", success);
		map.put("message", message);
		map.put("data", data);
		return map;
	}
}
