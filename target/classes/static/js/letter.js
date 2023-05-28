$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	//获取到要发送的数据
	var toUsername = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送一个异步请求
	$.post(
		//请求路径
		CONTEXT_PATH+"/message/send",
		//请求参数
		{"toUsername":toUsername,"content":content},
		//回调函数，用于处理返回的数据
		function (data){
			//将满足json格式的String转换为可以识别的Json格式数据
			data = $.parseJSON(data);
			if(data.code==0){
				$("#hintBody").text("发送成功")
			}else {
				$("#hintBody").text(data.msg)
			}
			//页面刷新
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}