package fans.develop.fe;

/*
    自己把控的数据类型转换
 */
public class FeFormat {

    public static int StringToInt(String string){
        try{
            return Integer.valueOf(string);
        }catch(java.lang.NumberFormatException e){
            return 0;
        }
    }
}
