package cn.elegent.ac.mqtt.config;
import cn.elegent.ac.mqtt.MqttCallback;
import cn.elegent.ac.mqtt.config.ACConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 设置Mqtt
 * @author wgl
 */
@Configuration
@Component
@Data
@Slf4j
public class MqttBeans {

    @Autowired
    private ACConfig acConfig;

    @Autowired
    private MqttCallback mqttCallback;

    @Bean
    public MqttClient mqttClient() {
        String serverURI="tcp://"+ acConfig.getHost()+":"+acConfig.getPort();
        try {
            MqttClient client = new MqttClient(
                    serverURI,
                    acConfig.getClientId(),
                    new MemoryPersistence());
            client.setManualAcks(true); //设置手动消息接收确认
            //===================
            client.setCallback(mqttCallback);
            //====================
            client.connect(mqttConnectOptions());
            return client;
        } catch (MqttException e) {
            log.error("MQTT无法连接！！！！",e);
            return null;
        }
    }

    private MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(acConfig.getUsername());
        options.setPassword(acConfig.getPassword().toCharArray());
        options.setAutomaticReconnect(true);//是否自动重新连接
        options.setCleanSession(true);//是否清除之前的连接信息
        options.setConnectionTimeout(acConfig.getConnectionTimeout());//连接超时时间
        options.setKeepAliveInterval(acConfig.getKeepAliveInterval());//心跳
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);//设置mqtt版本
        return options;
    }

}

