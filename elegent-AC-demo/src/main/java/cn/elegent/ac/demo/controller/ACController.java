package cn.elegent.ac.demo.controller;

import cn.elegent.ac.ElegentAC;
import cn.elegent.ac.demo.dto.ACDTO;
import cn.elegent.ac.demo.dto.VmStatusContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 发送消息（调用微服务）实例
 */
@RestController
public class ACController {

    @Autowired
    private ElegentAC elegentAc;  //消息发送器



    /**
     * 测试发送消息
     * @return
     */
    @GetMapping("/test_ac")
    public boolean test_ac(){
        ACDTO acdto=new ACDTO();
        acdto.setName("test");
        acdto.setPoint(10);
        return elegentAc.publish("testtopic",acdto);
    }

    /**
     * 延迟发送消息
     * @return
     */
    @GetMapping("/delayPublish")
    public boolean delayPublish(){
        ACDTO acdto=new ACDTO();
        acdto.setName("test");
        acdto.setPoint(10);
        return elegentAc.delayPublish("testtopic",acdto,60);
    }

    /**
     * 测试发送消息（主题带通配符）
     * @return
     */
    @GetMapping("/test_ac2")
    public boolean test_ac2(){
        VmStatusContract vmStatusContract=new VmStatusContract();
        vmStatusContract.setInnerCode("A000001");
        vmStatusContract.setStatusInfo(new ArrayList<>());
        return elegentAc.publish("server/order/goods/status",vmStatusContract);
    }

    /**
     * 测试发送消息
     * @return
     */
    @GetMapping("/test_ac3")
    public boolean test_ac3(){
        ACDTO acdto=new ACDTO();
        acdto.setName("test");
        acdto.setPoint(10);
        return elegentAc.publish("testtopic/acdemo",acdto);
    }


}