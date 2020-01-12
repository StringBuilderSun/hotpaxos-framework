package com.hotpaxos.netty;

import com.hotpaxos.framework.common.core.IHandler;
import com.hotpaxos.framework.common.core.IParser;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import com.hotpaxos.netty.annotation.HotPaxMsgAno;
import com.hotpaxos.netty.proxy.HandlerEnhance;
import com.hotpaxos.netty.proxy.ParserEnhance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

/**
 * 扫描HotPaxMessageAno注解 创建代理对象存入容器中备用
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public class RecieveHandlerPostProcessor implements BeanPostProcessor, Ordered {

    private int index;

    @Override
    public int getOrder() {
        return 100000;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        //获取其下所有方法 判断是否使用了 注解
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HotPaxMsgAno.class)) {
                //使用该注解的方法必须 至少2或者个参数  且第一个参数是TSession  第二个参数是Object 传输内容
                if (method.getParameterCount() != 2 && method.getParameterCount() != 3) {
                    throw new IllegalArgumentException(bean.getClass().getSimpleName() + "." + method.getName() + " ParamterCount must be two.")
                }
                if (!TSession.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    throw new IllegalArgumentException(bean.getClass().getSimpleName() + "." + method.getName() + " The first paramterCount must be TSession.")
                }
            }
            HotPaxMsgAno hotPaxMsgAno = method.getAnnotation(HotPaxMsgAno.class);
            int contentType = hotPaxMsgAno.contentType();
            int messageId = hotPaxMsgAno.messageId();
            String scope = hotPaxMsgAno.scope();
            Class cmdClass = method.getParameterTypes()[1];
            checkMessageId(messageId);
            IHandler handler = HandlerEnhance.invokeEnhance(bean, method, scope, ++index);
            registerHandler(messageId, cmdClass, handler);
            IParser parser = ParserEnhance.invokeEnhance(bean, method, cmdClass, scope, contentType, messageId, ++index);
            registerParser(parser);
        }
        return bean;
    }


    //使用协议适配 可以新增 删除 获取
    private void registerHandler(int messageId, Class cmdClass, IHandler handler) {
    }

    private void registerParser(IParser parser) {
    }


    //messagId 必须大于0
    private void checkMessageId(int messageId) {

    }
}
