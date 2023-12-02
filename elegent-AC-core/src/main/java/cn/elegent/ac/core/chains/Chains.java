package cn.elegent.ac.core.chains;

import cn.elegent.ac.ACHandler;

import java.util.ArrayList;
import java.util.List;

public class Chains implements GetHandler{

    private static List<GetHandler> getHandlerList = new ArrayList<GetHandler>();

    public Chains addGetHandler(GetHandler getHandler){
        getHandlerList.add(getHandler);
        return this;
    }

    @Override
    public ACHandler get(String topic) {
        for (GetHandler index : getHandlerList) {
            ACHandler acHandler = index.get(topic);
            if(acHandler!=null){
                return acHandler;
            }
        }
        return null;
    }
}
