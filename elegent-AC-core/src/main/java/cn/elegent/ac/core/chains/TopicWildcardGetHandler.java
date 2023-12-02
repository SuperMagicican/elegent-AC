package cn.elegent.ac.core.chains;


import cn.elegent.ac.ACHandler;
import cn.elegent.ac.core.CoreData;

import java.util.Set;


public class TopicWildcardGetHandler implements GetHandler{


    @Override
    public ACHandler get(String topic) {
        //1） 跟进名称完整匹配 -- 如果拿不到 采取通配符获取
        if(CoreData.handlerMap.get(topic)!=null){
            return CoreData.handlerMap.get(topic);
        }else if(topic.contains("+")){
            //如果包含/+说明是单级通配符
            String[] split = topic.split("/\\+");
            String key = "";
            for (String index : split) {
                key+=index;
            }
            //handlerMap中获取
            Set<String> keys = CoreData.handlerMap.keySet();
            for (String index : keys) {
                if(index.startsWith(key)){
                    return CoreData.handlerMap.get(index);
                }
            }
        }else if(topic.contains("#")){
            //如果包含/#说明是多级通配符
            String[] split = topic.split("#");
            String key = split[0];
            //handlerMap中获取
            Set<String> keys = CoreData.handlerMap.keySet();
            for (String index : keys) {
                if(index.startsWith(key)){
                    return CoreData.handlerMap.get(index);
                }
            }
        }
        return null;
    }
}
