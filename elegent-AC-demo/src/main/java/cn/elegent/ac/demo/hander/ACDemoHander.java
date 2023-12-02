package cn.elegent.ac.demo.hander;

import cn.elegent.ac.ACHandler;
import cn.elegent.ac.annotation.Topic;
import cn.elegent.ac.demo.dto.ACDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 接收消息例子
 */
@Topic("testtopic")
@Slf4j
public class ACDemoHander implements ACHandler<ACDTO> {

    @Override
    public void process(String topic,ACDTO dto) throws Exception {
        log.info(topic+"收到消息："+dto.getName());
    }
}