package cn.elegent.ac.util;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

public class SpringAopTargetUtil {

    public static Object getTarget(Object proxy) throws Exception {
        if(!AopUtils.isAopProxy(proxy)) {
            /**
             * 不是代理对象
             */
            return proxy;
        }
        if(AopUtils.isJdkDynamicProxy(proxy)) {
            /**
             * jdk代理
             */
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            /**
             * cglib 代理
             */
            return getCglibProxyTargetObject(proxy);
        }
    }
    /**
     * @MethodName: getCglibProxyTargetObject
     * @Description: CGLIB方式被代理类的获取
     * @Author: Jiajiajia
     * @Params:  * @param proxy
     * @Return {@link Object}
     * @date 2020/12/20
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }
    /**
     * @MethodName: getJdkDynamicProxyTargetObject
     * @Description: JDK动态代理方式被代理类的获取
     * @Author: Jiajiajia
     * @Params:  * @param proxy
     * @Return {@link Object}
     * @date 2020/12/20
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }
}
