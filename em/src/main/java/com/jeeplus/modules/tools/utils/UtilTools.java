package com.jeeplus.modules.tools.utils;

public class UtilTools {
	public static String getSeq(Integer count)
	{
		if (count<10)
			return "0000" + count;
		else if (count<100)
			return "000" + count;
		else if (count<1000)
			return "00" + count;
		else if (count<10000)
			return "0" + count;
		else
			return "" + count;
	}
}
