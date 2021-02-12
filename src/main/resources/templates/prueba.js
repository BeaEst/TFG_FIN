function error(){
	 $('body').css('display','block');
	 var show = sessionStorage.getItem("noLog");
	 if ( show == "show" ){
		 $("#noLogin").show();
	 }else{
		 $("#noLogin").hide();
	 }
}