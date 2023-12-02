package cn.elegent.ac.context;


/**
 * 分布式消息分发数据交互上下文
 */
public class ElegentACContext {

    private final static ThreadLocal<String> my = new InheritableThreadLocal<String>();

    public static void set(String data){
        my.set(data);
    }

    public static String get(){
        return my.get();
    }

    public static void remove(){
        my.remove();
    }
}