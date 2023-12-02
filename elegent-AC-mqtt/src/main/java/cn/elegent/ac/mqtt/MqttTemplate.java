package cn.elegent.ac.mqtt;
import cn.elegent.ac.core.ACTemplateImpl;
import cn.elegent.ac.core.SendBody;
import cn.elegent.ac.mqtt.config.ACConfig;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MqttTemplate extends ACTemplateImpl {


    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private ACConfig acConfig;


    @Override
    public boolean publish(String topic, SendBody sendBody) {
        String payload = "";
        try {
            payload = JSON.toJSONString(sendBody  );
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            if(acConfig.getAck()){
                mqttMessage.setQos(0);
            }else{
                mqttMessage.setQos(2);
            }
            mqttMessage.setRetained(false);
            mqttClient.publish(topic,mqttMessage);
            return true;
        }catch (MqttPersistenceException e) {
            e.printStackTrace();
            return false;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean delayPublish(String topic, Object sendBody,int seconds){
        return this.publish( "$delayed/"+seconds+"/"+topic, sendBody );
    }

    @Override
    public void subscribe(String  topic) {
        log.info("开始订阅主题");
        try {
            if(Strings.isEmpty(acConfig.getGroup())){//如果没有群组
                mqttClient.subscribe("$queue/" +topic);
            }else {
                mqttClient.subscribe("$share/"+ acConfig.getGroup()+"/"+  topic);
            }
        } catch (MqttException e) {
            log.error("emq connect error", e);
        }
    }

    @Override
    public void unSubscribe(String topic) {
        log.info("开始取消订阅主题");
        //和EMQ连接成功后根据配置自动订阅topic
        try {
            log.info(">>>>>>>>>>>>>>subscribe topic:" + topic);
            if(Strings.isEmpty(acConfig.getGroup())){//如果没有群组
                mqttClient.unsubscribe("$queue/" +topic);
            }else {
                mqttClient.unsubscribe("$share/"+ acConfig.getGroup()+"/"+  topic);
            }
        } catch (MqttException e) {
            log.error("emq connect error", e);
        }
    }
}
