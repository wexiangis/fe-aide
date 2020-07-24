package fans.develop.fe;

/*
    自己把控的数据类型转换
 */
public class FeFormat {

    public static int StringToInt(String string){
        try{
            return Integer.parseInt(string);
        }catch(java.lang.NumberFormatException e){
            return 0;
        }
    }

    /*
        从hex字符串提取int数据
     */
    public static int HexStringToInt(String string){
        int ret = 0;
        int count = 0;
        //过滤掉 "0x" "0X"
        if(string.length() > 1){
            if(string.charAt(0) == '0' && 
                (string.charAt(1) == 'x' || string.charAt(1) == 'X'))
            count += 2;
        }
        //提取hex数据
        for(; count < string.length(); count++){
            char c = string.charAt(count);
            ret <<= 4;
            if(c >= '0' && c <= '9')
                ret += c - '0';
            else if(c >= 'a' && c <= 'z')
                ret += c - 'a' + 10;
            else if(c >= 'A' && c <= 'Z')
                ret += c - 'A' + 10;
            else
                break;
        }
        return ret;
    }
}
