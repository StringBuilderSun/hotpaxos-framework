package com.hotpaxos.netty.annotation;

import com.hotpaxos.framework.common.core.ContentType;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;

import java.lang.annotation.*;

/**
 * 消息注解
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HotPaxMsgAno {
    /**
     * 作用域 用于区别多连接情况下的客户端
     *
     * @return
     */
    String scope() default HotpaxosConstans.DEFAULT_SCOPE;

    /**
     * 消息id 默认是0
     *
     * @return
     */
    int messageId() default HotpaxosConstans.DEFAULT_VALUE;

    /**
     * 默认数据传递使用protobuf
     *
     * @return
     */
    int contentType() default ContentType.TypeCode.PB;
}
