package com.hotpaxos.server;

import com.hotpaxos.netty.dispather.DefaultActionDispatcher;
import com.hotpaxos.netty.parser.DefaultParser;
import com.hotpaxos.server.config.HotPaxServerMarkerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(HotPaxServerMarkerConfiguration.class)
public @interface EnableHotPaxServer {
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
