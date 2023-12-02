package cn.elegent.ac.amqp;

import cn.elegent.ac.core.ACDistributer;
import cn.elegent.ac.core.ACTemplateImpl;
import cn.elegent.ac.core.SendBody;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
@Slf4j
@Component
public class AmqpTemplate extends ACTemplateImpl {

    @Autowired
    private Connection connection;

    @Autowired
    private ACDistributer acDistributer;

    @Override
    public boolean publish(String topic, SendBody sendBody) {

        try {
            Channel channel = connection.createChannel();
            //通道绑定对应消息队列
            //参数1:  队列名称 如果队列不存在自动创建
            //参数2:  用来定义队列特性是否要持久化 true 持久化队列   false 不持久化
            //参数3:  exclusive 是否独占队列  true 独占队列   false  不独占
            //参数4:  autoDelete: 是否在消费完成后自动删除队列  true 自动删除  false 不自动删除
            //参数5:  额外附加参数

            channel.queueDeclare(topic, true, false, false, null);
            String payload = JSON.toJSONString(sendBody  ) ;
            channel.basicPublish("", topic, MessageProperties.PERSISTENT_TEXT_PLAIN, payload.getBytes());
            channel.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void subscribe( String topic) {

        try {
            Channel channel = connection.createChannel();
            //通道绑定队列：与生产端一致
            channel.queueDeclare(topic, true, false, false, null);
            //获取消息
            //参数1: 消费那个队列的消息 队列名称
            //参数2: 开始消息的自动确认机制
            //参数3: 消费时的回调接口
            channel.basicConsume(topic, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("取出消息:===>" +consumerTag+"|"+ new String(body));
                    System.out.println(envelope.getRoutingKey());
                    acDistributer.distribute(topic,new String(body));
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void unSubscribe(String  topic) {

    }
}
