package com.hotpaxos.framework.common.utils;

import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.framework.common.core.retry.RetryPolicy;
import io.netty.util.AttributeKey;

import java.util.regex.Pattern;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public class HotpaxosConstans {
    public static final String SEPERATOR_ACCESS_LOG = "|";
    public static final String COMMA_SEPARATOR = ",";
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
    public static final String PROTOCOL_SEPARATOR = "://";
    public static final String PATH_SEPARATOR = "/";
    public static final String PATH_UNDERLINE = "_";
    public static final String REGISTRY_SEPARATOR = "|";
    public static final Pattern REGISTRY_SPLIT_PATTERN = Pattern.compile("\\s*[|;]+\\s*");
    public static final String SEMICOLON_SEPARATOR = ";";
    public static final String COLON_SEPARATOR = ":";
    public static final Pattern SEMICOLON_SPLIT_PATTERN = Pattern.compile("\\s*[;]+\\s*");
    public static final String QUERY_PARAM_SEPARATOR = "&";
    public static final Pattern QUERY_PARAM_PATTERN = Pattern.compile("\\s*[&]+\\s*");
    public static final String EQUAL_SIGN_SEPERATOR = "=";
    public static final Pattern EQUAL_SIGN_PATTERN = Pattern.compile("\\s*[=]\\s*");
    public static final int SECOND_MILLS = 1000;
    public static final int MINUTE_MILLS = 60 * SECOND_MILLS;
    public static final int DEFAULT_VALUE = 0;
    public static final String Empty = "";

    /**
     * propertyChangeSupport name
     */
    public static final String RECONNECT_LISTENER_NAME = PATH_UNDERLINE + "reconnect";
    /**
     * Tsession 的scope
     */
    public static final String DEFAULT_SCOPE = "default";
    public static final String HAND_SHAKE_SCOPE = "hand-shake";
    public static final String AGENT_SCOPE = "agent";
    /**
     * netty
     */
    public static final AttributeKey<Integer> TID_KEY = AttributeKey.valueOf("t-id");
    public static final AttributeKey<RetryPolicy> RETRY_POLICY_KEY = AttributeKey.valueOf("retry_policy");
    public static final AttributeKey<TSession> SESSION_ID = AttributeKey.valueOf("session_id");


    /**
     * ping  pong
     */
    /**
     * ping messageId
     */
    public static final int PING_MESSAGE_ID = 100;
    /**
     * pong messageId
     */
    public static final int PONG_MESSAGE_ID = PING_MESSAGE_ID + 1;
    /**
     * syn messageId
     */
    public static final int SYN_MESSAGE_ID = PING_MESSAGE_ID + 2;
    /**
     * syn_ack messageId
     */
    public static final int SYN_ACK_MESSAGE_ID = PING_MESSAGE_ID + 3;
}
