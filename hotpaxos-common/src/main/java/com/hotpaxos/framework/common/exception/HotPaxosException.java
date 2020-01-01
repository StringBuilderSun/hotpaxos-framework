package com.hotpaxos.framework.common.exception;


/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
public class HotPaxosException extends RuntimeException {
    /**
     * 默认构造函数
     */
    public HotPaxosException() {
    }

    /**
     * 外部传入异常信息
     *
     * @param message
     */
    public HotPaxosException(String message) {
        super(message);
    }

    /**
     * 外部传入异常
     *
     * @param throwable
     */
    public HotPaxosException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 外部异常/信息 构造函数
     *
     * @param message
     * @param throwable
     */
    public HotPaxosException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
