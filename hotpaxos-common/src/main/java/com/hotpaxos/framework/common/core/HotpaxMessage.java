package com.hotpaxos.framework.common.core;


import com.hotpaxos.framework.common.exception.HotPaxosException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 客户端和网关交互主体
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
public class HotpaxMessage extends HotpaxCon {
    private boolean nullObject;
    private Map<String, Object> properties;

    public boolean isNullObject() {
        return nullObject;
    }

    public HotpaxMessage(boolean nullObject) {
        this();
        this.nullObject = nullObject;
    }

    public HotpaxMessage() {
        properties = new HashMap<>();
    }


    public Object put(String key, Object value) {
        Object previous = this.properties.get(key);
        element(key, value);
        return previous;
    }

    public int size() {
        return this.properties.size();
    }

    public Set<String> keys() {
        this.verifyIsNull();
        return this.properties.keySet();
    }

    /**
     * 校验是否允许设置/传递属性值
     */
    private void verifyIsNull() {
        if (isNullObject()) {
            throw new HotPaxosException("HotpaxMessage is setted nullobject");
        }
    }

    //设置属性值 如果value为空则移除
    private HotpaxMessage element(String key, Object value) {
        this.verifyIsNull();
        if (null == key) {
            throw new HotPaxosException("HotpaxMessage set properties,key is null");
        } else {
            if (null == value) {
                this.properties.remove(key);
            } else {
                this.properties.put(key, value);
            }
        }
        return this;
    }

    public Object get(String key) {
        this.verifyIsNull();
        return this.properties.get(key);
    }

    public int getInt(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null != obj) {
            return obj instanceof Number ? ((Number) obj).intValue() : (int) getDouble(key);
        } else {
            throw new HotPaxosException("HotpaxMessage[" + key + "] is not a number");
        }
    }

    public double getDouble(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null != obj) {
            try {
                return obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj);
            } catch (Exception e) {
                throw new HotPaxosException("HotpaxMessage[" + key + "] is not a number");
            }
        } else {
            throw new HotPaxosException("HotpaxMessage[" + key + "] is not a number");
        }
    }

    public long getLong(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null != obj) {
            return obj instanceof Number ? ((Number) obj).longValue() : (long) getDouble(key);
        } else {
            throw new HotPaxosException("HotpaxMessage[" + key + "] is not a number");
        }
    }

    public byte getBytes(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null == obj) {
            return obj instanceof Number ? ((Number) obj).byteValue() : (byte) obj;
        } else {
            throw new HotPaxosException("HotpaxMessage[" + key + "] is not a number");
        }
    }

    public String getString(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null == obj) {
            return obj.toString();
        } else {
            throw new HotPaxosException("HotpaxMessage[" + key + "] not find");
        }

    }

    public boolean getBoolean(String key) {
        this.verifyIsNull();
        Object obj = get(key);
        if (null != obj) {
            if (obj.equals(Boolean.FALSE) || obj instanceof String && ((String) obj).equalsIgnoreCase("false")) {
                return false;
            }
            if (obj.equals(Boolean.TRUE) || obj instanceof String && ((String) obj).equalsIgnoreCase("true")) {
                return true;
            }
        }
        throw new HotPaxosException("HotpaxMessage[" + key + "] is not a boolean");
    }

    //设置消息体长度
    public void putContentLength(int contentLength) {
        this.put(Header.CONTENT_LENGTH, contentLength);
    }

    //设置协议版本
    public void putParserVersion(byte parserVersion) {
        this.put(Header.PARSER_VERSION, parserVersion);
    }

    //设置appid
    public void putAppId(int appid) {
        this.put(Header.APP_ID, appid);
    }

    //设置消息id
    public void putMessageId(int message) {
        this.put(Header.MESSAGE_ID, message);
    }

    //设置发送类型
    public void putSendType(byte sendType) {
        this.put(Header.SEND_TYPE, sendType);
    }

    public void putDeliveryId(String deliveryId) {
        this.put(Header.DELIVERY_ID, deliveryId);
    }

    //设置版本
    public void putVersion(int version) {
        this.put(Header.VERSION, version);
    }

    //设置数据包的类型
    public void putContentType(int contentType) {
        this.put(Header.CONTENT_TYPE, contentType);
    }

    public int messageId() {
        return this.getInt(Header.MESSAGE_ID);
    }

    public int contentLength() {
        return this.getInt(Header.CONTENT_LENGTH);
    }
}
