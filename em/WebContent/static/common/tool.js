function vaildTelNum(telNum) {
	if(telNum!=""){
		//		var tel = 18767802354;
		var reg = /^0?1[0-9][0-9]\d{8}$/;
		if (reg.test(telNum)) { 
			return true;
		} else { 
			return false;
		}
	} 
	return true;
}

function showTipDef(msg, id){
	top.layer.tips(msg, '#'+id);
}

function vaildEmail(email)
{
	//对电子邮件的验证
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(!myreg.test(email))
	{
		return false;
	}
	return true;
}