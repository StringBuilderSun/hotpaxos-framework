package com.hotpaxos.netty.annotation;

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

}
