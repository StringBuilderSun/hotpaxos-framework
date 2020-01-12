package com.hotpaxos.framework.common.core;

/**
 * PB 和 json 的互转
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public interface IParser {

    Object toBean(byte[] bytes);

    byte[] toBytes(Object content);

    IParser DEFAULT_PARSER=new IParser() {
        @Override
        public Object toBean(byte[] bytes) {
            return null;
        }

        @Override
        public byte[] toBytes(Object content) {
            return new byte[0];
        }
    };

}
