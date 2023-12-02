package cn.elegent.ac.mqtt;

import cn.elegent.ac.ACACKHandler;
import cn.elegent.ac.core.ACDistributer;
import cn.elegent.ac.core.CoreData;
import cn.elegent.ac.mqtt.config.MqttBeans;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/**
 * 基于Eclipse paho设置事件
 * @author wgl
 */
@Component
@Slf4j
public class MqttCallback implements MqttCallbackExtended{

    @Autowired
    private ACDistributer acDistributer;//消息分发处理器

    @Autowired
    @Lazy
    private MqttClient mqttClient;

    @Autowired
    @Lazy
    private ACACKHandler acackHandler;


    @Autowired
    private MqttBeans mqttBeans;

    @Autowired
    private MqttTemplate mqttTemplate;

    @Override//连接断开
    public void connectionLost(Throwable throwable) {
        //当连接断开了自动重连
        log.info("MQTT连接丢失.",throwable);
    }


    //==============================重点关注==============================
    @Override//接收到消息
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("topic:" + topic + "  message:" + new String(message.getPayload()));
        //处理消息
        //topic  payload
        String msgContent = new String(message.getPayload());//获取emq发过来的消息体
        //开始处理事务
        log.info("接收到消息:"+msgContent);
        acDistributer.distribute(topic,msgContent);//消息分发
        mqttClient.messageArrivedComplete(message.getId(),message.getQos());
    }

    //==============================重点关注==============================
    @Override//对方收到消息
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            String[] topics = iMqttDeliveryToken.getTopics();
            MqttMessage mqttMessage = iMqttDeliveryToken.getMessage();
            if(mqttMessage!=null){
                String payload = new String(mqttMessage.getPayload());
                log.info("-------------deliveryComplete-------------{}",payload);
                acackHandler.deliveryComplete(topics[0],payload);
            }else{
                log.info("-------------deliveryComplete-------------");
            }
        }catch (Exception e){
            log.error("deliveryComplete");
        }
    }

    //==============================重点关注==============================
    @Override//连接完成
    public void connectComplete(boolean b, String s) {
       log.info("连接成功");
        //自动订阅topic
        if (CoreData.handlerMap.keySet().size()>0) {
            for (String  topic : CoreData.handlerMap.keySet()) {
                mqttTemplate.subscribe(topic);
            }
        }
    }

}