package cn.elegent.ac.core;

/**
 * 异步调用消息分发处理器
 */
public interface ACDistributer {


    void distribute(String topic,String msgContent);

}
