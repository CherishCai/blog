/**
 * 封装ajax的请求函数
 */
$(document).ready(function() { $.ajaxSetup ({ cache: false  }); });	

/**
 * ajax的GET方法
 * 参数：url	 
 * 返回值：JSON格式的数据
 * 同步
 */	
function getAjax(url_post) {
	var result="" ;
	$.ajax({
		type : 'GET',
		url : url_post,						
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: false
	});
	return result;
}

/**
 * ajax的GET方法
 * 参数：url
 * 返回值：JSON格式的数据
 * 异步
 */
function getAjaxAsync(url_post) {
	var result="" ;
	$.ajax({
		type : 'GET',
		url : url_post,						
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: true
	});
	return result;
}

/**
 * ajax的POST方法
 * 参数：url, json格式的data
 * 返回值：JSON格式的数据
 * 异步
 */
function postAjaxAsync(url_post, data, callback) {
	$.ajax({
		type : 'POST',
		contentType : "application/json; charset=utf-8",
		url : url_post,
		data : JSON.stringify(data),							
		success : callback,
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: true
	});
}

/**
 * ajax的POST方法
 * 参数：url	 
 * 返回值：JSON格式的数据
 * 异步
 */	
function postAjaxNoDataAsync(url_post, callback) {
	$.ajax({
		type : 'POST',
		contentType : "application/json; charset=utf-8",
		url : url_post,						
		success : callback,
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: true
	});
}

/**
 * ajax的POST方法
 * 参数：url, json格式的data
 * 返回值：JSON格式的数据
 * 同步
 */
function postAjax(url_post, data) {
	var result="" ;
	$.ajax({
		type : 'POST',
		contentType : "application/json; charset=utf-8",
		url : url_post,
		data : JSON.stringify(data),							
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: false
	});
	return result;
}

/**
 * ajax的POST方法
 * 参数：url	 
 * 返回值：JSON格式的数据
 * 同步
 */	
function postAjaxNoData(url_post) {
	var result="" ;
	$.ajax({
		type : 'POST',
		contentType : "application/json; charset=utf-8",
		url : url_post,						
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: false
	});
	return result;
}
/**
 * ajax的PUT方法
 * 参数：url, json格式的data	 
 * 返回值：JSON格式的数据
 * 同步
 */	
function putAjax(url_put, data) {
	var result="" ;
	$.ajax({
		type : 'put',
		contentType : "application/json; charset=utf-8",
		url : url_put,
		data : JSON.stringify(data),							
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: false
	});
	return result;
}
/**
 * ajax的POST方法
 * 参数：url	 
 * 返回值：JSON格式的数据
 * 同步
 */	
function putAjaxNoData(url_put) {
	var result="" ;
	$.ajax({
		type : 'put',
		contentType : "application/json; charset=utf-8",
		url : url_put,					
		success : function(result1) { result = result1; },
		dataType : 'json',
		error : function(XMLHttpRequest, textStatus, errorThrown) { },
		async: false
	});
	return result;
}