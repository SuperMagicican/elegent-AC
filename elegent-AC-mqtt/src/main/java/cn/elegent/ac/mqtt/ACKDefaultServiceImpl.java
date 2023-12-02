package cn.elegent.ac.mqtt;

import cn.elegent.ac.ACACKHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(prefix = "elegent.ac",name = "ack",havingValue = "false")
@Component
public class ACKDefaultServiceImpl implements ACACKHandler {
    @Override
    public void deliveryComplete(String topic, Object params) {
        log.info("确认来自："+topic+"的消息："+params);
    }
}
