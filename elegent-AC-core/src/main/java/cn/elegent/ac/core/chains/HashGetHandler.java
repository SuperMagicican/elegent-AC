package cn.elegent.ac.core.chains;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.core.CoreData;

public class HashGetHandler implements GetHandler{


    @Override
    public ACHandler get(String topic) {
        return CoreData.handlerMap.get(topic);
    }
}
