package cn.elegent.ac;


public interface ElegentAC {

    boolean publish(String topic, Object sendBody);



    boolean delayPublish(String topic, Object sendBody,int seconds);


    void subscribe(String topic);


    void unSubscribe(String topic);



}
