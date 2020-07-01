/****************************************************************
* comm_fileupload.js
* owner : chcho
* reg date : 2016-04-06
* description : 파일업로드 관련 공통 자바스크립트*
****************************************************************/

/* 첨부파일 추가
 * obj : input type=file 객체
 * objId : input box 의 id
 * multYn : N 일경우 1개 파일만 첨부가능 , Y일경우 여러개 입력가능
 * */
function fn_fileMultiAtt(obj, objId, multYn){
	var fcnt = $("#file1_cnt").val();
	if(fcnt == ''){
		fcnt = "1";
	}else{
		fcnt = parseInt(fcnt)+1;
	}
	
	  $img_li = $("ul."+objId).find('li');
	  $filObj = $(obj); 
	  if(multYn == 'N' && $img_li.size() > 0){
		  alert('Already attached. Delete the attached file if you change it.');
		  $filObj.replaceWith(  $filObj = $(obj).clone(true) );
		  return;
	  }	
	
	fValue = $(obj).val();
	var pathHeader = fValue.lastIndexOf("\\");
 	var pathMiddle = fValue.lastIndexOf(".");
 	var pathEnd = fValue.length;
 	var fileName = fValue.substring(pathHeader+1, pathMiddle);
 	var extName = fValue.substring(pathMiddle+1, pathEnd);

	var finputObId = objId+"_file_no_"+fcnt;
	var fliId = objId+"_file_li_"+fcnt;
	
	//
	var apply_ext = "hwp,pdf,xls,xlsx,ppt,pptx,doc,docx,dwg,jpg,png,bmp,tif,gif,txt,avi,asf,mpeg,mov,wmv,mp4,skm,k3g,mp3,wav,zip,xml";
	if(apply_ext.indexOf(","+extName)<0){
		alert("Restrict File, Please check it.");
		$("#"+objId).replaceWith($("#"+objId).clone());
		$("#"+objId).focus();
		return ;
	}
	
	var liClass = "";
	if(extName == 'pdf'){
		liClass ='class="pdf"';
	}else if(extName == 'doc' || extName == 'docx'){
		liClass ='class="doc"';
	}else if(extName == 'xls' || extName == 'xlsx'){
		liClass ='class="xls"';
	}else if(extName == 'ppt' || extName == 'pptx'){
		liClass ='class="ppt"';
	}

	var html = '<li id="'+fliId+'" '+liClass+'>'+fileName+"."+extName;
		html += '	<a href="javascript:fn_delAttFile(\''+fliId+'\')"><img src="/resources/images/icon-delete.png" alt="삭제버튼이미지"></a>';
		html += '</li>';
		
	$(obj).hide();
	
	if(multYn == 'Y'){
		$("."+objId).append(html);
	}else{
		$("."+objId).html(html);
	}
	$("#"+fliId).prepend($(obj));
	
	var oldhtml = '<input type="file"  id="'+finputObId+'" name="'+obj.name+'" onchange="javascript:fn_fileMultiAtt(this, \''+objId+'\',\''+multYn+'\');" />';
	$("."+objId).parent().prepend(oldhtml);		
	$("#file1_cnt").val(fcnt);
}

function fn_delAttFile(objId){
	
	if(confirm("Delete the attached file?")){
		$("#"+objId).remove();
	}
	/*
	if ($("#delRefArtcleSeq").val() == "") {
		$("#delRefArtcleSeq").val(refArtcleSeq);
	} else {
		var value = $("#delRefArtcleSeq").val();
		$("#delRefArtcleSeq").val(value + "," + refArtcleSeq);
	}
	
	if ($("#fileSeq").val() == "") {
		$("#fileSeq").val(fileSeq);
	} else {
		var value = $("#fileSeq").val();
		$("#fileSeq").val(value + "," + fileSeq);
	}
	*/
}

function fn_delRealAttFile(tbId,refBrdId,refArtcleSeq,refReSeq,fileSeq){
	// /file/rem
	if(confirm("Delete the attached file?")){
		
        $.ajax({
            url : "/file/rem/file_rem.do",
            type : 'post',
            dataType : 'json',
            data: { tbId:tbId, refBrdId:refBrdId, refArtcleSeq:refArtcleSeq, refReSeq:refReSeq, fileSeq: fileSeq},
            success : function(data) { 
                if(data.CODE == "101"){
                    alert(data.MESSAGE);
                    $("#att_fileno_"+filesq).remove();
                    location.reload();
                } else {
                    alert(data.MESSAGE);
                }
            },
            error: function () {
                alert("Delete failed..");
                return false;
            }
        }); 
	}
}
