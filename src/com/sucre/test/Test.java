package com.sucre.test;

import static org.junit.Assert.*;

import com.sucre.utils.MyUtil;
import com.sucre.utils.SinaCapchaUtil;

public class Test {

	@org.junit.Test
	public void test() {
//		byte[] ret=SinaCapchaUtil.recombineShadow("5|24|31|38|0|22|16|12|29|25|3|13|30|33|2|27|17|8|15|9|20|11|6|23|32|34|26|1|19|21|14|10|18|4|39|37|7|35|28|36", MyUtil.loadByte("pic1.jpg"));
//	    MyUtil.outPutData("ret.jpg", ret);
//		
		//ret=88 ret1=53 ret2=54
		String ret=SinaCapchaUtil.grayImage2(MyUtil.loadByte("ret2.jpg"), 54);
		System.out.println(ret);
		
		/*String temp="1122^2222^3333";
		String[] tem=temp.split("\\^");
		System.out.println(tem[1]);*/
	}

}
