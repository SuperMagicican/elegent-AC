package cn.elegent.ac;

/**
 * 异步调用处理接口（接收消息方）
 */
public interface ACHandler<T>{

    //业务处理的方法
    void process(String topic,T dto) throws Exception;
}
