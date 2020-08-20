package fans.develop.fe;

import java.util.*;

/*
		随机数生成器
 */
public class FeRandom
{
		/*
				取得[start, end]范围内的随机整数
		 */
		public static int getInt(int start, int end){
				Random r = new Random();
				int range = end - start + 1;
				return r.nextInt(range) + start;
		}
}
