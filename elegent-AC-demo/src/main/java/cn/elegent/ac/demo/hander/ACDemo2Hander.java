package cn.elegent.ac.demo.hander;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import cn.elegent.ac.demo.dto.VmStatusContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 接收消息例子
 */
@Topic("server/#")
@Component
@Slf4j
public class ACDemo2Hander implements ACHandler<VmStatusContract> {

    @Override
    public void process(String topic,VmStatusContract dto) throws Exception {
        log.info(topic+"收到消息："+dto);
    }
}