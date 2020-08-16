package fans.develop.fe;

/*
    通用的链表结构
 */
public class FeChain<U> {
    public U data = null;
    public FeChain<U> previous = null, next = null;

    public FeChain(U dat) {
        data = dat;
    }
}
