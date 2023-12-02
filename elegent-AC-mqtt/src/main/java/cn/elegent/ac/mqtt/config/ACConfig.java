package cn.elegent.ac.mqtt.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * 设置Mqtt
 * @author wgl
 */
@Configuration
@Data
@Component
public class ACConfig {


    @Value("${elegent.ac.host:127.0.0.1}")
    private String host;

    @Value("${elegent.ac.port:1883}")
    private int port;

    @Value("${elegent.ac.username:admin}")
    private String username;

    @Value("${elegent.ac.password:public}")
    private String password;

    //private String serverURI;
    @Value("${elegent.ac.clientId:clientId}")
    private String clientId;

    @Value("${elegent.ac.keepAliveInterval:60}")
    private int keepAliveInterval;

    @Value("${elegent.ac.connectionTimeout:120}")
    private int connectionTimeout;

    @Value("${elegent.ac.ack:false}")
    private Boolean ack;

    @Value("${elegent.ac.group:default}")
    private String group;

}

