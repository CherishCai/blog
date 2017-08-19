package cn.cherish.blog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人：Cherish
 * 联系方式：18826137274/785427346@qq.com 
 * @author Cherish
 * @date 2016年8月17日 下午6:51:31
 * @version 1.0
 */
public class ABaseController {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected static final int PAGE_SIZE = 20;
	protected static final int SUCCESS_CODE = 200;
	protected static final int FORBIDDEN_CODE = 403;
	protected static final int ERROR_CODE = 500;
	protected static final int NOT_FOUND_CODE = 404;
	protected static final int NOT_LOGIN_CODE = 100;

	protected Map<String, Object> getReturnMap(int code, String message, Object data) {
		Map<String, Object> map = new HashMap<>(8);
		map.put("code", code);
		map.put("message", message);
		map.put("data", data);
		return map;
	}
	
	protected Map<String, Object> getReturnMap(boolean success, String message, Object data) {
		Map<String, Object> map = new HashMap<>(8);
		map.put("success", success);
		map.put("message", message);
		map.put("data", data);
		return map;
	}

    protected Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> list = result.getFieldErrors();
        for (FieldError error : list) {
            log.debug("【参数检验】 error: {} -> {}", error.getField(), error.getDefaultMessage());
            map.put(error.getField(), error.getDefaultMessage());
        }
        return map;
    }


}
