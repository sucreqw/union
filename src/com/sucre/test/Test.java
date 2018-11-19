package com.sucre.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import com.sucre.utils.MyUtil;
import com.sucre.utils.SinaCapchaUtil;

class Test {

	@org.junit.jupiter.api.Test
	void test() {
		
		/*byte[] ret=SinaCapchaUtil.recombineShadow("2|6|30|31|10|5|33|39|35|9|3|23|17|0|18|21|26|38|34|12|22|29|11|24|1|37|28|4|36|32|7|20|14|16|27|13|25|19|15|8", MyUtil.loadByte("test.jpg"));
	    MyUtil.outPutData("ret.jpg", ret);
		*/
		
		String ret=SinaCapchaUtil.grayImage2(MyUtil.loadByte("ret.jpg"), 100, 100, 100);
		System.out.println(ret);
		
		/*String temp="1122^2222^3333";
		String[] tem=temp.split("\\^");
		System.out.println(tem[1]);*/
	}

}
