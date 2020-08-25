package fans.develop.fe;

/*
    通用的链表结构升级版,添加唯一id及链表方法
 */
public class FeChain2<U> {
    public int id;
    public U data = null;
    public FeChain2<U> previous = null, next = null;

    //往链表后添加元素
    public void add(int id, U dat){
        FeChain2<U> tmp = this;
        while(tmp.next != null)
            tmp = tmp.next;
        tmp.next = new FeChain2<U>(id, dat);
        tmp.next.previous = tmp;
    }

    //根据id移除元素(不能移除this)
    public void remove(int id){
        FeChain2<U> tmp = this.next;
        while(tmp != null && tmp.id != id)
            tmp = tmp.next;
        if(tmp != null){
            //移花接木
            tmp.previous.next = tmp.next;
            if(tmp.next != null)
                tmp.next.previous = tmp.previous;
        }
    }

    //根据id查找元素值
    public U find(int id){
        FeChain2<U> tmp = this;
        while(tmp != null && tmp.id != id)
            tmp = tmp.next;
        if(tmp != null)
            return tmp.data;
        return null;
    }

    //设置唯一id和值
    public FeChain2(int id, U dat) {
        this.id = id;
        this.data = dat;
    }
}
