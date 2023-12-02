package cn.elegent.ac.core.chains;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.core.CoreData;


public class MapKeyWildcardGetHandler implements GetHandler{


    @Override
    public ACHandler get(String topic) {
        for (String indexKey : CoreData.handlerMap.keySet()) {
            if(indexKey.contains("+")){
                //如果包含+ 说明是单级通配符
                String[] split = indexKey.split("/\\+");
                String key = "";
                for (String index : split) {
                    key+=index;
                }
                //handlerMap中获取
                if(topic.startsWith(key)){
                    return CoreData.handlerMap.get(indexKey);
                }
            }else if(indexKey.contains("#")){
                //如果包含# 说明是多级通配符
                //如果包含/#说明是多级通配符
                String[] split = indexKey.split("#");
                String key = split[0];
                //handlerMap中获取
                if(topic.startsWith(key)){
                    return CoreData.handlerMap.get(indexKey);
                }
            }
        }
        return null;
    }
}
