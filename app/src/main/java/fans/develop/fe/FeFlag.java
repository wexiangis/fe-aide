package fans.develop.fe;

/*
    各种flag的父类,提供基本操作方法
 */
public class FeFlag {

    public boolean[] array = null;

    public FeFlag(int arrayLen){
        if(arrayLen < 0)
            arrayLen = 1;
        array = new boolean[arrayLen];
        for(int i = 0; i < array.length; i++)
            array[i] = false;
    }

    // 置标志
    public void setFlag(int type){
        if(array == null)
            return;
        if(type < 0 || type >= array.length)
            return;
        array[type] = true;
    }
    // 置标志,清除其它
    public void setOnlyFlag(int type){
        cleanFlagAll();
        setFlag(type);
    }
    // 查标志
    public boolean checkFlag(int type){
        if(array == null)
            return false;
        if(type < 0 || type >= array.length)
            return false;
        return array[type];
    }
    // 清标志
    public void cleanFlag(int type){
        if(array == null)
            return;
        if(type < 0 || type >= array.length)
            return;
        array[type] = false;
    }
    // 清某一类型的全部标志
    public void cleanFlagAll(){
        if(array == null)
            return;
        for(int i = 0; i < array.length; i++)
            array[i] = false;
    }

}
