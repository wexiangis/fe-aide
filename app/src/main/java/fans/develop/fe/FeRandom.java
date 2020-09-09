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
    Random random = new Random();
    int range = end - start + 1;
    return random.nextInt(range) + start;
  }

  /*
    把[start, end]中的整数填入数组array中
   */
  public static void getArray(int[] array, int start, int end){
    Random random = new Random();
    int range = end - start + 1;
    for(int i = 0; i < array.length; i++)
      array[i] = random.nextInt(range) + start;
  }

  /*
    不重复的把[start, start + array.length)中的整数填入数组array中
   */
  public static void getArray2(int[] array, int start){
    Random random = new Random();
    //先按顺序填数据
    for(int i = 0; i < array.length; i++)
      array[i] = start + i;
    
    int len = array.length;
    for(; len > 0;){
      //每次取一个len--的随机数(即数组元素序号)
      int order = random.nextInt(len--);//注意len--后已经指向最后一个元素
      //然后把该序号的数据交换到最后面
      if(order != len){
        int tmp = array[len];
        array[len] = array[order];
        array[order] = tmp;
      }
    }
  }
}
