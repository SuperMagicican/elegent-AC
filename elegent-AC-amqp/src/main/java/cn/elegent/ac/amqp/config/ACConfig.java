package cn.elegent.ac.amqp.config;
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

    @Value("${elegent.ac.port:5672}")
    private int port;

    @Value("${elegent.ac.username:guest}")
    private String username;

    @Value("${elegent.ac.password:guest}")
    private String password;

    @Value("${elegent.ac.group:default}")
    private String group;

}

