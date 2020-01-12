package com.hotpaxos.netty;

import com.hotpaxos.netty.annotation.HotPaxMsgAno;
import com.hotpaxos.netty.invoke.HandShakeInvoker;
import com.hotpaxos.netty.invoke.InvokerManager;
import com.hotpaxos.netty.invoke.InvokerType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
public class HotPaxNettyApplicationContext implements BeanPostProcessor {

    private InvokerManager invokerManager;

    public HotPaxNettyApplicationContext(InvokerManager invokerManager) {
        this.invokerManager = invokerManager;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                HotPaxMsgAno hotPaxMsgAno = method.getAnnotation(HotPaxMsgAno.class);
                if (null != hotPaxMsgAno) {
                    //创建代理类 执行invoke方法
                    //该方法第一个参数是TSession 第二个是传输对象
                    invokerManager.addInvoker(InvokerType.SHANKE_INVOKER, new HandShakeInvoker());
                }
            }
        }
        return bean;
    }
}
