package com.hotpaxos.framework.common.core;
/**
 * 消息传递的格式枚举，目前支持protobuf 和 json
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public enum ContentType {

    PB(TypeCode.PB, "PB"),
    JSON(TypeCode.JSON, "JSON");
    private String name;
    private int type;

    ContentType(int type, String name) {
        this.name = name;
        this.type = type;
    }

    public class TypeCode {
        public final static int PB = 1;
        public final static int JSON = 2;
    }
}
