var question = {
	questionText: function (focusid) {
		var focusblurid = $(focusid);
		var defval = focusblurid.val();
		focusblurid.focus(function () {
			var thisval = $(this).val();
			if (thisval == defval) {
				$(this).val("");
			}
		});
		focusblurid.blur(function () {
			var thisval = $(this).val();
			if (thisval == "") {
				$(this).val(defval);
			}
		});
	},

	submitQuestion: function () {
		var questiontype = document.getElementById("question-type").value;
		var questiondesc = $("#question-desc").val();
		if (util.trim(questiondesc) == "" || questiondesc.length < 10 || questiondesc == language["comm.error.tips.137"]) {
			desc = language["comm.error.tips.137"];
			util.showerrortips("errortips", desc);
			return;
		}
		var url = "/question/submitQuestion.html?random=" + Math.round(Math.random() * 100);
		var param = {
			questiontype: questiontype,
			questiondesc: questiondesc
		};
		jQuery.post(url, param, function (data) {
			if (data.code == 0) {
				//util.layerAlert("", data.msg, 1);
				$("#errortips").empty();
				layer.msg(data.msg);
				setTimeout(function () {
					$('#backBT').click()
				}, 1000);
			} else {
				util.showerrortips("errortips", data.msg);
			}
		}, "json");
	},

	delquestion: function (data) {
		var questionid = $(data).data('question').questionid;
		util.layerConfirm(language["question.error.tips.1"],
			function (result) {
				var url = "/question/delquestion.html?random=" + Math.round(100 * Math.random()),
					param = {
						fid: questionid
					};
				$.post(url, param,
					function (questionid) {
						null != questionid && (location.reload(true), layer.close(result))
					})
			});
	},
	viewquestion: function (data) {
		var questionid = $(data).data('question').questionid;
		var url = "/question/findAnswer.html?random=" + Math.round(Math.random() * 100);
		var param = {
			questionid: questionid
		}
		jQuery.post(url, param, function (data) {
			$("#view-questiondetail .modal-body").empty();
			$("#view-questiondetail").modal("show");
			$.each(data.quests, function (key, value) {
				var anserele = $("#answer" + key);
				var kefu = "客服";
				if (anserele.length == 0) {
					if (value.userOfsys == "0" || value.userOfsys == 0) {
						$("#view-questiondetail .modal-body").append("<div  id ='anwser" + key + "' class='pannel panel-default yonghu'><div class='panel-heading'><h3 class='panel-title'>" + value.floginName + "</h3></div>" +
							"<span class='question_content'>" + value.fdesc + "</span><input type='hidden' class='fqid' name='fqid' value=" + value.fqid + "></div>");
					}
					if (value.userOfsys == "1" || value.userOfsys == 1) {
						$("#view-questiondetail .modal-body").append("<div  id ='anwser" + key + "' class='pannel panel-default kefu'><div class='panel-heading'><h3 class='panel-title'>" + kefu + "</h3></div>" +
							"<span class='answer_content'>" + value.fanswer + "</span></div>");
					}
				} else {

					anserele.children()[1].innerHTML = value.floginName;
					anserele.children()[2].innerHTML = value.fanswer;
				}

			})

		}, 'json')
	},
	view1question: function (data) {
		var questionid = $(data).data('question').questionid;
		var name = "暂时没有追问记录";
		var url = "/question/allAnwer.html?random=" + Math.round(Math.random() * 100);
		var param = {
			questionid: questionid
		}

		jQuery.post(url, param, function (data) {
			$("#questiondetail .modal-body").empty();
			/*if(data== ""|| data == null){
				util.layerAlert("",language["certification.error.tips.6"],4);
			}*/
			console.log(questionid);
			$.each(data.quests, function (key, value) {
				$("#questiondetail").modal("show");
				var questele = $("#modal" + key);
				var kefu = "客服";
				if (questele.length == 0) {
					if (value.userOfsys == "0" || value.userOfsys == 0) {
						$("#questiondetail .modal-body").append("<div  id ='que" + key + "' class='pannel panel-default yonghu'><div class='panel-heading'><h3 class='panel-title'>" + value.floginName + "</h3></div>" +
							"<span class='question_content'>" + value.fdesc + "</span><input type='hidden' class='fqid' name='fqid' value=" + value.fqid + "></div>");
					}
					if (value.userOfsys == "1" || value.userOfsys == 1) {
						$("#questiondetail .modal-body").append("<div  id ='que" + key + "' class='pannel panel-default kefu'><div class='panel-heading'><h3 class='panel-title'>" + kefu + "</h3></div>" +
							"<span class='answer_content'>" + value.fanswer + "</span></div>");
					}
				} else {
					questele.children()[1].innerHTML = value.floginName;
					questele.children()[2].innerHTML = value.fanswer;
				}
			})


		}, 'json')


	}
};

$(function () {
	
	/* 文本域 */
	question.questionText("#question-desc");
	/* 删除 */
	$(".delete").click(function () {
		question.delquestion(this);
	});
	$(".view").click(function () {
		question.viewquestion(this);
	});
	/* 提交问题 */
	$("#submitQuestion").on("click", function () {
		question.submitQuestion(false);
	});
	$(".view2").on("click", function () {
		question.view1question(this);
	});
	var count = false;
	$(".publish").on("click", function () {
		if (count) {
			return false;
		}
		count = true;
		var fqid = $(".fqid").val();
		var content = $(".ll-content").val();
		if (content.trim() == "" || content.trim() == null) {
			desc = language["certification.error.tips.5"];
			util.showerrortips('certificationinfo-errortips', desc);
			return;
		}

		var url = "/question/saveAnwer.html?random=" + Math.round(Math.random() * 100);
		var param = {
			fqid: fqid,
			content: content
		}
		jQuery.post(url, param, function (data) {
			count = false;
			if (data.code == 0) {
				util.layerAlert("", data.msg, 1);
			} else {
				util.showerrortips("errortips", data.msg);
			}
		}, 'json')

	})
	$(".modal .close").click(function () {
		location.reload(true);
	})

});