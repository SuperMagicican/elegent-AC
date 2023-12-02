package cn.elegent.ac.amqp.config;

import cn.elegent.ac.amqp.config.ACConfig;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@Component
public class AmqpBeans {


    @Autowired
    private ACConfig acConfig;


    /**
     * 连接对象
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    @Bean
    public Connection connection() throws IOException, TimeoutException {
        ConnectionFactory factory= new ConnectionFactory();
        factory.setHost(acConfig.getHost());//主机
        factory.setPort(acConfig.getPort());//端口
        factory.setUsername(acConfig.getUsername());
        factory.setPassword(acConfig.getPassword());
        factory.setVirtualHost("/");
        return factory.newConnection();
    }



}
