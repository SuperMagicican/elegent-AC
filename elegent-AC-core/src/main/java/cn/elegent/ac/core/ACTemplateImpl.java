package cn.elegent.ac.core;

import cn.elegent.ac.ElegentAC;
import cn.elegent.ac.context.ElegentACContext;
import cn.elegent.ac.util.UUIDUtils;

import java.util.UUID;


public abstract class ACTemplateImpl implements ElegentAC {


    public boolean publish(String topic, Object data){
        return sendTemplate(topic,data,null);
    }


    public boolean delayPublish(String topic, Object data,int seconds){
        return sendTemplate(topic,data,seconds);
    }


    public boolean sendTemplate(String topic, Object data,Integer seconds){
        String chainID = ElegentACContext.get();
        if(ElegentACContext.get()==null) {
            chainID = UUIDUtils.getUUID();
        }
        SendBody sendBody = new SendBody(data,chainID);
        if(seconds!=null){
            return this.delayPublish(topic,sendBody,seconds);
        }else{
            return this.publish(topic,sendBody);
        }

    }


    public abstract boolean publish(String topic, SendBody data);

    public abstract void subscribe(String topic);


    public abstract void unSubscribe(String topic);
}
