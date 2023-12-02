package cn.elegent.ac.core;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.core.chains.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心数据
 */
public class CoreData {


    /**
     * 消息处理类map
     */
    public static Map<String, ACHandler> handlerMap = new HashMap<String,ACHandler>();//key:主题 value:消息处理类

    /**
     * 主题列表
     */
    //public static Set<Topic> topicList=new HashSet<>();


    private static Chains chains = new Chains();

    static {
        chains.addGetHandler(new HashGetHandler())
                .addGetHandler(new TopicWildcardGetHandler())
                .addGetHandler(new MapKeyWildcardGetHandler())
                ;
    }

    public static Object get(String topic) {
        return chains.get(topic);
    }

}
