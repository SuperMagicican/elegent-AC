package cn.elegent.ac;



public interface ACACKHandler<T> {


    public void deliveryComplete(String topic,T params);

}
