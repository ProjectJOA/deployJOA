$(document).ready(function() {
	$(".gnb > li > a").on("click", function() {
		$(this).parent().toggleClass("active").siblings().removeClass("active");
	});

	$(".gnb > li > ul").on("mouseleave", function() {
		$(this).parent().removeClass("active");
	});
});


/* 숫자만 입력받기 */
function fn_press(event, type) {
    if(type == "numbers") {
        if((event.keyCode < 48) || (event.keyCode > 57)) return false;
        //onKeyDown일 경우 좌, 우, tab, backspace, delete키 허용 정의 필요
    }
}
/* 한글입력 방지 */
function fn_press_han(obj)
{
    //좌우 방향키, 백스페이스, 딜리트, 탭키에 대한 예외
    if(event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39
    || event.keyCode == 46 ) return;
    //obj.value = obj.value.replace(/[\a-zㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
    obj.value = obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
}	