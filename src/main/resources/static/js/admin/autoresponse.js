	/**
	 * 文章管理
	 * @author Cherish
	 * @date 2016年8月21日 下午9:10:05
	 */
	var oTable;
	$(document).ready(function() {
		oTable = $('#otable').DataTable(
			//拼接options参数
			$.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
			//ajax配置为function,手动调用异步查询
			"ajax" : function(data, callback, settings) {
				//封装请求参数
				var param = articleManage.getQueryCondition(data);
				$.ajax({
					type : "GET",
					url : "/autoresponse/list",//TODO
					cache : false, //禁用缓存
					data : param, //传入已封装的参数
					dataType : "json",
					success : function(result) {
						//异常判断与处理
						if (!result.success) {
							alert("查询失败。错误信息：" + result.message);
							return;
						}
						mdata = result.data;
						//封装返回数据，这里仅演示了修改属性名
						var returnData = {};
						returnData.draw = result.message;//这里直接自行返回了draw计数器,应该由后台返回
						returnData.recordsTotal = mdata.totalElements;
						returnData.recordsFiltered = mdata.totalElements;//后台不实现过滤功能，每次查询均视作全部结果
						returnData.data = mdata.content;
						//调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
						//此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
						callback(returnData);
					},
					error : function(XMLHttpRequest,textStatus,errorThrown) {
						alert("查询失败,error！");
					}
				});
			},
			"columns" : [
			    CONSTANT.DATA_TABLES.COLUMN.NO,
			    {
					"data" : 'keyword'
                }, {
                    "data" : 'message'
                }, {
                    "data" : 'msgType'
				}, {
					"data" : 'createdTime'
				},
				CONSTANT.DATA_TABLES.COLUMN.OPERATION
				],
			"columnDefs" : [ {
					"searchable" : false,
					"orderable" : false,
					"targets" : "_all"
				}]
		}));//end $('#otable').DataTable($.extend({
		
	});

	//客户表格的管理机制
	var articleManage = {
		currentItem : null,//储存当前被选中的行
		fuzzySearch : false,//是否模糊查询
		getQueryCondition : function(data) {
			var param = {};
			//组装排序参数
			if (data.order && data.order.length && data.order[0]) {
				switch (data.order[0].column) {
				case 1:
					param.orderColumn = "keyword";
					break;
				case 2:
					param.orderColumn = "message";
					break;
				case 3:
					param.orderColumn = "msgType";
					break;
				default:
					param.orderColumn = "id";
					break;
				}
				param.orderDir = data.order[0].dir;
			}
			//组装查询参数
			param.fuzzySearch = articleManage.fuzzySearch;
			if (articleManage.fuzzySearch) {//模糊查询
				param.fuzzy = $("#fuzzy-search").val();
			} else {//非模糊查询

			}
			//组装分页参数
			param.startIndex = data.start;
			param.pageSize = data.length;

			param.draw = data.draw;

			return param;
		}
	};

    //添加操作
    $('#otable_new').on('click',function (e) {
        e.preventDefault();

        window.open("/autoresponse/form");
    });

    //表格行删除操作
    $('#otable').on('click', 'a.op_delete', function (e) {
        e.preventDefault();
        var nRow = $(this).parents('tr')[0];

        var rowData = oTable.row(nRow).data();
        rowData.id;

    });

    //表格行编辑操作
    $('#otable').on('click', 'a.op_edit', function (e) {
        e.preventDefault();
        var nRow = $(this).parents('tr')[0];

        var rowData = oTable.row(nRow).data();

        window.open("/autoresponse/form/" + rowData.id);
    });
