package com.sucre.test;

import static org.junit.Assert.*;

import com.sucre.service.WeiboCapcha;
import com.sucre.utils.JsUtil;
import com.sucre.utils.MyUtil;
import com.sucre.utils.SinaCapchaUtil;

public class Test {

	@org.junit.Test
	public void test() {
//		byte[] ret=SinaCapchaUtil.recombineShadow("5|24|31|38|0|22|16|12|29|25|3|13|30|33|2|27|17|8|15|9|20|11|6|23|32|34|26|1|19|21|14|10|18|4|39|37|7|35|28|36", MyUtil.loadByte("pic1.jpg"));
//	    MyUtil.outPutData("ret.jpg", ret);
//		
		// ret=88 ret1=53 ret2=54
		/*
		 * String ret=SinaCapchaUtil.grayImage2(MyUtil.loadByte("ret2.jpg"), 54);
		 * System.out.println(ret);
		 */
		System.out.println(System.currentTimeMillis());
/*		System.out.println("" + (char) 139 + (char) 77 + (char) 46 + (char) 02
				+ "ualMozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36"
				+ (char) 04 + "plat" + (char) 05 + "Win32" + (char) 04 + "lang" + (char) 05 + "zh-CN" + (char) 06
				+ "screen" + (char) 8 + "768*1366" + (char) 02 + "fp" + (char) 10 + "4129540459" + (char) 05 + "scale"
				+ (char) 01 + "1" + (char) 02 + "lt" + (char) 13 + "1542634041102" + (char) 02 + "ct" + (char) 13
				+ "1542634053359" + (char) 02 + "st" + (char) 13 +"1542634043758");*/
		for (int i = 0; i < 3; i++) {
			JsUtil.loadJs("js.js");
			WeiboCapcha capcha = new WeiboCapcha();
		    capcha.Actions(0, "getpicD");
			//String temp = capcha.simulate("147", "22");
			// System.out.println(temp );
			//temp = capcha.getjs(temp);
			//System.out.println(temp);
		}
	}

}