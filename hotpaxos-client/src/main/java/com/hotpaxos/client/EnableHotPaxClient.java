package com.hotpaxos.client;

import com.hotpaxos.client.config.HotPaxClientMarkConfiguration;
import com.hotpaxos.netty.dispather.DefaultActionDispatcher;
import com.hotpaxos.netty.parser.DefaultParser;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 客户端标识注解
 * User: lijinpeng
 * Created by Shanghai on 2020/1/11
 */
//在运行时生效
@Retention(RetentionPolicy.RUNTIME)
@Documented
//作用于类上
@Target(ElementType.TYPE)
@Import(HotPaxClientMarkConfiguration.class)
public @interface EnableHotPaxClient {
    /**
     * socket传递的消息解码器
     *
     * @return
     */
    Class parser() default DefaultParser.class;

    /**
     * 消息分发器(用于查找消息的目的地和处理方法)
     *
     * @return
     */
    Class actionDispatch() default DefaultActionDispatcher.class;

    /**
     * 制定要扫描的包 ，这里指扫描使用了HotPaxMsgAno直接的类所在的包
     *
     * @return
     */
    String[] basePackages() default {};
}
