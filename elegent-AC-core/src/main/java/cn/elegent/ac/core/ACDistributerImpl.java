package cn.elegent.ac.core;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.context.ElegentACContext;
import cn.elegent.ac.util.SpringAopTargetUtil;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 分发处理器
 */
@Component
@Slf4j
public class ACDistributerImpl implements ACDistributer {

    @Override
    public void distribute(String topic, String msgContent) {
        log.info("消息分发处理器--{}-{}",topic,msgContent);
        try {
            ACHandler acHandler = (ACHandler) SpringAopTargetUtil.getTarget(CoreData.get(topic));//这一步是重点 拿到消息的实现类
            Class dtoClass = getDtoClass(acHandler);
            Object body = null;
            if(isSendBody(msgContent)){
                log.info("sendBody----");
                SendBody sendBody = JSON.parseObject(msgContent,  SendBody.class ) ;
                String chainID = sendBody.getChainID();
                //将链路全局id传入到ThreadLocal中
                ElegentACContext.set(chainID);
                if (acHandler == null) return;
                //获取泛型的类型
                if(sendBody.getData()instanceof String){
                    body = JSON.parseObject( sendBody.getData().toString(), dtoClass);
                }else {
                    String msgJson =  JSON.toJSONString( sendBody.getData() );
                    body = JSON.parseObject(msgJson, dtoClass  ) ;
                }

            }else{
                log.info("非sendBody---直接处理");
                //非sendBody---直接处理
                body = JSON.parseObject(msgContent, dtoClass  ) ;
            }
            acHandler.process(topic, body); //执行处理消息的逻辑
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            ElegentACContext.remove();
        }
    }

    /**
     * 判断是否是框架提供的sendBody
     * @param msg
     * @return
     * @throws IOException
     */
    private boolean isSendBody(String msg) throws IOException {
        if(msg.contains("\"chainID\":")&&msg.contains("\"data\":")){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取泛型数据类型
     * @param object
     * @return
     */
    private Class getDtoClass(Object object){
        //获取泛型的类型
        Type genericInterface = object.getClass().getGenericInterfaces()[0];
        ParameterizedType parameterizedType=(ParameterizedType) genericInterface;
        Class dtoClass = (Class)parameterizedType.getActualTypeArguments()[0];
        return dtoClass;
    }

}
