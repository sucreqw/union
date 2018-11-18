package com.sucre.test;

import static org.junit.jupiter.api.Assertions.*;

import com.sucre.utils.MyUtil;
import com.sucre.utils.SinaCapchaUtil;

class Test {

	@org.junit.jupiter.api.Test
	void test() {
		
		/*byte[] ret=SinaCapchaUtil.recombineShadow("7|2|6|34|19|4|29|31|30|12|3|37|28|10|27|20|8|33|15|38|9|23|11|32|1|35|36|25|0|14|17|21|13|16|24|22|5|39|26|18", MyUtil.loadByte("test.jpg"));
	    MyUtil.outPutData("ret.jpg", ret);*/
		
		String temp="1122^2222^3333";
		String[] tem=temp.split("\\^");
		System.out.println(tem[1]);
	}

}
