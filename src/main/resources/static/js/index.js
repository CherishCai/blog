//
//	首页js
//
//	Prepared by Cherish Cai
//
//	感谢您的到来
//


$(document).ready(function() {
	
	//随机logo与头像
	var timemillis = Date.now();
	var logo = new Image();logo.src = "/image/logo.jpg";
	var myself = new Image();myself.src = "/image/myself.jpg";
	//logo显示多一点，myself少一点
	if(timemillis % 3 == 0){
		$("#logo").attr("src",myself.src);
		$("#myself").attr("src",logo.src);
	}else{
		$("#logo").attr("src",logo.src);
		$("#myself").attr("src",myself.src);
	}

	<!-- Slideshow Background  滑动也显示的背景 -->
    $.vegas("slideshow", {
        delay : 5000,
        backgrounds : [ {
				src : "/image/love.svg",
				fade : 2000
			},{
				src : "/image/cover1.jpg",
				fade : 2000
			}, {
				src : "/image/cover2.jpg",
				fade : 2000
			}, {
				src : "/image/cover3.jpg",
				fade : 2000
			}, {
				src : "/image/cover4.gif",
				fade : 2000
			}

        ]
    })("overlay", {
        src : "/image/overlay.png"
    });
    <!-- /Slideshow Background -->

	<!-- Mixitup : Grid 图片网格 -->
    $("#Grid").mixitup();

    <!-- Custom JavaScript for Smooth Scrolling - Put in a custom JavaScript file to clean this up -->
    $("a[href*=#]:not([href=#])").click(function() {
        if (location.pathname.replace(/^\//, "")
                == this.pathname.replace(/^\//, "")
                || location.hostname == this.hostname) {

            var target = $(this.hash);
            target = target.length ? target
                    : $("[name=" + this.hash.slice(1) + "]");
            if (target.length) {
                $("html,body").animate({
                    scrollTop : target.offset().top
                }, 1000);
                return false;
            }
        }
    });

    <!-- Navbar 屏幕顶部固定导航条 -->
    $("#nav").scrollToFixed();

	 <!-- Validate 表单验证 -->
	$("#contact-form").validate({
		rules : {
			name : {
				minlength : 2,
				required : true
			},
			email : {
				required : true,
				email : true
			},
			subject : {
				minlength : 2,
				required : true
			},
			message : {
				minlength : 2,
				required : true
			}
		},
		highlight : function(element) {
			$(element).closest(".form-group").removeClass(
					"success").addClass("error");
		},
		success : function(element) {
			element.text("OK!").addClass("valid").closest(
					".form-group").removeClass("error")
					.addClass("success");
		}
	});

}); // end document.ready
