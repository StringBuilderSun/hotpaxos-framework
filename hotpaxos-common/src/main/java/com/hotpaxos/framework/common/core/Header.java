package com.hotpaxos.framework.common.core;

/**
 * 消息头
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
public interface Header {
    //消息长度  type ==== int
    String CONTENT_LENGTH = "contentLength";
    //前四位协议版本  后四位格式类型  type==== byte
    String PARSER_VERSION = "parserVersion";
    //应用服务的appid
    String APP_ID = "appId";
    //消息id  type==== int
    String MESSAGE_ID = "messageId";
    //广播类型 type ==== byte
    String SEND_TYPE = "sendType";
    //业务id type==== int
    String BIZ_ID = "bizId";
    //deliveryId的类型(int long string) deliveryCategory==== byte
    String DELIVERY_CATEGORY = "deliveryCategory";
    //deliveryId的长度  当deliveryId为String的时候存在
    String DELIVERY_ID_LENGTH = "deliveryIdLength";
    //单播为用户id  组播或广播为组id   type=====String
    String DELIVERY_ID = "deliveryId";
    //协议版本
    String VERSION = "version";
    //数据包类型  type====int
    String CONTENT_TYPE = "contentType";
    //用户SessionUID的数据类型   type ====  byte
    String SESSION_UID_CATEGORY = "sessionUidGategory";
    //用户sessionUid的长度   type ==== char
    String SESSION_UID_LENGTH = "sessionUidLength";
    //用户sessionUID
    String SESSION_UID = "sessionUid";
}
