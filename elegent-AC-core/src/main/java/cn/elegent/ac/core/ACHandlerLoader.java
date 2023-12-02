package cn.elegent.ac.core;
import cn.elegent.ac.ACHandler;
import cn.elegent.ac.ElegentAC;
import cn.elegent.ac.annotation.Topic;
import cn.elegent.ac.util.SpringAopTargetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息处理类加载器（策略模式）
 * @author wgl
 */
@Component
@Slf4j
public class ACHandlerLoader implements ApplicationContextAware{

    @Autowired
    //@Lazy
    private ElegentAC elegentAc;  //订阅器

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        log.info("消息处理类加载器启动");
        //key 订阅的主题 vlaue 是消息处理类
        // 是基于类获取bean对象
        //key是类名  value:具体的消息处理类
        //key: supplyTaskHandler   class
        // 拿到了所有的消息处理类
        Map<String,ACHandler> map = ctx.getBeansOfType(ACHandler.class);//拿到IOC容器中所有的MsgHandler的类

        //我们需要的是什么  map  key : 该消息处理类处理的主题名称 value: 消息处理类
        map.values().stream().forEach(v->{
            //当前消息处理类应该处理的是哪个主题下对应的消息
            try {
                Object target = SpringAopTargetUtil.getTarget(v);
                Topic annotation = target.getClass().getAnnotation(Topic.class);  //获取注解
                if (annotation != null) {
                    //开起了自动订阅的才会添加到List集合中去进行自动订阅
                    String topicName = "";
                    if(annotation.value().contains("${")){
                        topicName = getYmlPerporties(annotation.value());
                    }else {
                        topicName = annotation.value();
                    }
                    CoreData.handlerMap.put(topicName, v);  //处理器集合
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        //订阅主题
        //自动订阅topic
        if (CoreData.handlerMap.keySet().size()>0) {
            for (String topic : CoreData.handlerMap.keySet()) {
                elegentAc.subscribe(topic);
            }
        }
    }

    @Autowired
    private Environment environment;

    /**
     * 获取
     * @param value
     * @return
     */
    private String getYmlPerporties(String value) {
        String regex = "\\$\\{(.*?)\\}";
        String key = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        List<String> list = new ArrayList<>();
        int i = 1;
        while (matcher.find()) {
            list.add(matcher.group(i));
        }
        for (String index : list) {
            key = value.replaceAll("\\$\\{" + index + "\\}", environment.getProperty(index));
        }
        return key;
    }

}